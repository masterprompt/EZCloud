; This Procedure is for free MPZ (@) from Berlin
; Version 0.1 1/2004
; 
; in the USERLIBS must be the file kernel32.decls
;.lib "kernel32.dll"
;api_RtlMoveMemory(Destination*,Source,Length) : "RtlMoveMemory"

; in the USERLIBS must be the file comdlg32.decls
;.lib "comdlg32.dll"
;api_ChooseColor% (pChoosecolor*) : "ChooseColorA"
Const OFN_ALLOWMULTISELECT			= 512
Const OFN_CREATEPROMPT				= $2000
Const OFN_FILEMUSTEXIST				= $1000
Const OFN_HIDEREADONLY				= 4
Const OFN_NOCHANGEDIR				= 8
Const OFN_NONETWORKBUTTON			= $20000
Const OFN_NOREADONLYRETURN			= $8000
Const OFN_NOVALIDATE				= 256
Const OFN_OVERWRITEPROMPT			= 2
Const OFN_PATHMUSTEXIST				= $800
Const OFN_READONLY					= 1
Const OFN_LONGNAMES					= $200000
Const OFN_NOLONGNAMES				= $40000
Const OFN_EXPLORER					= $80000
AppTitle("KCSAPP")
Global hWnd		= FUI_API_FindWindow( "Blitz Runtime Class", "KCSAPP" )
	
Function FUI_MessageBox( msg$, title$="", style=0 )
	If title$ = "" title$ = AppTit$
	Return FUI_API_MessageBox( hWnd, msg$, title$, style )
End Function		
		
Function FUI_OpenDialog$( title$ = "", initdir$ = "", filter$ = "", flags = 0, index = 1 )
	If title$ = "" title$ = "Select a File to Open..."
	If filter$ = "" filter$ = "All Files (*.*)|*.*"
	
	lpofn = CreateBank(19*4)
	
	PokeInt lpofn, 0, BankSize( lpofn )
	
	bnkStart = CreateBank( Len( initdir$ ) + 1 )
	strStart = FUI_API_lstrcpy( bnkStart, initdir$ )
	
	bnkFilter = CreateBank( Len( filter$ ) + 1 )
	strFilter = FUI_API_lstrcpy( bnkFilter, filter$ )
	
	For i = 0 To BankSize( bnkFilter) - 1
		If PeekByte( bnkFilter, i ) = Asc("|")
			PokeByte bnkFilter, i, 0
		EndIf
	Next
	
	bnkTitle = CreateBank( Len( title$ ) + 1 )
	strTitle = FUI_API_lstrcpy( bnkTitle, title$ )
	
	PokeInt lpofn, 4, hWnd
	PokeInt lpofn, 12, strFilter
	
	fname$ = String( " ", 5120 )
	bnkFile = CreateBank( Len( fname$ ) + 1 )
	strFile = FUI_API_lstrcpy( bnkFile, fname$)
	
	PokeInt lpofn, 24, index
	PokeInt lpofn, 28, strFile
	PokeInt lpofn, 32, BankSize( bnkFile )
	PokeInt lpofn, 44, strStart
	PokeInt lpofn, 48, strTitle
	PokeInt lpofn, 52, OFN_EXPLORER Or OFN_FILEMUSTEXIST Or flags
	
	Result = FUI_API_OpenDialog( lpofn )
	If Result <> 0
		currentFile$ = FUI_GetFilename( bnkFile, flags )
		currentIndex = PeekInt( lpofn, 24 )
		FreeBank bnkStart
		FreeBank bnkFilter
		FreeBank bnkFile
		FreeBank lpofn
		
		Return currentFile$
	Else
		FreeBank bnkStart
		FreeBank bnkFilter
		FreeBank bnkFile
		FreeBank lpofn
		
		Return ""
	EndIf
	
End Function
Function FUI_SaveDialog$( title$ = "", initdir$ = "", filter$ = "", index = 1 )
	
	If title$ = "" title$ = "Select a File to Open..."
	If filter$ = "" filter$ = "All Files (*.*)|*.*"
	
	lpofn = CreateBank(19*4)
	
	PokeInt lpofn, 0, BankSize( lpofn )
	
	bnkStart = CreateBank( Len( initdir$ ) + 1 )
	strStart = FUI_API_lstrcpy( bnkStart, initdir$ )
	
	bnkFilter = CreateBank( Len( filter$ ) + 1 )
	strFilter = FUI_API_lstrcpy( bnkFilter, filter$ )
	
	For i = 0 To BankSize( bnkFilter) - 1
		If PeekByte( bnkFilter, i ) = Asc("|")
			PokeByte bnkFilter, i, 0
		EndIf
	Next
	
	bnkTitle = CreateBank( Len( title$ ) + 1 )
	strTitle = FUI_API_lstrcpy( bnkTitle, title$ )
	
	PokeInt lpofn, 4, hWnd
	PokeInt lpofn, 12, strFilter
	
	fname$ = String( " ", 2048 )
	bnkFile = CreateBank( Len( fname$ ) + 1 )
	strFile = FUI_API_lstrcpy( bnkFile, fname$)
	
	PokeInt lpofn, 24, index
	PokeInt lpofn, 28, strFile
	PokeInt lpofn, 32, BankSize( bnkFile)
	PokeInt lpofn, 44, strStart
	PokeInt lpofn, 48, strTitle
	PokeInt lpofn, 52, OFN_EXPLORER Or OFN_OVERWRITEPROMPT
	
	Result = FUI_API_SaveDialog( lpofn )
	If Result <> 0
		currentFile$ = FUI_GetFilename( bnkFile )
		currentIndex = PeekInt( lpofn, 24 )
		FreeBank bnkStart
		FreeBank bnkFilter
		FreeBank bnkFile
		FreeBank lpofn
		Return currentFile
	Else
		Return ""
	EndIf

End Function
Function FUI_GetFilename$( bnkIn, flags=0 )
	
	fname$ = ""
	For i = 0 To BankSize( bnkIn ) - 1
		byte = PeekByte( bnkIn, i )
		If flags = OFN_ALLOWMULTISELECT
			If byte = 0
				If byte = lbyte And i > 0
					Exit
				Else
					byte = Asc( "|"	)
				EndIf
			EndIf
			If byte > 0
				fname$ = fname$ + Chr( byte )
			EndIf
			If byte = Asc( "|" )
				lbyte = 0
			Else
				lbyte = byte
			EndIf
		Else
			If byte = 0 Exit
			fname$ = fname$ + Chr( byte )
		EndIf
	Next
	Return fname$
	
End Function
Function Color_Dialog(RGB=0)
	Return FUI_ColorDialog(RGB)
End Function

Function FUI_ColorDialog( RGB=0)
	
	lpofn = CreateBank(9*4)
	
	PokeInt lpofn, 0, BankSize( lpofn )
	PokeInt lpofn, 4, hWnd
	
	bnkStart = CreateBank( 64 )
	
	Address = CreateBank( 4 )
	FUI_API_RtlMoveMemory2( Address, bnkStart+4, 4 )
	
	rgbResult = RGB;FUI_RGBToInt( B, G, R,-1 )
	PokeInt lpofn, 12, rgbResult
	PokeInt lpofn, 16, PeekInt( Address, 0 )
	PokeInt lpofn, 20, 2 Or $1
	
	Result = FUI_API_ColorDialog( lpofn )
	
	rgbResult = PeekInt( lpofn, 12 )
	
	;RGB format is reversed
	currentRGB = FUI_GetRed( rgbResult ) Or (FUI_GetGreen( rgbResult ) Shl 8) Or (FUI_GetBlue( rgbResult ) Shl 16)
	
	FreeBank Address
	FreeBank bnkStart
	FreeBank lpofn
	
	If Result <> 0
		Return currentRGB
	Else
		Return -1
	EndIf
	
End Function

Function FUI_GetRed( RGB )

	Return (RGB Shr 16) And $FF
	
End Function

Function FUI_GetGreen( RGB )

	Return (RGB Shr 8) And $FF
	
End Function

Function FUI_GetBlue( RGB )

	Return RGB And $FF
	
End Function
Function FUI_RGBToInt( Red, Green, Blue, Alpha=255 )
	
	If Alpha >-1
		Return (Alpha Shl 24) Or (Red Shl 16) Or (Green Shl 8) Or Blue
	Else
		Return (Red Shl 16) Or (Green Shl 8) Or Blue
	EndIf
	
End Function