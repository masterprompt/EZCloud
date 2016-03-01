Function GUI_FLAG(XL_VAL,XL_FLG)
	RET=(XL_VAL And XL_FLG)
	If RET<>0
		Return True
	Else
		Return False
	EndIf
End Function

Function GUI_TXTITEMS(XL_TXT$)
	XL_POS=1
	While XL_POS>0 And XL_POS<Len(XL_TXT$)+1
		XL_POS=Instr(XL_TXT$,"|",XL_POS)
		If XL_POS>0
			XL_POS=XL_POS+1
			XL_CNT=XL_CNT+1
		EndIf
	Wend
	Return XL_CNT
End Function

Function GUI_PARSE$(XL_TXT$,XL_ITM=0)
	XL_START=1
	If XL_ITM=0
		XL_END=Instr(XL_TXT$,"|")
	Else
		For XL_T=0 To XL_ITM-1
			XL_START=Instr(XL_TXT$,"|",XL_START)+1
		Next
		XL_END=Instr(XL_TXT$,"|",XL_START)
		If XL_END=0
			XL_END=Len(XL_TXT$)
		EndIf
		If XL_END<XL_START
			Return ""
		EndIf
	EndIf
	RET$=Mid$(XL_TXT$,XL_START,XL_END-XL_START)
	Return RET$
End Function

Function GUI_WIDEST_ITM(XL_TXT$)
	For T=0 To GUI_TXTITEMS(XL_TXT$)-1
		xl_ITM$=GUI_PARSE$(XL_TXT$,T)
		If GUI_STRINGWIDTH(XL_ITM$)>XL_W
			XL_W=GUI_STRINGWIDTH(XL_ITM$)
		EndIf
	Next
	Return XL_W
End Function

Function GUI_FINDICON.ICON(XL_ICON$)
	If XL_ICON$=""
		Return Null
	Else
		For XL_ICN.ICON=Each ICON
			If Upper$(XL_ICN\NAME$)=Upper$(XL_ICON$)
				Return XL_ICN
			EndIf
		Next
		Return Null
	EndIf
End Function

Function GUI_MESSWIDTH(XL_TXT$)
	If Instr(XL_TXT$,Chr$(13))=0 And Instr(XL_TXT$,"¬")=0
		Return GUI_STRINGWIDTH(XL_TXT$)
	Else
		XL_W=0
		XL_TXT$=Replace$(XL_TXT$,"¬",Chr$(13))
		XL_T=1
		If Right$(XL_TXT$,1)<>Chr$(13)
			XL_TXT$=XL_TXT$+Chr$(13)
		EndIf
		While XL_DONE=False
			XL_POS=Instr(XL_TXT$,Chr$(13),XL_T)
			XL_LINE$=Mid$(XL_TXT$,XL_T,XL_POS-XL_T)
			If GUI_STRINGWIDTH(XL_LINE$)>XL_W
				XL_W=GUI_STRINGWIDTH(XL_LINE$)
			EndIf
			XL_T=XL_T+Len(XL_LINE$)+1
			If XL_T>Len(XL_TXT) Or XL_POS=0
				XL_DONE=True
			EndIf
		Wend
		Return XL_W
	EndIf
End Function

Function GUI_MESSHEIGHT(XL_TXT$)
	If Instr(XL_TXT$,Chr$(13))=0 And Instr(XL_TXT$,"¬")=0
		Return 14
	Else
		XL_W=0
		XL_TXT$=Replace$(XL_TXT$,"¬",Chr$(13))
		XL_POS=1
		If Right$(XL_TXT$,1)<>Chr$(13)
			XL_TXT$=XL_TXT$+Chr$(13)
		EndIf
		While XL_DONE=False
			XL_POS=Instr(XL_TXT$,Chr$(13),XL_POS)
			If XL_POS<>0
				XL_H=XL_H+1
				XL_POS=XL_POS+1
				If XL_POS>Len(XL_TXT$)
					XL_DONE=True
				EndIf
			Else
				XL_DONE=True
			EndIf
		Wend
		Return XL_H*14
	EndIf
End Function

Function GUI_PRINT(XL_X,XL_Y,XL_TXT$,XL_INV=False)
	XL_LEN=Len(XL_TXT$)
	If XL_LEN=1
		XL_ASC=Asc(XL_TXT$)
		If GUI_CHAR(XL_ASC)=Null
			XL_ASC=32
		EndIf
		If XL_ASC=32 Or XL_ASC=160
			If XL_INV=True
				Color 0,0,GUI_FONTCOL
				Rect XL_X,XL_Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
			EndIf
			XL_X=XL_X+GUI_CHAR(XL_ASC)\W
		Else
			If XL_INV=False
				DrawImageRect GUI_FONTIMG_ALPHA,XL_X,XL_Y,GUI_CHAR(XL_ASC)\X,GUI_CHAR(XL_ASC)\Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
			Else
				DrawImageRect GUI_FONTIMG_INV,XL_X,XL_Y,GUI_CHAR(XL_ASC)\X,GUI_CHAR(XL_ASC)\Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
			EndIf
			XL_X=XL_X+GUI_CHAR(XL_ASC)\W
		EndIf
	Else
		For XL_T=1 To XL_LEN
			XL_ASC=Asc(Mid$(XL_TXT$,XL_T,1))
			If GUI_CHAR(XL_ASC)=Null
				XL_ASC=32
			EndIf
			If XL_ASC=32 Or XL_ASC=160
				If XL_INV=True
					Color 0,0,GUI_FONTCOL
					Rect XL_X,XL_Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
				EndIf
				XL_X=XL_X+GUI_CHAR(XL_ASC)\W
			Else
				If XL_INV=False
					DrawImageRect GUI_FONTIMG_ALPHA,XL_X,XL_Y,GUI_CHAR(XL_ASC)\X,GUI_CHAR(XL_ASC)\Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
				Else
					DrawImageRect GUI_FONTIMG_INV,XL_X,XL_Y,GUI_CHAR(XL_ASC)\X,GUI_CHAR(XL_ASC)\Y,GUI_CHAR(XL_ASC)\W,GUI_CHAR(XL_ASC)\H
				EndIf
				XL_X=XL_X+GUI_CHAR(XL_ASC)\W
			EndIf
		Next
	EndIf
	Return XL_X
End Function


Function GUI_TEXT(xl_TXT$,xl_X,xl_Y,xl_W,xl_H=14,xl_ALIGN=0,xl_WRAP=False,XL_PASS=False)
			
	If XL_PASS=True
		XL_TXT$=Replace$(XL_TXT$,Chr$(13),"¬")
		For XL_T=1 To Len(XL_TXT$)
			If Mid$(XL_TXT$,XL_T,1)<>"¬"
				XL_A$=XL_A$+"*"
			Else
				XL_A$=XL_A$+"¬"
			EndIf
		Next
		XL_TXT$=Replace$(XL_A$,"¬",Chr$(13))
	EndIf
	
	If XL_WRAP=False
		XL_TXT$=Replace$(XL_TXT$,"¬"," ")
	EndIf
	
	XL_STRINGWIDTH=GUI_STRINGWIDTH(XL_TXT$)
	If XL_STRINGWIDTH<XL_W
		XL_TXT$=Replace$(XL_TXT$,"¬"," ")
		Select XL_ALIGN
			Case 0
				GUI_PRINT XL_X,XL_Y,XL_TXT$
			Case 1
				GUI_PRINT (XL_X+(XL_W/2))-(XL_STRINGWIDTH/2),XL_Y,XL_TXT$
			Case 2
				GUI_PRINT (XL_X+XL_W)-XL_STRINGWIDTH,XL_Y,XL_TXT$
		End Select
	Else
		If XL_WRAP=False
			XL_TXT$=Replace$(XL_TXT$,"¬"," ")
			XL_LINE$=GUI_ONELINE$(XL_TXT$,XL_W)
			If XL_LINE$<>""
				XL_TXT$=XL_LINE$
				XL_STRINGWIDTH=GUI_STRINGWIDTH(XL_TXT$)
				Select XL_ALIGN
					Case 0
						GUI_PRINT XL_X,XL_Y,XL_TXT$
					Case 1
						GUI_PRINT (XL_X+(XL_W/2))-(XL_STRINGWIDTH/2),XL_Y,XL_TXT$
					Case 2
						GUI_PRINT (XL_X+XL_W)-XL_STRINGWIDTH,XL_Y,XL_TXT$
				End Select
			EndIf	
		Else
			XL_POS=1
			XL_YY=XL_Y
			While XL_DONE=False
				XL_WORD$=GUI_RIPWORD$(XL_TXT$,XL_POS)
				XL_OVERRUN=False
				XL_NEWLINE=False
				If XL_WORD$<>""
					xl_POS=xl_POS+Len(xl_WORD$)
					If Asc(Right$(xl_WORD$,1))=13 Or Right$(xl_WORD$,1)="¬"
						XL_NEWLINE=True
						xl_WORD$=Left$(xl_WORD$,Len(xl_WORD$)-1)
					EndIf
					If xl_POS>Len(xl_TXT$)
						xl_DONE=True
					EndIf
					XL_WORDW=GUI_STRINGWIDTH(XL_WORD$)
					If XL_WORDW<XL_W
						If GUI_STRINGWIDTH(XL_LINE$)+XL_WORDW<XL_W
							XL_LINE$=XL_LINE$+XL_WORD$
						Else
							XL_OVERRUN=True
						EndIf
						If XL_OVERRUN=True
							Select XL_ALIGN
								Case 0
									GUI_PRINT XL_X,XL_YY,XL_LINE$
								Case 1
									GUI_PRINT ((XL_X+XL_W/2)-GUI_STRINGWIDTH(XL_LINE$)/2),XL_YY,XL_LINE$
								Case 2
									GUI_PRINT (XL_X+XL_W)-GUI_STRINGWIDTH(XL_LINE$),XL_YY,XL_LINE$
							End Select
							XL_YY=XL_YY+14
							If XL_YY>XL_Y+XL_H-14
								XL_DONE=True
							EndIf
							XL_LINE$=XL_WORD$
						EndIf
						If XL_NEWLINE=True
							Select XL_ALIGN
								Case 0
									GUI_PRINT XL_X,XL_YY,XL_LINE$
								Case 1
									GUI_PRINT ((XL_X+XL_W/2)-GUI_STRINGWIDTH(XL_LINE$)/2),XL_YY,XL_LINE$
								Case 2
									GUI_PRINT (XL_X+XL_W)-GUI_STRINGWIDTH(XL_LINE$),XL_YY,XL_LINE$
							End Select
							XL_YY=XL_YY+14
							If XL_YY>XL_Y+XL_H-14
								XL_DONE=True
							EndIf
							XL_LINE$=""
						EndIf
					Else
						XL_WORD_POS=1
						XL_WORD_DONE=False
						While XL_WORD_DONE=False
							XL_CHAR$=Mid$(XL_WORD$,XL_WORD_POS,1)
							If GUI_STRINGWIDTH(XL_LINE$+XL_CHAR$)<XL_W
								XL_LINE$=XL_LINE$+XL_CHAR$
							Else
								Select XL_ALIGN
									Case 0
										GUI_PRINT XL_X,XL_YY,XL_LINE$
									Case 1
										GUI_PRINT ((XL_X+XL_W/2)-GUI_STRINGWIDTH(XL_LINE$)/2),XL_YY,XL_LINE$
									Case 2
										GUI_PRINT (XL_X+XL_W)-GUI_STRINGWIDTH(XL_LINE$),XL_YY,XL_LINE$
								End Select
								XL_YY=XL_YY+14
								If XL_YY>XL_Y+XL_H-14
									XL_DONE=TURE:XL_WORD_DONE=True
								EndIf
								XL_LINE$=XL_CHAR$
							EndIf
							XL_WORD_POS=XL_WORD_POS+1
							If XL_WORD_POS>Len(XL_TXT$)
								XL_WORD_DONE=True
							EndIf
						Wend
					EndIf
				Else
					XL_DONE=True
				EndIf
			Wend
			If XL_LINE$<>"" And XL_YY<XL_Y+XL_H-14
				Select XL_ALIGN
					Case 0
						GUI_PRINT XL_X,XL_YY,XL_LINE$
					Case 1
						GUI_PRINT ((XL_X+XL_W/2)-GUI_STRINGWIDTH(XL_LINE$)/2),XL_YY,XL_LINE$
					Case 2
						GUI_PRINT (XL_X+XL_W)-GUI_STRINGWIDTH(XL_LINE$),XL_YY,XL_LINE$
				End Select
			EndIf
		EndIf
	EndIf
End Function

Function GUI_STRINGWIDTH(XL_TXT$)
	For XL_T=1 To Len(XL_TXT$)
		XL_ASC=Asc(Mid$(XL_TXT$,XL_T,1))
		If XL_ASC>31
			If GUI_CHAR(XL_ASC)=Null
				XL_ASC=32
			EndIf
			XL_W=XL_W+GUI_CHAR(XL_ASC)\W
		EndIf
	Next
	Return XL_W
End Function

Function GUI_STRINGHEIGHT(XL_TXT$)
	Return GUI_CHAR(32)\H
End Function



Function GUI_ONELINE$(xl_TXT$,xl_W,XL_POS=1)
	While xl_PRINTED=False
		xl_A$=Mid$(xl_TXT$,xl_POS,1)
		If GUI_STRINGWIDTH(RET$+xl_A$)<xl_W And Asc(xl_A$)>31
			RET$=RET$+xl_A$
		Else
			xl_PRINTED=True
		EndIf
		xl_POS=xl_POS+1
		If xl_POS>Len(xl_TXT$)
			xl_PRINTED=True
		EndIf
	Wend
	Return RET$
End Function

Function GUI_WRAPRIPWORD$(xl_TXT$,xl_POS)
	While xl_FINI=False
		xl_A$=Mid$(xl_TXT$,xl_POS,1)
		xl_AS=Asc(xl_A$)
		If xl_AS=32 Or xl_AS=10 Or xl_AS=13 Or XL_A$="¬" Or xl_AS=9
			xl_FINI=True
			If xl_AS=32 Or xl_AS=9
				RET$=RET$+Chr$(xl_AS)
			Else
				RET$=RET$+Chr$(13)
			EndIf
		Else
			RET$=RET$+xl_A$
			xl_POS=xl_POS+1
			If xl_POS>Len(xl_TXT$)
				xl_FINI=True
			EndIf
		EndIf
	Wend
	Return RET$
End Function

Function GUI_RIPWORD$(xl_TXT$,xl_POS)
	While xl_FINI=False
		xl_A$=Mid$(xl_TXT$,xl_POS,1)
		xl_AS=Asc(xl_A$)
		If xl_AS=32 Or xl_AS=10 Or xl_AS=13 Or XL_A$="¬"
			xl_FINI=True
			If xl_AS=32 Or xl_AS=9
				RET$=RET$+Chr$(xl_AS)
			Else
				RET$=RET$+Chr$(13)
			EndIf
		Else
			RET$=RET$+xl_A$
			xl_POS=xl_POS+1
			If xl_POS>Len(xl_TXT$)
				xl_FINI=True
			EndIf
		EndIf
	Wend
	Return RET$
End Function

Function GUI_PARSE_KEY()
	If GUI_KEYDOWN<>GUI_OLDKEYDOWN Or GUI_OLDKEYDOWN<1
		Select GUI_KEYDOWN
			Case 82
				GUI_GETKEY=48
			Case 79
				GUI_GETKEY=49
			Case 80
				GUI_GETKEY=50
			Case 81
				GUI_GETKEY=51
			Case 75
				GUI_GETKEY=52
			Case 76
				GUI_GETKEY=53
			Case 77
				GUI_GETKEY=54
			Case 71
				GUI_GETKEY=55
			Case 72
				GUI_GETKEY=56
			Case 73
				GUI_GETKEY=57
			Case 83
				GUI_GETKEY=46
		End Select
	EndIf
End Function

Function GUI_FLOAT_STR$(XL_VAL#,XL_DEC=2)
	XL_FS$=XL_VAL#
	XL_FS$=Upper$(XL_FS$)
	
	For XL_T=0 To XL_DEC-1
		XL_D$=XL_D$+"0"
	Next
	
	If XL_VAL#<>0
	
		If Instr(XL_FS$,"E")
		
			XL_EXP_VAL$=Mid$(XL_FS$,Instr(XL_FS$,"E")+1)
			XL_EVAL=XL_EXP_VAL$
			For XL_T=0 To Abs(XL_EVAL)-1
				XL_ZERO$=XL_ZERO$+"0"
			Next
			
			XL_DEC_PLACE=Instr(XL_FS$,".")
			XL_INT_VAL$=Left$(XL_FS$,XL_DEC_PLACE-1)
			XL_DEC_VAL$=Mid$(XL_FS$,XL_DEC_PLACE+1,Instr(XL_FS$,"E")-XL_DEC_PLACE-1)
			XL_DEC_VAL$=Left$(XL_DEC_VAL$,XL_DEC-1)
					
			If Left$(XL_INT_VAL$,1)="-"
				XL_MINUS=True
				XL_INT_VAL$=Mid$(XL_INT_VAL$,2)
			EndIf
			
			If Instr(XL_FS$,"E-")
				If XL_ZERO$<>"0"
					XL_FIN$="0."+Mid$(XL_ZERO$,2)
				Else
					XL_FIN$="0."
				EndIf
				XL_DEC_VAL$=XL_INT_VAL$+XL_DEC_VAL$
				XL_DEC_VAL$=Left$(XL_DEC_VAL$,XL_DEC-1)
				XL_FIN$=XL_FIN$+XL_DEC_VAL$
				If Len(XL_ZERO$)>XL_DEC
					XL_FIN$="0."+XL_D$
				EndIf
			Else
				XL_FIN$=XL_INT_VAL$+XL_DEC_VAL$+XL_ZERO$+"."+XL_D$
			EndIf
					
			If XL_MINUS=True
				XL_FIN$="-"+XL_FIN$
			EndIf
			
			Return XL_FIN$
		Else
			XL_DEC_PLACE=Instr(XL_FS$,".")
			XL_DEC_VAL$=Mid$(XL_FS$,XL_DEC_PLACE+1,XL_DEC)
			If Len(XL_DEC_VAL$)<XL_DEC
				XL_DEC_VAL$=XL_DEC_VAL$+"0"
			EndIf
			XL_INT_VAL$=Left$(XL_FS$,XL_DEC_PLACE)
			Return XL_INT_VAL$+XL_DEC_VAL$
		EndIf
		
	Else
		XL_FIN$="0."+XL_D$
		Return XL_FIN$
	EndIf
	
End Function

Function QLIMIT(XL_A,XL_B,XL_C)
	;SIMPLE LIMITER
	If XL_A<XL_B
		Return XL_B
	Else
		If XL_A>XL_C
			Return XL_C
		Else
			Return XL_A
		EndIf
	EndIf
End Function

Function FLIMIT#(XL_A#,XL_B#,XL_C#)
	;SIMPLE LIMITER
	If XL_A<XL_B
		Return XL_B
	Else
		If XL_A>XL_C
			Return XL_C
		Else
			Return XL_A
		EndIf
	EndIf
End Function

Function GUI_REFRESH_TEXT(GAD.GAD)
	If GAD\TAB=0 Or GAD\TAB=GAD\WIN\TAB
		If GAD\INP\IMG=0
			GAD\INP\IMG=CreateImage(GAD\INP\IMG_W,GAD\INP\IMG_H)
			SetBuffer ImageBuffer(GAD\INP\IMG)
			GUI_OX=0:GUI_OY=0
			GUI_VPX=0:GUI_VPY=0
			GUI_VPW=GAD\INP\IMG_W
			GUI_VPH=GAD\INP\IMG_H
			Viewport 0,0,GUI_VPW,GUI_VPH
			ClsColor 0,0,GAD\INP\CLS_COL 
			Cls
			ClsColor 0,0,0
		EndIf
		If GAD\WIN\IMG
			SetBuffer ImageBuffer(GAD\WIN\IMG)
			WIN.WIN=GAD\WIN
			GUI_GAD_BUFFER(GAD)
			If GAD\TYP=gad_INTEGER Or GAD\TYP=gad_FLOAT
				XL_PLUS=1
			EndIf
			DrawBlockRect GAD\INP\IMG,GAD\X+1+XL_PLUS,GAD\Y+1+XL_PLUS,GAD\INP\IMG_X,GAD\INP\IMG_Y,GAD\INP\VIS_W,GAD\INP\VIS_H
			If GAD\PAD[11]=True
				Color 0,0,0
				Rect GAD\X,GAD\Y,GAD\W,GAD\H+1,0
			EndIf
		EndIf
		SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
		GUI_OX=0:GUI_OY=0
		GUI_VPX=0:GUI_VPY=0
		GUI_VPW=GUI_GFXW
		GUI_VPH=GUI_GFXH
	EndIf
End Function

Function GUI_CUR_LOCATE(XL_X,XL_Y,XL_FORCELEFT=False,XL_SETEND=True)
	SetBuffer BackBuffer()
	GAD.GAD=GUI_INPUTGAD
	WIN.WIN=GAD\WIN
	INP.TXTAREA=GAD\INP
	
	INP\CUR_X=-1:INP\CUR_Y=-1
	
	XL_X=XL_X+INP\IMG_X
	XL_Y=XL_Y+INP\IMG_Y
	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			If INP\TXT$=""
				INP\CUR_X=1:INP\CUR_Y=1
				INP\MODE=inp_APPEND
				INP\INPUTPOS=0
				INP\IMG_X=0
				INP\IMG_Y=0
				GUI_CURX=1:GUI_CURY=1
			Else
				If INP\WRAP=False
					XL_BNK.TXTBNK=INP\ALPHA
					XL_Y=XL_Y+INP\START_Y
					While XL_BNK<>Null
						If RectsOverlap(0,XL_Y,INP\IMG_W,12,0,XL_BNK\Y,INP\IMG_W,12)
							INP\INPUTBNK=XL_BNK
							INP\INPUTPOS=Len(XL_BNK\TXT$)
							INP\MODE=inp_APPEND
							INP\CUR_X=1:INP\CUR_Y=1
							XL_TX=0
							For XL_T=1 To Len(XL_BNK\TXT$)
								XL_CHAR$=Mid$(XL_BNK\TXT$,XL_T,1)
								If XL_CHAR$=Chr$(9)
									If INP\PASSWORD=False
										XL_CHAR$="    "
									Else
										XL_CHAR$="****"
									EndIf
								Else
									If INP\PASSWORD=True
										XL_CHAR$="*"
									EndIf
								EndIf
								If RectsOverlap(XL_X,XL_Y,1,12,XL_TX,XL_Y,GUI_STRINGWIDTH(XL_CHAR$)+4,12)
									INP\INPUTPOS=XL_T
									If XL_T=Len(XL_BNK\TXT$)
										INP\MODE=inp_APPEND
									Else
										INP\MODE=inp_INSERT
									EndIf
									Exit
								Else
									XL_TX=XL_TX+GUI_STRINGWIDTH(XL_CHAR$)
								EndIf
							Next
							XL_BNK=Null
						Else
							If XL_BNK\NXT=Null And XL_SETEND=True
								INP\INPUTBNK=XL_BNK
								INP\INPUTPOS=Len(XL_BNK\TXT$)
								INP\CUR_X=1:INP\CUR_Y=1
								INP\MODE=inp_APPEND
								XL_BNK=Null
							Else
								XL_BNK=XL_BNK\NXT
							EndIf
						EndIf	
					Wend
				Else
					XL_BNK.TXTBNK=INP\ALPHA
					XL_Y=XL_Y+INP\START_Y
					If INP\ZETA<>Null
						If XL_SETEND=True
							INP\INPUTBNK=INP\ZETA
							INP\INPUTPOS=Len(INP\ZETA\TXT$)
							INP\MODE=inp_APPEND
						EndIf
						XL_WORD.TXTWORD=INP\WORD_ZETA
						While XL_WORD<>Null
							If RectsOverlap(XL_X,XL_Y,1,1,XL_WORD\X,XL_WORD\Y,INP\IMG_W,12)
								INP\INPUTBNK=XL_WORD\BNK
								INP\INPUTPOS=Len(XL_WORD\BNK\TXT$)
								INP\MODE=inp_APPEND
								XL_TX=XL_WORD\X
								For XL_T=1 To Len(XL_WORD\TXT$)
									XL_CHAR$=Mid$(XL_WORD\TXT$,XL_T,1)
									If XL_CHAR$=Chr$(9)
										XL_CHAR$="    "
									EndIf
									If RectsOverlap(XL_X,XL_Y,1,12,XL_TX,XL_WORD\Y,GUI_STRINGWIDTH(XL_CHAR$)+8,12)
										If XL_T+XL_WORD\POS1=Len(XL_BNK\TXT$)
											INP\MODE=inp_APPEND
										Else
											INP\MODE=inp_INSERT
										EndIf
										INP\INPUTPOS=XL_T+XL_WORD\POS1
										If XL_FORCELEFT=True
											INP\INPUTPOS=XL_T+XL_WORD\POS1-1
										EndIf
										XL_WORD=Null
										Exit
									Else
										XL_TX=XL_TX+GUI_STRINGWIDTH(XL_CHAR$)
									EndIf
								Next
							EndIf
							If XL_WORD=INP\WORD_ALPHA Or XL_WORD=Null
								XL_WORD=Null
							Else
								XL_WORD=Before XL_WORD
							EndIf
						Wend
					EndIf
				EndIf
				GUI_TEXTOUTPUT(GAD)
			EndIf
		Case gad_INTEGER,gad_FLOAT
			INP\CUR_X=1:INP\CUR_Y=1
			INP\MODE=inp_APPEND
			INP\INPUTPOS=Len(INP\ALPHA\TXT$)
			INP\INPUTBNK=INP\ALPHA
			INP\IMG_X=0
			INP\IMG_Y=0
			GUI_CURX=1:GUI_CURY=1
			GUI_TEXTOUTPUT(GAD)
	End Select
	
	Return XL_RET
End Function

Function GUI_TEXTOUTPUT(GAD.GAD)

	XL_AREA.TXTAREA=GAD\INP

	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			GUI_FONTCOL(XL_AREA\TXT_COL)
		Default
			GUI_FONTCOL(0)
	End Select
	
	If GAD\INP\IMG
		SetBuffer ImageBuffer(GAD\INP\IMG)
		ClsColor 0,0,GAD\INP\CLS_COL
		Cls
		ClsColor 0,0,0
	Else
		GUI_REFRESH_TEXT(GAD)
	EndIf
	
	If GAD\INP\IMG
		SetBuffer ImageBuffer(GAD\INP\IMG)
	Else
		GAD\INP\IMG=CreateImage(GAD\INP\IMG_W,GAD\INP\IMG_H)
		SetBuffer ImageBuffer(GAD\INP\IMG)
		ClsColor 0,0,GAD\INP\CLS_COL
		Cls
		ClsColor 0,0,0
	EndIf
	
	XL_AREA\STARTLINE=QLIMIT(XL_AREA\STARTLINE,0,XL_AREA\LINES)
	
	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			
			If XL_AREA\BLOCK=True And XL_AREA\INV_START>-1 And XL_AREA\INV_END>-1
				XL_INVA=XL_AREA\INV_START
				XL_INVB=XL_AREA\INV_END
				If XL_INVB<XL_INVA
					XL_TMP=XL_INVB
					XL_INVB=XL_INVA
					XL_INVA=XL_TMP
				EndIf
				XL_BLOCK=True
			EndIf
			
			Select GAD\INP\WRAP
				Case True
					XL_X=1:XL_Y=1
										
					XL_BNK.TXTBNK=XL_AREA\ALPHA
					
					;SORT OUT BANK Y POSITIONS
					XL_AREA\TXT$=""
					XL_AREA\TXT_Y=0
					
					For XL_WORD.TXTWORD=Each TXTWORD
						If XL_WORD\AREA=XL_AREA
							Delete XL_WORD
						EndIf
					Next
					
					XL_WORD.TXTWORD=Null
					
					While XL_BNK<>Null
						XL_BNK\ID=XL_ID:XL_ID=XL_ID+1
						XL_BNK\GAD=GAD
						XL_BNK\AREA=XL_AREA
						XL_AREA\TXT$=XL_AREA\TXT$+XL_BNK\TXT$;+Chr$(13)
						XL_AREA\ZETA=XL_BNK
						XL_T=1:XL_LINE=False
						While XL_LINE=False
							XL_TXT$=GUI_WRAPRIPWORD$(XL_BNK\TXT$,XL_T)
							XL_LEN=Len(XL_TXT$)
							If XL_ADDTAB=True
							;	XL_TXT$=Chr$(9)+XL_TXT$
								XL_ADDTAB=False
							EndIf
							If Right$(XL_TXT$,1)=Chr$(9)
								XL_ADDTAB=True
							;	XL_TXT$=Mid$(XL_TXT$,1,Len(XL_TXT$)-1)
							EndIf
							XL_WORD=New TXTWORD
							XL_WORD\AREA=XL_AREA
							XL_WORD\BNK=XL_BNK
							If XL_AREA\WORD_ALPHA=Null
								XL_AREA\WORD_ALPHA=XL_WORD
							EndIf
							XL_AREA\WORD_ZETA=XL_WORD
							XL_WORD\TAB$=XL_TXT$
							XL_WORD\TXT$=Replace(XL_TXT$,Chr$(9),"    ")
							XL_WORD\POS1=XL_T
							XL_WORD\POS2=XL_WORD\POS1+XL_LEN
							XL_T=XL_T+XL_LEN
							If XL_T>Len(XL_BNK\TXT$)
								XL_LINE=True
							EndIf
						Wend
						If XL_WORD<>Null
							XL_WORD\NEWLINE=True
						EndIf
						XL_BNK\POS_START=XL_POS+1
						XL_POS=XL_POS+Len(XL_BNK\TXT$)
						XL_BNK\POS_END=XL_POS-1
						XL_BNK=XL_BNK\NXT
					Wend
					
					XL_WORD=XL_AREA\WORD_ALPHA
					XL_X=1:XL_Y=1
					
					While XL_WORD<>Null
						XL_WORD\W=GUI_STRINGWIDTH(XL_WORD\TXT$)
						
						If XL_WORD\W>XL_AREA\IMG_W-2
							XL_LEFT$=""
							For XL_T=1 To Len(XL_WORD\TAB$)
								XL_CHAR$=Mid$(XL_WORD\TAB$,XL_T,1)
								If XL_CHAR$=Chr$(9)
									XL_DIGIT$="    "
								Else
									XL_DIGIT=XL_CHAR$
								EndIf
								XL_LEFT$=XL_LEFT$+XL_DIGIT$
								If GUI_STRINGWIDTH(XL_LEFT$)>XL_AREA\IMG_W-2
									XL_POS=XL_T
									Exit
								EndIf
							Next
							
							XL_1TAB$=Left$(XL_WORD\TAB$,XL_POS-1)
							XL_2TAB$=Mid$(XL_WORD\TAB$,XL_POS)
							
							XL_1TXT$=Replace$(XL_1TAB$,Chr$(9),"    ")
							XL_2TXT$=Replace$(XL_2TAB$,Chr$(9),"    ")
							
							XL_WORD\TXT$=XL_1TXT$
							XL_WORD\TAB$=XL_1TAB$
							XL_WORD\POS2=XL_WORD\POS1+Len(XL_WORD\TAB$)
							XL_WORD\W=GUI_STRINGWIDTH(XL_WORD\TXT$)
							
							XL_NEWWORD.TXTWORD=New TXTWORD
							Insert XL_NEWWORD After XL_WORD
							XL_NEWWORD\AREA=XL_AREA
							XL_NEWWORD\BNK=XL_WORD\BNK
							XL_NEWWORD\TXT$=XL_2TXT$
							XL_NEWWORD\TAB$=XL_2TAB$
							XL_NEWWORD\POS1=XL_WORD\POS2+1
							XL_NEWWORD\POS2=XL_NEWWORD\POS1+Len(XL_NEWWORD\TAB$)
							
							If XL_WORD\NEWLINE=True
								XL_WORD\NEWLINE=False
								XL_NEWWORD\NEWLINE=True
							EndIf
							
							If XL_AREA\WORD_ZETA=XL_WORD
								XL_AREA\WORD_ZETA=XL_NEWWORD
							EndIf
							
						EndIf
									
						If XL_X+XL_WORD\W>XL_AREA\IMG_W-2
							XL_X=1+XL_WORD\W
							XL_Y=XL_Y+12
							XL_AREA\TXT_Y=XL_AREA\TXT_Y+12
							XL_WORD\X=1
							XL_WORD\Y=XL_Y
							If XL_WORD\NEWLINE=True
								XL_X=1
								XL_Y=XL_Y+12
								XL_AREA\TXT_Y=XL_AREA\TXT_Y+12
							EndIf
						Else
							XL_WORD\X=XL_X
							XL_WORD\Y=XL_Y
							If XL_WORD\NEWLINE=True
								XL_X=1
								XL_Y=XL_Y+12
								XL_AREA\TXT_Y=XL_AREA\TXT_Y+12
							Else
								XL_X=XL_X+XL_WORD\W
							EndIf
						EndIf
						If XL_WORD\BNK=XL_AREA\INPUTBNK
							If (XL_AREA\INPUTPOS>=XL_WORD\POS1 And XL_AREA\INPUTPOS<=XL_WORD\POS2) Or (XL_WORD\POS1=1 And XL_AREA\INPUTPOS=0)
								XL_LINEPOS=XL_WORD\Y
								XL_INPUTWORD.TXTWORD=XL_WORD
							EndIf
						EndIf
						XL_MAXY=XL_Y
						XL_WORD=After XL_WORD
					Wend
					
					If XL_MAXY<>XL_AREA\TXT_H
						XL_AREA\TXT_H=XL_MAXY
						XL_REFRESH_VERTPROP=True
					EndIf
					
					If GAD\TYP=GAD_TEXTAREA
						If XL_AREA\INPUTBNK<>Null
							If XL_LINEPOS<XL_AREA\IMG_H-1
								XL_LINEPOS=0
							Else
								XL_LINEPOS=QLIMIT(XL_LINEPOS,0,XL_AREA\TXT_H-XL_AREA\IMG_H)
							EndIf
						Else
							XL_LINEPOS=GUI_GADVAL(GAD\LINK[0]\OBJ)*12
						EndIf
					Else
						XL_REFRESH_VERTPROP=False
					EndIf
					
					GUI_SETVAL(GAD\LINK[0]\OBJ,XL_LINEPOS/12)
					SetBuffer ImageBuffer(XL_AREA\IMG)
					
					XL_AREA\START_Y=XL_LINEPOS
					
					XL_WORD=XL_AREA\WORD_ALPHA
					
					While XL_WORD<>Null
						If XL_WORD\Y>=XL_LINEPOS And XL_WORD\Y<=XL_LINEPOS+XL_AREA\IMG_H
														
							XL_INV=False
														
							If XL_AREA\COLBNK=Null
								XL_X=XL_WORD\X
								For XL_T=1 To Len(XL_WORD\TAB$)
									XL_CHAR$=Mid$(XL_WORD\TAB$,XL_T,1)
									If XL_CHAR$=Chr$(9)
										If XL_AREA\PASSWORD=True
											XL_CHAR$="****"
										Else
											XL_CHAR$="    "
										EndIf
									Else
										If XL_AREA\PASSWORD=True
											XL_CHAR$="*"
										EndIf
									EndIf
									XL_INV=False
									If XL_BLOCK=True
										If XL_T+XL_WORD\POS1+XL_WORD\BNK\POS_START>=XL_INVA And XL_T+XL_WORD\POS1+XL_WORD\BNK\POS_START<=XL_INVB
											XL_INV=True
										EndIf
									EndIf
									XL_X=GUI_PRINT(XL_X,XL_WORD\Y-XL_LINEPOS,XL_CHAR$,XL_INV)
								Next
							Else
								
							EndIf
							
						EndIf
						If XL_WORD=XL_AREA\WORD_ZETA
							XL_YY=XL_WORD\Y
							XL_WORD=Null
						Else
							XL_WORD=After XL_WORD
						EndIf
					Wend
					
					If XL_INPUTWORD<>Null
						If XL_INPUTWORD\TAB$<>""
							XL_LEN=(XL_AREA\INPUTPOS-(XL_INPUTWORD\POS1-1))
							XL_LEFT$=""
							For XL_T=1 To XL_LEN
								XL_CHAR$=Mid$(XL_INPUTWORD\TAB$,XL_T,1)
								If XL_CHAR$=Chr$(9)
									XL_CHAR$="    "
								EndIf
								XL_LEFT$=XL_LEFT$+XL_CHAR$
							Next
							XL_X=XL_INPUTWORD\X+GUI_STRINGWIDTH(XL_LEFT$)
							XL_Y=XL_INPUTWORD\Y-XL_LINEPOS
						Else
							XL_X=XL_INPUTWORD\X
							XL_Y=XL_INPUTWORD\Y-XL_LINEPOS
						EndIf
						XL_AREA\CUR_X=XL_X
						XL_AREA\CUR_Y=XL_Y
					Else
						XL_AREA\CUR_X=1
						XL_AREA\CUR_Y=1
					EndIf
					
					;NEW VERTICAL SIZE SO UPDATE VERT SLIDER					
					If XL_REFRESH_VERTPROP=True
						XL_H=Int(XL_YY/12)+1
						GUI_PROP_RANGE(GAD\LINK[0]\OBJ,0,XL_H,True,12)
						GUI_SETVAL(GAD\LINK[0]\OBJ,XL_LINEPOS)
					EndIf	
				
				Case False
					XL_X=1:XL_Y=1
					XL_T=1:XL_TY=1
					
					XL_BNK.TXTBNK=XL_AREA\ALPHA
					
					;SORT OUT BANK Y POSITIONS	
					XL_AREA\TXT$=""				
					While XL_BNK<>Null
						XL_BNK\ID=XL_ID:XL_ID=XL_ID+1
						XL_BNK\GAD=GAD
						XL_BNK\AREA=XL_AREA
						XL_AREA\TXT$=XL_AREA\TXT$+XL_BNK\TXT$;+Chr$(13)
						XL_BNK\Y=XL_Y
						XL_Y=XL_Y+12
						XL_AREA\ZETA=XL_BNK
						XL_BNK=XL_BNK\NXT
					Wend
					
					If XL_Y<>XL_AREA\TXT_H
						XL_AREA\TXT_H=XL_Y
						XL_REFRESH_VERTPROP=True
					EndIf	
					
					XL_BNK=XL_AREA\ALPHA
					XL_Y=1
					
					;SORT OUT CURRENT STARTING LINE Y POSITION
					If GAD\TYP=GAD_TEXTAREA
						XL_AREA\IMG_X=GUI_GADVAL(GAD\LINK[1]\OBJ)
						If XL_AREA\INPUTBNK<>Null
							XL_LINEPOS=XL_AREA\INPUTBNK\Y
							If XL_LINEPOS<XL_AREA\IMG_H-1
								XL_LINEPOS=0
							Else
								XL_LINEPOS=QLIMIT(XL_LINEPOS,0,XL_AREA\TXT_H-XL_AREA\IMG_H)
							EndIf
						Else
							XL_LINEPOS=GUI_GADVAL(GAD\LINK[0]\OBJ)*12
						EndIf
					Else
						XL_REFRESH_VERTPROP=False
					EndIf
					
					If GAD\LINK[0]<>Null
						GUI_SETVAL(GAD\LINK[0]\OBJ,XL_LINEPOS/12)
						SetBuffer ImageBuffer(XL_AREA\IMG)
					EndIf
					
					XL_AREA\START_Y=XL_LINEPOS
														
					While XL_BNK<>Null
						
						;CHECK TO SEE IF LINE IS VISIBLE IN BOX
						If XL_Y>=XL_LINEPOS And XL_Y<=XL_LINEPOS+XL_AREA\IMG_H
							If XL_BNK\TXT$<>""
								XL_TXT$=XL_BNK\TXT$
								XL_TAB$=XL_TXT$
								
								;SORT OUT TAB
								If Instr(XL_TXT$,Chr$(9))
									XL_TXT$=Replace$(XL_TXT$,Chr$(9),"    ")	
								EndIf
																							
								;RESIZE IMAGE WIDTH IF NEEDED
								XL_STRINGWIDTH=GUI_STRINGWIDTH(XL_TXT$)
								If XL_STRINGWIDTH>XL_AREA\IMG_W
									GUI_TEXT_RESIZE(XL_AREA,XL_STRINGWIDTH+12)
									If GAD\LINK[1]<>Null
										GUI_PROP_RANGE(GAD\LINK[1]\OBJ,0,XL_AREA\IMG_W+2,True)
									EndIf
									SetBuffer ImageBuffer(XL_AREA\IMG)
								EndIf
								
								;PRINT THE LINE WITH PROPER COLOR
								
								XL_INV=False
															
								If XL_AREA\COLBNK=Null
									For XL_T=1 To Len(XL_TAB$)
										XL_CHAR$=Mid$(XL_TAB$,XL_T,1)
										If XL_CHAR$=Chr$(9)
											If XL_AREA\PASSWORD=True
												XL_CHAR$="****"
											Else
												XL_CHAR$="    "
											EndIf
										Else
											If XL_AREA\PASSWORD=True
												XL_CHAR$="*"
											EndIf
										EndIf
										XL_INV=False
										If XL_BLOCK=True
											If XL_T+XL_POS>=XL_INVA And XL_T+XL_POS<=XL_INVB
												XL_INV=True
											EndIf
										EndIf
										XL_X=GUI_PRINT(XL_X,XL_TY,XL_CHAR$,XL_INV)
									Next
									XL_X=1
								Else
									
								EndIf
							EndIf
							
							;SORT OUT CURSOR POSITION AND IMAGE POSITION IF THIS IS THE INPUT BANK
							If XL_BNK=XL_AREA\INPUTBNK
								If XL_AREA\INPUTPOS=0 Or XL_BNK\TXT$=""
									XL_AREA\CUR_X=1
									XL_AREA\CUR_Y=XL_BNK\Y-XL_LINEPOS
									XL_AREA\IMG_X=0
								Else
								
									For XL_T=1 To XL_AREA\INPUTPOS
										XL_LEFT$=Mid$(XL_TAB$,XL_T,1)
										If XL_LEFT$=Chr$(9)
											If XL_AREA\PASSWORD=True
												XL_LEFT$="****"
											Else
												XL_LEFT$="    "
											EndIf
										Else
											If XL_AREA\PASSWORD=True
												XL_LEFT$="*"
											EndIf
										EndIf
										XL_LEFTWIDTH=XL_LEFTWIDTH+GUI_STRINGWIDTH(XL_LEFT$)
									Next
									
									XL_LEFTWIDTH=XL_LEFTWIDTH+1
									
									XL_AREA\IMG_X=XL_LEFTWIDTH-(GAD\W-2)
									If XL_AREA\IMG_X<0 Then XL_AREA\IMG_X=0
																
									XL_AREA\CUR_X=XL_LEFTWIDTH-XL_AREA\IMG_X
									XL_AREA\CUR_Y=XL_BNK\Y-XL_LINEPOS
								EndIf
								If GAD\LINK[1]<>Null
									GUI_SETVAL(GAD\LINK[1]\OBJ,XL_AREA\IMG_X)
								EndIf
								SetBuffer ImageBuffer(GAD\INP\IMG)
							EndIf
							
							XL_TY=XL_TY+12
						EndIf
						
						;NEXT BANK
						XL_Y=XL_Y+12
						XL_BNK\POS_START=XL_POS+1
						XL_POS=XL_POS+Len(XL_BNK\TXT$)
						XL_BNK\POS_END=XL_POS-1
						XL_BNK=XL_BNK\NXT
					Wend
					
					;NEW VERTICAL SIZE SO UPDATE VERT SLIDER					
					If XL_REFRESH_VERTPROP=True
						XL_H=Int(XL_Y/12)
						GUI_PROP_RANGE(GAD\LINK[0]\OBJ,0,XL_H,True,12)
						GUI_SETVAL(GAD\LINK[0]\OBJ,XL_LINEPOS)
					EndIf										
															
			End Select
	
		Case gad_INTEGER
			
			XL_BNK.TXTBNK=XL_AREA\ALPHA
			XL_AREA\TXT$=XL_BNK\TXT$
			XL_TXT$=XL_BNK\TXT$
			XL_OFFSET=(XL_AREA\IMG_W/2)-(GUI_STRINGWIDTH(XL_TXT$)/2)
			GUI_PRINT(XL_X+XL_OFFSET,XL_TY,XL_TXT$,XL_INV)
			XL_LEFT$=Left$(XL_TXT$,XL_AREA\INPUTPOS)
			XL_AREA\CUR_X=GUI_STRINGWIDTH(XL_LEFT$)+1+XL_OFFSET
	
		Case gad_FLOAT
			
			XL_BNK.TXTBNK=XL_AREA\ALPHA
			XL_AREA\TXT$=XL_BNK\TXT$
			XL_TXT$=XL_BNK\TXT$
			XL_OFFSET=1;(XL_AREA\IMG_W/2)-(GUI_STRINGWIDTH(XL_TXT$)/2)
			GUI_PRINT(XL_X+XL_OFFSET,XL_TY,XL_TXT$,XL_INV)
			XL_LEFT$=Left$(XL_TXT$,XL_AREA\INPUTPOS)
			XL_AREA\CUR_X=GUI_STRINGWIDTH(XL_LEFT$)+1+XL_OFFSET		
			
	End Select
	GUI_REFRESH_TEXT(GAD)
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
	GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_VPH
End Function

Function GUI_TEXT_OCCUR(XL_TXT$,XL_STR$)
	XL_POS=1
	Repeat
		XL_T=Instr(XL_TXT$,XL_STR$,XL_POS)
		If XL_T>0
			XL_POS=XL_T+1
			XL_RET=XL_RET+1
		EndIf
	Until XL_T=0
	Return XL_RET
End Function

Function GUI_TEXT_RESIZE(XL_AREA.TXTAREA,XL_NEW_W)
	
	XL_IMG=CreateImage(XL_NEW_W,XL_AREA\IMG_H)
	SetBuffer ImageBuffer(XL_IMG)
	ClsColor 0,0,XL_AREA\CLS_COL
	Cls
	ClsColor 0,0,0
	If XL_AREA\IMG
		DrawBlock XL_AREA\IMG,0,0
	Else
		FreeImage XL_IMG
		Return
	EndIf
	FreeImage XL_AREA\IMG
	XL_AREA\IMG=XL_IMG
	XL_AREA\IMG_W=XL_NEW_W
	
End Function

Function GUI_TEXTINPUT(XL_REFRESH=True)

	GAD.GAD=GUI_INPUTGAD
	XL_AREA.TXTAREA=GAD\INP
	XL_BNK.TXTBNK=XL_AREA\INPUTBNK
		
	If XL_BNK=Null And XL_AREA\ALPHA=Null
		XL_AREA\ALPHA=New TXTBNK
		XL_BNK=XL_AREA\ALPHA
		XL_AREA\INPUTPOS=0
		XL_AREA\INPUTBNK=XL_BNK
	Else
		If XL_BNK=Null
			XL_BNK=New TXTBNK
			XL_AREA\ALPHA=XL_BNK
			XL_AREA\INPUTBNK=XL_BNK
			XL_AREA\INPUTPOS=0
		EndIf
	EndIf
	
	If XL_AREA\INPUTPOS=0
		XL_AREA\MODE=inp_INSERT
	EndIf
	
	XL_BNK=XL_AREA\INPUTBNK
		
	XL_CHAR$=Chr$(GUI_GETKEY)
	Select XL_AREA\MODE
		Case inp_APPEND
			Select GUI_GETKEY
				Case 4				;DELETE
					If XL_BNK\NXT<>Null
						XL_BNK\TXT$=XL_BNK\TXT$+XL_BNK\NXT\TXT$
						XL_OLDBNK.TXTBNK=XL_BNK\NXT
						If XL_BNK\NXT\NXT<>Null
							XL_BNK\NXT\NXT\PAR=XL_BNK
							XL_BNK\NXT=XL_BNK\NXT\NXT
						EndIf
						Delete XL_OLDBNK
						GUI_INPUTMODE=inp_INSERT
						XL_AREA\LINES=XL_AREA\LINES-1
					EndIf
				Case 8										;BACKSPACE
					If Len(XL_BNK\TXT$)>0
						XL_BNK\TXT$=Left$(XL_BNK\TXT$,Len(XL_BNK\TXT$)-1)
						XL_AREA\INPUTPOS=XL_AREA\INPUTPOS-1
					Else
						XL_BNK\TXT$=""
						XL_OLDBNK.TXTBNK=XL_BNK
						XL_AREA\LINES=XL_AREA\LINES-1
						If XL_BNK\PAR<>Null
							If XL_BNK\NXT<>Null
								XL_BNK\NXT\PAR=XL_BNK\PAR
								XL_BNK\PAR\NXT=XL_BNK\NXT
							EndIf
							XL_AREA\INPUTPOS=Len(XL_BNK\PAR\TXT$)
							XL_BNK=XL_BNK\PAR
							XL_AREA\MODE=inp_APPEND
							Delete XL_OLDBNK
						Else
							If XL_BNK\NXT<>Null
								XL_AREA\ALPHA=XL_BNK\NXT
								XL_BNK\NXT\PAR=Null
								XL_BNK=XL_BNK\NXT
								XL_AREA\MODE=inp_INSERT
								XL_AREA\INPUTPOS=0
								Delete XL_OLDBNK
							EndIf
						EndIf
						XL_AREA\STARTLINE=XL_AREA\STARTLINE-1
						XL_AREA\TXT_Y=XL_AREA\TXT_Y-12
					EndIf
				Case 13,10										;ENTER
					XL_BNK\TXT$=XL_BNK\TXT$
					XL_NEW.TXTBNK=New TXTBNK
					XL_NEW\PAR=XL_BNK
					If XL_BNK\NXT=Null
						XL_BNK\NXT=XL_NEW
					Else
						XL_NEW\NXT=XL_BNK\NXT
						XL_NEW\NXT\PAR=XL_NEW
						XL_BNK\NXT=XL_NEW
					EndIf
					XL_BNK=XL_NEW
					XL_NEW\TXT$=""
					XL_AREA\INPUTPOS=1
					XL_AREA\MODE=inp_APPEND
					XL_AREA\LINES=XL_AREA\LINES+1
					XL_AREA\STARTLINE=XL_AREA\STARTLINE+1
					XL_AREA\TXT_Y=XL_AREA\TXT_Y+12
				Default
					If GUI_GETKEY>31 Or GUI_GETKEY=9
						XL_BNK\TXT$=XL_BNK\TXT$+XL_CHAR$
						XL_AREA\INPUTPOS=XL_AREA\INPUTPOS+1
					EndIf
			End Select
		Case inp_INSERT
			Select GUI_GETKEY
				Case 4
					XL_BNK\TXT$=Left$(XL_BNK\TXT$,XL_AREA\INPUTPOS)+Mid$(XL_BNK\TXT$,XL_AREA\INPUTPOS+2)
					If XL_AREA\INPUTPOS=Len(XL_BNK\TXT$) Or XL_BNK\TXT$=""
						XL_AREA\MODE=inp_APPEND
						If XL_BNK\NXT<>Null
							XL_BNK\TXT$=XL_BNK\TXT$+XL_BNK\NXT\TXT$
							XL_OLDBNK.TXTBNK=XL_BNK\NXT
							If XL_BNK\NXT\NXT<>Null
								XL_BNK\NXT\NXT\PAR=XL_BNK
								XL_BNK\NXT=XL_BNK\NXT\NXT
							EndIf
							Delete XL_OLDBNK
							XL_AREA\INPUTBNK=XL_BNK
							XL_AREA\INPUTPOS=0
							GUI_INPUTMODE=inp_INSERT
							XL_AREA\LINES=XL_AREA\LINES-1
						EndIf
					EndIf	
				Case 8
					If XL_AREA\INPUTPOS=0
						If XL_BNK\PAR<>Null
							XL_AREA\INPUTPOS=Len(XL_BNK\PAR\TXT$)
							XL_BNK\PAR\TXT$=XL_BNK\PAR\TXT$+XL_BNK\TXT$
							If XL_BNK\NXT<>Null
								XL_BNK\NXT\PAR=XL_BNK\PAR
								XL_BNK\PAR\NXT=XL_BNK\NXT
							EndIf
							XL_OLDBNK.TXTBNK=XL_BNK
							XL_BNK=XL_BNK\PAR
							XL_AREA\LINES=XL_AREA\LINES-1
							Delete XL_OLDBNK
						EndIf
						XL_AREA\STARTLINE=XL_AREA\STARTLINE-1
						XL_AREA\TXT_Y=XL_AREA\TXT_Y-12
					Else
						XL_CHAR$=Mid$(XL_BNK\TXT$,XL_AREA\INPUTPOS,1)
						XL_BNK\TXT$=Left$(XL_BNK\TXT$,XL_AREA\INPUTPOS-1)+Mid$(XL_BNK\TXT$,XL_AREA\INPUTPOS+1)
						XL_AREA\INPUTPOS=XL_AREA\INPUTPOS-1
					EndIf
				Case 13,10
					XL_NEW.TXTBNK=New TXTBNK
					XL_NEW\TXT$=Mid$(XL_BNK\TXT$,XL_AREA\INPUTPOS+1)
					XL_NEW\PAR=XL_BNK
					If XL_BNK\NXT<>Null
						XL_NEW\NXT=XL_BNK\NXT
						XL_BNK\NXT\PAR=XL_NEW
					EndIf
					XL_BNK\NXT=XL_NEW
					XL_BNK\TXT$=Left$(XL_BNK\TXT$,XL_AREA\INPUTPOS)
					XL_BNK=XL_NEW
					If Len(XL_NEW\TXT$)>0
						XL_AREA\MODE=inp_INSERT
						XL_AREA\INPUTPOS=0
					Else
						XL_AREA\INPUTPOS=1
						XL_AREA\MODE=inp_APPEND
					EndIf
					XL_AREA\LINES=XL_AREA\LINES+1
					XL_AREA\STARTLINE=XL_AREA\STARTLINE+1
					XL_AREA\TXT_Y=XL_AREA\TXT_Y+12
				Default
					If GUI_GETKEY>31 Or GUI_GETKEY=9
						If XL_AREA\INPUTPOS=0
							XL_BNK\TXT$=XL_CHAR$+XL_BNK\TXT$
						Else
							XL_BNK\TXT$=Left$(XL_BNK\TXT$,XL_AREA\INPUTPOS)+XL_CHAR$+Mid$(XL_BNK\TXT$,XL_AREA\INPUTPOS+1)
						EndIf
						XL_AREA\INPUTPOS=XL_AREA\INPUTPOS+1
					EndIf
			End Select
		End Select
		
		XL_AREA\INPUTBNK=XL_BNK
						
		If XL_REFRESH=True
			GUI_TEXTOUTPUT(GAD)
		EndIf
			
End Function