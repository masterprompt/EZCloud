;XLNT II USER ACCESS FUNCTIONS

;CONTAINS MOST TOF THE USER ACCESS FUNCTIONS FOR XLNT II
;IE - GADGET VALUE READING, SETTING TEXT ETC....

;==========================================================================================
;GENERAL FUNCTIONS
;==========================================================================================

Function GUI_DRAWMOUSE()
	;---------------------------------------------------------------------------------------------------------------------
	;Draws the Mouse Pointer
	;---------------------------------------------------------------------------------------------------------------------
	SetBuffer BackBuffer():Origin 0,0:Viewport 0,0,GUI_GFXW,GUI_GFXH
	
	If GUI_MOUSE_DRAW=True
		If GUI_MOUSE=0
			GUI_MOUSE=GUI_MOUSE_POINTER
		EndIf
		DrawImage GUI_MOUSE,MouseX(),MouseY()
	EndIf
	
End Function

;==========================================================================================
;GENERAL GADGET FUNCTIONS
;==========================================================================================

Function GUI_FREEGAD(XL_GAD1)
	;---------------------------------------------------------------------------------------------------------------------
	;Deletes a gadget
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD1<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD1)
	For XL_T=0 To 3
		If GAD\LINK[XL_T]<>Null
			GUI_FREEGAD(GAD\LINK[XL_T]\OBJ)
		EndIf
	Next
	If GAD\IMG
		FreeImage GAD\IMG
		GAD\IMG=0
	EndIf
	
	If GAD=GAD\WIN\FIRST_GAD
		For XL_GAD.GAD=Each GAD
			If XL_GAD\WIN=GAD\WIN And XL_GAD<>GAD
				GAD\WIN\FIRST_GAD=XL_GAD
				Exit
			EndIf
		Next
	EndIf
	If GAD\TYP=gad_TAB
		For XL_GAD.GAD=Each GAD
			If XL_GAD\WIN=GAD\WIN And XL_GAD\TAB=GAD\PAD[0] And GAD<>XL_GAD
				GUI_FREEGAD(XL_GAD\OBJ)
			EndIf
		Next
	EndIf
	If GAD\TYP=gad_PANEL
		For XL_GAD.GAD=Each GAD
			If XL_GAD\WIN=GAD\WIN And XL_GAD\PANEL=GAD\PANEL And XL_GAD<>GAD
				GUI_FREEGAD(XL_GAD\OBJ)
			EndIf
		Next
	EndIf
	If GAD\IMG<>0
		FreeImage GAD\IMG
	EndIf
	If GAD\LIST<>Null
		For XL_ITM.LISTITM=Each LISTITM
			If XL_ITM\LIST=GAD\LIST
				Delete XL_ITM
			EndIf
		Next
		If GAD\LIST\IMG<>0
			FreeImage GAD\LIST\IMG
		EndIf
		Delete GAD\LIST
	EndIf
	If GAD\PANEL<>GAD\WIN\PANEL
		Delete GAD\PANEL
	EndIf
	If GUI_3DCAM=GAD\OBJ
		GUI_3DCAM=0
	EndIf
	If GAD\MENU<>Null
		GUI_FREEMENU(GAD\MENU\OBJ)
	EndIf
	
	If GAD\INP<>Null
		XL_BNK.TXTBNK=GAD\INP\ALPHA
		XL_OLDBNK.TXTBNK=XL_BNK
		While XL_BNK<>Null
			XL_OLDBNK=XL_BNK
			XL_BNK=XL_BNK\NXT
			Delete XL_OLDBNK
		Wend
		Delete GAD\INP
	EndIf
	Delete GAD
End Function

Function GUI_GADACTIVE(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns if a gadget is Active [True] or Inactive [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\ACTIVE
End Function

Function GUI_GADON(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns if a gadget is On/Down [True] or Off/Up [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\ON
End Function

Function GUI_GADSTATUS(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadets status:
	;0 = Hidden
	;1 = Normal / Displayed
	;2= Displayed but LOCKED
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\STATUS
End Function
;---------------------------------------------------------------------------------------------------------------------

Function GUI_SETACTIVE(XL_GAD,XL_ACT=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadget Active [True] or Inactive [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\ACTIVE<>XL_ACT
		GAD\ACTIVE=XL_ACT
		Select GAD\TYP
			Case gad_TEXTAREA
			Default
				For XL_T=0 To 3
					If GAD\LINK[XL_T]<>Null
						GUI_SETACTIVE(GAD\LINK[XL_T]\OBJ,XL_ACT)
					EndIf
				Next
		End Select
		GUI_REFRESH_GAD(GAD\OBJ)
	EndIf
End Function

Function GUI_SELECTOR_SET(XL_GAD,XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a SELECTOR gadgets current item
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Or XL_ITM<1 Return
	
	XL_GAD1.GAD=Object.GAD(XL_GAD)
	XL_ICON.ICON=Null
	
	For XL_ITEM.LISTITM=Each LISTITM
		If XL_ITEM\LIST=XL_GAD1\LIST
			XL_ITEM\ON=False
			XL_ITEM\SELECTED=False
			XL_ITEM\OPEN=False
			If XL_ITEM\OBJ=XL_ITM
				XL_ITEM\ON=True
				XL_TXT$=XL_ITEM\TXT$
				XL_ICON=XL_ITEM\ICON
			EndIf
		EndIf
	Next
	
	XL_GAD1\CAP$=XL_TXT$
	XL_GAD1\ICON=XL_ICON
	
	GUI_REFRESH_GAD(XL_GAD1\OBJ)
	
End Function

Function GUI_SETLIST(XL_GAD,XL_LIST2)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgetS LIST
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	XL_LIST.LIST=Object.LIST(XL_LIST2)
	Select GAD\TYP
		Case gad_TXTLIST,gad_TREELIST
			GAD\LINK[0]\FVAL=0
			GAD\LINK[0]\VAL=0
			GAD\LIST=XL_LIST
			XL_LIST\GAD=GAD
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_COMBO
			GAD\CAP$=""
			GAD\LINK[0]\LINK[0]\LINK[0]\VAL=0
			GAD\LIST=XL_LIST
			XL_LIST\GAD=GAD
			GUI_REFRESH_GAD(GAD\OBJ)
	End Select
End Function

Function GUI_SET_FOCUS(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets current focused gadget
	;---------------------------------------------------------------------------------------------------------------------

	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	If GAD\ACTIVE=False Or GAD\STATUS=0
		Return
	EndIf
	
	GUI_ACTIVEWIN=GAD\WIN
	
	GUI_WINSHOW(GUI_ACTIVEWIN\OBJ,True)
		
	If GAD\TAB<>0 And GAD\TAB<>GAD\WIN\TAB
		GUI_SETTAB(GAD\WIN\OBJ,GAD\TAB)
	EndIf
	
	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			Delete EVENT
			EVENT=New EVENT
			GUI_WINMODE=Wmode_INPUT
			GUI_INPUTGAD=GAD
			GAD\INP\CUR_X=-1:GAD\INP\CUR_Y=-1
			GAD\INP\INPUTBNK=GAD\INP\ZETA
			GAD\INP\INPUTPOS=0
			If GAD\INP\ZETA<>Null
				GAD\INP\INPUTPOS=Len(GAD\INP\ZETA\TXT$)
				GAD\INP\MODE=inp_APPEND
			EndIf
			GUI_TEXTOUTPUT(GAD)
	End Select
	
	GUI_FOCUS=GAD\OBJ
	
End Function

Function GUI_FOCUS()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns currently Focused gadget
	;---------------------------------------------------------------------------------------------------------------------
	
	Return GUI_FOCUS
	
End Function

Function GUI_SETON(XL_GAD,XL_ON)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadget On [True] or Off [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\GRP<>0
		If XL_ON=True
			GUI_GROUP_OFF(GAD\OBJ)
		EndIf
		GAD\ON=XL_ON
		If GAD\TYP=gad_TAB
			If XL_ON=True
				GAD\WIN\TAB=GAD\PAD[0]
			Else
				GAD\WIN\TAB=0
			EndIf
		EndIf
	Else
		GAD\ON=XL_ON
	EndIf
	GUI_REFRESH(GAD\WIN\OBJ)
End Function

Function GUI_GAD_STATUS(XL_GAD,XL_STATUS,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets status:
	;0 = Hidden
	;1 =Normal
	;2 = Locked
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	GAD\STATUS=XL_STATUS
	For XL_T=0 To 3
		If GAD\LINK[XL_T]<>Null
			GUI_GAD_STATUS(GAD\LINK[XL_T]\OBJ,XL_STATUS,False)
		EndIf
	Next
	If GAD\TYP=gad_COMBO
		GUI_GAD_STATUS(GAD\LINK[0]\OBJ,gad_HIDE,False)
	EndIf
	If GAD\TYP=gad_PANEL
		If GAD\ON=True
			GAD\LINK[1]\STATUS=GAD\STATUS
		Else
			GAD\LINK[1]\STATUS=gad_HIDE
		EndIf
	EndIf
	If XL_REFRESH=True
		If GAD\TAB=GAD\WIN\TAB Or GAD\TAB=0
			GUI_REFRESH(GAD\WIN\OBJ)
		EndIf
	EndIf
End Function

Function GUI_GADFLOAT#(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets FLOAT value
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\FVAL
End Function

Function GUI_GADVAL(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets INT value
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_SWITCH
			Return GAD\ON
		Case gad_CYCLE
			Return GAD\VAL
		Case gad_IMGBUT
			Return GAD\ICON\OBJ
		Case gad_IMGRAD
			Return GAD\ON
		Case gad_RADIO
			Return GAD\ON
		Case gad_TICK
			Return GAD\ON
		Case gad_PROP
			Return GAD\VAL
		Case gad_SLIDER
			Return GAD\VAL
		Case gad_COMBO
			XL_ITM=GUI_LIST_SELECTED(GAD\LIST\OBJ)
			Return GUI_LIST_CURPOS(XL_ITM)
		Case gad_INTEGER
			Return GAD\VAL
		Case gad_FLOAT
			Return Int(GAD\FVAL)
		Case gad_IMGBOX
			Return GAD\IMG
		Case gad_IMGDSP
			Return GAD\IMG
		Case gad_PANEL
			Return GAD\ON
		Case gad_TAB
			Return GAD\PAD[0]
		Case gad_3D
			Return GAD\PAD[0]
	End Select
End Function

Function GUI_GADGROUP_ON(XL_GRP)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the .gad which is currently ON in the passed group
	;---------------------------------------------------------------------------------------------------------------------
		
	For XL_GAD.GAD=Each GAD
		If XL_GAD\GRP=XL_GRP
			If XL_GAD\ON=True
				XL_RET=XL_GAD\OBJ
				Exit
			EndIf
		EndIf
	Next
	Return XL_RET
End Function

Function GUI_GADGROUP(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets group
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\GRP
End Function

Function GUI_GADTAB(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets tab
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\TAB
End Function

Function GUI_GAD_SETTAB(XL_GAD,XL_TAB,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets tab
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	GAD\TAB=XL_TAB
	GUI_SETGADTAB(XL_GAD)
	
	For XL_T=0 To 3
		If GAD\LINK[XL_T]<>Null
			GUI_GAD_SETTAB(GAD\LINK[XL_T]\OBJ,XL_TAB,False)
		EndIf
	Next
	
	If XL_REFRESH=True
		GUI_REFRESH(GAD\WIN\OBJ)
	EndIf
	
End Function

Function GUI_GADX(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets relative X position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GUI_BUFFERX(GAD)+GAD\X
End Function

Function GUI_GADY(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets relative Y position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GUI_BUFFERY(GAD)+GAD\Y
End Function

Function GUI_GAD_SCREENX(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets on-screen X position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\WIN\X+GUI_BUFFERX(GAD)+GAD\X
End Function

Function GUI_GAD_SCREENY(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets on-screen Y position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\WIN\Y+GUI_BUFFERY(GAD)+GAD\Y
End Function

Function GUI_GAD_MOUSEX(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets mouse x position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD = Object.GAD(XL_GAD)
	
	Select GAD\TYP
		Case gad_IMGBOX,gad_IMGAREA
			XL_GAD_X=GAD\WIN\X+ GUI_BUFFERX(GAD)+GAD\X+GAD\PAD[0]
			XL_GAD_Y=GAD\WIN\Y+ GUI_BUFFERY(GAD)+GAD\Y+GAD\PAD[1]
			If MouseX()>=XL_GAD_X And MouseX()<=XL_GAD_X+GAD\W-(GAD\PAD[0]*2)
				If MouseY()>=XL_GAD_Y And MouseY()<=XL_GAD_Y+GAD\H-(GAD\PAD[1]*2)
					RET=MouseX()-XL_GAD_X
				Else
					RET=-1
				EndIf
			Else
				RET=-1
			EndIf
		Default 
			RET=-1
	End Select
	
	Return RET
		
End Function

Function GUI_GAD_MOUSEY(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets mouse y position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD = Object.GAD(XL_GAD)
	
	Select GAD\TYP
		Case gad_IMGBOX,gad_IMGAREA
			XL_GAD_X=GAD\WIN\X+ GUI_BUFFERX(GAD)+GAD\X+GAD\PAD[0]
			XL_GAD_Y=GAD\WIN\Y+ GUI_BUFFERY(GAD)+GAD\Y+GAD\PAD[1]
			If MouseX()>=XL_GAD_X And MouseX()<=XL_GAD_X+GAD\W-(GAD\PAD[0]*2)
				If MouseY()>=XL_GAD_Y And MouseY()<=XL_GAD_Y+GAD\H-(GAD\PAD[1]*2)
					RET=MouseY()-XL_GAD_Y
				Else
					RET=-1
				EndIf
			Else
				RET=-1
			EndIf
		Default 
			RET=-1
	End Select
	
	Return RET
	
End Function


Function GUI_GADW(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets TOTAL width
	;---------------------------------------------------------------------------------------------------------------------

	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_TXTLIST,gad_TREELIST,gad_TEXTAREA,gad_IMGAREA
			RET=GAD\W+1+GAD\LINK[0]\W
		Default
			RET=GAD\W
	End Select
	Return RET
	
End Function

Function GUI_GADH(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets TOTAL height
	;---------------------------------------------------------------------------------------------------------------------

	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_TEXTAREA,gad_IMGAREA
			If GAD\LINK[1]<>Null
				RET=GAD\H+1+GAD\LINK[1]\H
			EndIf
		Default
			RET=GAD\H
	End Select
	Return RET
	
End Function

Function GUI_GAD_RESIZE(XL_GAD,XL_W,XL_H,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;ReSize a gadget
	;---------------------------------------------------------------------------------------------------------------------

	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	If GAD\ICON<>Null
		XL_IW=GAD\ICON\W
		XL_IH=GAD\ICON\H
	EndIf
	
	XL_TXT$=GAD\CAP$
	XL_MIN=GAD\MIN
	XL_MAX=GAD\MAX
	XL_FMIN#=GAD\FMIN#
	XL_FMAX#=GAD\FMAX#
	
	Select GAD\TYP
		Case gad_BUTTON,gad_SWITCH
			GAD\W=QLIMIT(XL_W,GUI_STRINGWIDTH(XL_TXT$)+6+XL_IW,999999)	
			GAD\H=QLIMIT(XL_IH+6,19,999999999)
			GAD\TW=GAD\W:GAD\TH=GAD\H
		Case gad_CYCLE
			XL_WIDEST=GUI_WIDEST_ITM(GAD\TXT$)
			GAD\W=QLIMIT(XL_W,XL_WIDEST+24,999999)
			GAD\H=19
			GAD\TW=GAD\W:GAD\TH=GAD\H
		Case gad_IMGBUT,gad_IMGRAD
			If GAD\PAD[0]=True
				XL_PLS=4
			EndIf
			GAD\W=QLIMIT(XL_W,XL_IW+XL_PLS,9999999)
			GAD\H=QLIMIT(XL_H,XL_IH+XL_PLS,9999999)
			GAD\TW=GAD\W:GAD\TH=GAD\H
		Case gad_PROP
			If GAD\PAD[1]=True					;HORZ
				GAD\PAD[0]=XL_W
				XL_SIZE=XL_W
				GAD\W=XL_W
				GAD\H=13
				GAD\TW=GAD\W+30:GAD\TH=GAD\H
				GAD\LINK[1]\X=GAD\X+1+XL_SIZE
			Else
				GAD\PAD[0]=XL_H
				XL_SIZE=XL_H
				GAD\H=XL_H
				GAD\W=13
				GAD\TW=GAD\W:GAD\TH=GAD\H+30
				GAD\LINK[1]\Y=GAD\Y+1+XL_SIZE
			EndIf
			GUI_PROP_RANGE(GAD\OBJ,GAD\MIN,GAD\MAX,False)
		Case gad_SLIDER
			If GAD\PAD[1]=True
				GAD\PAD[0]=XL_W
				GAD\W=XL_W
				GAD\H=16
			Else
				GAD\PAD[0]=XL_H
				GAD\W=16
				GAD\H=XL_H
			EndIf
			GUI_PROP_RANGE(GAD\OBJ,GAD\MIN,GAD\MAX,False)
			GAD\TW=GAD\W:GAD\TH=GAD\H
		Case gad_INTEGER
			If GUI_STRINGWIDTH(Str$(XL_MIN))>GUI_STRINGWIDTH(Str$(XL_MAX))
				GAD\W=GUI_STRINGWIDTH(Str$(XL_MIN))+10
			Else
				GAD\W=GUI_STRINGWIDTH(Str$(XL_MAX))+10
			EndIf
			If XL_W>GAD\W
				GAD\W=XL_W
			EndIf
			GAD\H=16
		
			GAD\LINK[0]\X=GAD\X+GAD\W-1
			GAD\LINK[1]\X=GAD\X+GAD\W-1
			GAD\TW=GAD\W-1+GAD\LINK[0]\W:GAD\TH=GAD\H
		Case gad_FLOAT
			If GUI_STRINGWIDTH(GUI_FLOAT_STR$(XL_FMIN,GAD\PAD[0]))>GUI_STRINGWIDTH(GUI_FLOAT_STR(XL_FMAX,GAD\PAD[0]))
				GAD\W=GUI_STRINGWIDTH(GUI_FLOAT_STR$(XL_FMIN,GAD\PAD[0]))+10
			Else
				GAD\W=GUI_STRINGWIDTH(GUI_FLOAT_STR$(XL_FMAX,GAD\PAD[0]))+10
			EndIf
			If XL_W>GAD\W
				GAD\W=XL_W
			EndIf
			GAD\H=16
		
			GAD\LINK[0]\X=GAD\X+GAD\W-1
			GAD\LINK[1]\X=GAD\X+GAD\W-1
			GAD\TW=GAD\W-1+GAD\LINK[0]\W:GAD\TH=GAD\H
		Case gad_TXTLIST,gad_TREELIST
			XL_W=QLIMIT(XL_W,60,9999)
			XL_H=QLIMIT(XL_H,34,9999)
			XL_H=Int(XL_H/17)*17
			XL_H=XL_H+4
			GAD\W=XL_W
			GAD\H=xl_W-4:GAD\H=xl_H
			GAD\PAD[0]=(XL_H-4)/17
			xl_XSIZE=GAD\W
			xl_YSIZE=QLIMIT((XL_H-4)/17,0,99999)
		;	GAD\LINK[0]\X=GAD\X+GAD\W+1
		;	GAD\LINK[0]\Y=XL_H-(14*2)
			GUI_SETXY(GAD\LINK[0]\OBJ,GAD\X+GAD\W+1,GAD\LINK[0]\Y,False)
			GUI_GAD_RESIZE(GAD\LINK[0]\OBJ,0,XL_H-(14*2),False)
			GAD\TW=GAD\W+1+GAD\LINK[0]\W:GAD\TH=GAD\H
		Case gad_COMBO
			XL_W=QLIMIT(XL_W,GAD\PAD[7],99999)
			XL_H=17
			GAD\W=XL_W
			GAD\H=XL_H
			GAD\LINK[0]\W=GAD\W
			GAD\LINK[0]\H=24+(GAD\PAD[3]*17)
			GUI_GAD_RESIZE(GAD\LINK[0]\LINK[0]\OBJ,GAD\LINK[0]\W+6,GAD\LINK[0]\H-20,False)
			GAD\TW=GAD\W:GAD\TH=GAD\H	
		Case gad_IMGBOX
			GAD\W=XL_W
			GAD\H=XL_H
			GAD\LINK[0]\W=XL_W
			GAD\LINK[0]\H=XL_H
			If GAD\IMG
				GUI_BOXIMAGE(GAD,GAD\IMG)
			EndIf
		Case gad_FRAME
			GAD\W=XL_W
			GAD\H=XL_H
			GAD\TW=GAD_W:GAD\TH=GAD\H
		Case gad_PANEL
			XL_W=QLIMIT(XL_W,GUI_STRINGWIDTH(GAD\CAP$)+12,99999999)
			XL_H=QLIMIT(XL_H,19,9999999)
			GUI_GAD_RESIZE(GAD\LINK[0]\OBJ,XL_W,XL_H,False)
			GUI_GAD_RESIZE(GAD\LINK[1]\OBJ,GAD\LINK[0]\W+1,XL_H,False)
			GAD\PANEL\W=XL_W
			GAD\PANEL\H=GAD\LINK[1]\H
			GAD\TW=GAD\W:GAD\TH=GAD\H
		Case gad_TOOLBAR
			If GAD\PAD[0]=True
				GAD\H=QLIMIT(XL_H,20,9999999)
				GAD\W=XL_W-24
				GAD\LINK[0]\H=GAD\H
				GAD\LINK[1]\H=GAD\H
				GAD\LINK[0]\X=GAD\X+GAD\W
				GAD\LINK[1]\X=GAD\X+GAD\W+12
			Else
				GAD\W=QLIMIT(XL_W,20,9999999)
				GAD\H=XL_H-24
				GAD\LINK[0]\W=GAD\W
				GAD\LINK[1]\W=GAD\W
				GAD\LINK[0]\Y=GAD\Y+GAD\H
				GAD\LINK[1]\Y=GAD\Y+GAD\H+12
			EndIf
			GAD\TW=GAD\W:GAD\TH=GAD\H			
			GAD\PANEL\W=GAD\W
			GAD\PANEL\H=GAD\H
		Case gad_TAB
			GAD\W=QLIMIT(XL_W,GUI_STRINGWIDTH(GAD\CAP$)+6+XL_IW,999999)
			GAD\H=QLIMIT(XL_IH+6,19,999999999)
			GAD\TW=GAD\W:GAD\TH=GAD\H
	Case gad_IMGBUT,gad_IMGRAD 
		If GAD\PAD[0]=True
			XL_PLS=4
		EndIf 
		GAD\W=QLIMIT(XL_W,XL_IW+XL_PLS,9999999)
		GAD\H=QLIMIT(XL_H,XL_IH+XL_PLS,9999999)
		GAD\TW=GAD\W:GAD\TH=GAD\H 
		Case gad_LABEL
			GAD\W=QLIMIT(XL_W,1,99999999)
			GAD\H=QLIMIT(XL_H,1,99999999)
		Case gad_TEXTAREA
			XL_W=QLIMIT(XL_W,4,9999999)
			GAD\W=XL_W
			XL_H=Int(XL_H/14)*14
			If XL_H<1 Then XL_H=14
			GAD\H=XL_H
		
			GUI_GAD_RESIZE(GAD\LINK[0]\OBJ,0,XL_H-(14*2),False)
			GAD\LINK[2]\W=GAD\W
			GAD\LINK[2]\H=GAD\H
			;GAD\LINK[0]\X=GAD\X+GAD\W+1
			GUI_SETXY(GAD\LINK[0]\OBJ,GAD\X+GAD\W+1,GAD\LINK[0]\Y,False)
			If GAD\LINK[1]<>Null
				GUI_GAD_RESIZE(GAD\LINK[1]\OBJ,XL_W-(14*2),0,False)
				;GAD\LINK[1]\Y=GAD\Y+GAD\H+1
				GUI_SETXY(GAD\LINK[1]\OBJ,GAD\LINK[1]\X,GAD\Y+GAD\H+1,False)
			EndIf
			GAD\TW=GAD\W
			GAD\TH=GAD\H
			If GAD\IMG
				FreeImage GAD\IMG
				GAD\IMG=CreateImage(GAD\TW,GAD\TH)
			EndIf
			GUI_SETTEXT(GAD\OBJ,GAD\CAP$)
		Case gad_TEXTINP
			GAD\W=QLIMIT(XL_W,14,9999999)
		
			GAD\TW=GAD\W
			GAD\TH=GAD\H
			If GAD\IMG
				FreeImage GAD\IMG
			EndIf
			GUI_SETTEXT(GAD\OBJ,GAD\CAP$)
		Case gad_IMGAREA
			XL_W=QLIMIT(XL_W,16,999999)
			XL_H=QLIMIT(XL_H,16,999999)
			GAD\W=XL_W
			GAD\H=XL_H
			GUI_GAD_RESIZE(GAD\LINK[0]\OBJ,0,XL_H-(14*2),False)
			GUI_GAD_RESIZE(GAD\LINK[1]\OBJ,XL_W-(14*2),0,False)
			GAD\LINK[2]\W=GAD\W
			GAD\LINK[2]\H=GAD\H
			;GAD\LINK[0]\X=GAD\X+GAD\W+1
			;GAD\LINK[1]\Y=GAD\Y+GAD\H+1
			GUI_SETXY(GAD\LINK[0]\OBJ,GAD\X+GAD\W+1,GAD\LINK[0]\Y,False)
			GUI_SETXY(GAD\LINK[1]\OBJ,GAD\LINK[1]\X,GAD\Y+GAD\H+1,False)
			GAD\TW=GAD\W
			GAD\TH=GAD\H
	End Select
	
	If XL_REFRESH=True
		GUI_REFRESH(GAD\WIN\OBJ)
	EndIf
	
End Function

Function GUI_TOOLTIP(XL_GAD,XL_HELP$)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets ToolTip [Help] Text
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	GAD\HELP$=XL_HELP$
End Function

Function GUI_SETXY(XL_GAD,XL_X,XL_Y,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets relative position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	XL_XO=XL_X-GAD\X
	XL_YO=XL_Y-GAD\Y
	
	For XL_T=0 To 3
		If GAD\LINK[XL_T]<>Null
			GUI_SETXY(GAD\LINK[XL_T]\OBJ,XL_X+(GAD\LINK[XL_T]\X-GAD\X),XL_Y+(GAD\LINK[XL_T]\Y-GAD\Y),False)		
		EndIf
	Next
	GAD\X=XL_X
	GAD\Y=XL_Y
	
	If XL_REFRESH=True
		GUI_REFRESH(GAD\WIN\OBJ)	
	EndIf
End Function

Function GUI_SETFLOAT(XL_GAD,XL_VAL#)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets FLOAT value
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\TYP=gad_FLOAT
		GAD\FVAL#=FLIMIT(XL_VAL#,GAD\FMIN,GAD\FMAX)
		If (GAD\TAB=GAD\WIN\TAB Or GAD\TAB=0) And (GAD\STATUS=gad_SHOW Or GAD\STATUS=gad_LOCK)
			GUI_REFRESH_GAD(GAD\OBJ)
		EndIf
	EndIf
End Function

Function GUI_SETVAL(XL_GAD,XL_VAL#)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets INT value
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_PROP
			GAD\VAL=QLIMIT(XL_VAL,GAD\MIN,GAD\MAX)
			GAD\FVAL=FLIMIT(XL_VAL,GAD\FMIN,GAD\FMAX)
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_SLIDER
			GAD\VAL=QLIMIT(XL_VAL,GAD\MIN,GAD\MAX)
			GAD\FVAL=FLIMIT(XL_VAL,GAD\FMIN,GAD\FMAX)
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_FLOAT
			GAD\FVAL=QLIMIT(XL_VAL,GAD\FMIN,GAD\FMAX)
			If (GAD\TAB=GAD\WIN\TAB Or GAD\TAB=0) And (GAD\STATUS=gad_SHOW Or GAD\STATUS=gad_LOCK)
				GUI_REFRESH_GAD(GAD\OBJ)
			EndIf
		Case gad_INTEGER
			GAD\VAL=QLIMIT(XL_VAL,GAD\MIN,GAD\MAX)
			If (GAD\TAB=GAD\WIN\TAB Or GAD\TAB=0) And (GAD\STATUS=gad_SHOW Or GAD\STATUS=gad_LOCK)
				GUI_REFRESH_GAD(GAD\OBJ)
			EndIf
		Case gad_IMGBOX
			If XL_VAL>0
				If GAD\IMG>0
					FreeImage GAD\IMG
				EndIf
				GAD\IMG=XL_VAL
				If ImageWidth(XL_VAL)>GAD\W Or ImageHeight(XL_VAL)>GAD\H
					GAD\PAD[2]=True
				EndIf
				GUI_BOXIMAGE(GAD,GAD\IMG)
			Else
				If GAD\IMG
					FreeImage GAD\IMG
				EndIf
				GAD\IMG=0;CreateImage(GAD\W,GAD\H)
				GUI_BOXIMAGE(GAD,GAD\IMG)
			EndIf
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_IMGAREA
			If XL_VAL>0
				If GAD\IMG>0
					FreeImage GAD\IMG
				EndIf
				GAD\IMG=XL_VAL
			Else
				If GAD\IMG
					FreeImage GAD\IMG
				EndIf
				GAD\IMG=CreateImage(GAD\W,GAD\H)
			EndIf
			GUI_PROP_RANGE(GAD\LINK[1]\OBJ,0,ImageWidth(GAD\IMG),True)
			GUI_PROP_RANGE(GAD\LINK[0]\OBJ,0,ImageHeight(GAD\IMG),True)
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_COMBO
			For XL_ITM.LISTITM=Each LISTITM
				If XL_ITM\LIST=GAD\LIST
					If XL_NUM=XL_VAL
						GUI_LIST_TOGGLE(XL_ITM\OBJ,True)
						Exit
					Else
						XL_NUM=XL_NUM+1
					EndIf
				EndIf
			Next
		Default
			GAD\VAL=QLIMIT(XL_VAL,GAD\MIN,GAD\MAX)
			GAD\ON=XL_VAL
			GUI_REFRESH_GAD(GAD\OBJ)
	End Select
End Function

Function GUI_GAD_SCROLL(XL_GAD,XL_XP=0,XL_YP=0)
	;---------------------------------------------------------------------------------------------------------------------
	;Scrolls a gadgets text positon
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
		
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_TEXTAREA
			If GAD\INP\WRAP=False
				If GAD\INP\IMG_W>GAD\W And XL_XP<>0
					If XL_XP=1
						GUI_PROCESS_INPUT(203,True,False)
					Else
						GUI_PROCESS_INPUT(205,True,False)
					EndIf
				EndIf
			EndIf
			If GAD\INP\TXT_H>GAD\H And XL_YP<>0
				If XL_YP=-1
					If GAD\LINK[0]\VAL+GAD\INP\CUR_Y>1
						GUI_PROCESS_INPUT(200)
					EndIf
				Else
					GUI_PROCESS_INPUT(208)
				EndIf
			EndIf
		Case gad_TEXTINP
			If GAD\INP\IMG_W>GAD\W And XL_XP<0
				GAD\INP\IMG_X=QLIMIT(GAD\INP\IMG_X+XL_XP,0,GAD\INP\IMG_W-(GAD\W-2))
				GUI_TEXTOUTPUT(GAD)
				GUI_REFRESH_GAD(GAD\OBJ)
			EndIf
	End Select
	
	SetBuffer BackBuffer()
	
End Function

Function GUI_GAD_CLEARTEXT(XL_GAD,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Clear a gadgets text
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	GAD\INP\ALPHA=Null
	GAD\INP\ZETA=Null
	
	XL_AREA.TXTAREA=GAD\INP
	
	For XL_BNK.TXTBNK=Each TXTBNK
		If XL_BNK\AREA=XL_AREA
			Delete XL_BNK
		EndIf
	Next
	
	For XL_WORD.TXTWORD=Each TXTWORD
		If XL_WORD\AREA=XL_AREA
			Delete XL_WORD
		EndIf
	Next
	
	If XL_REFRESH=True
		GUI_REFRESH_GAD(XL_GAD)
	EndIf
	
End Function

Function GUI_GADTEXT$(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a gadgets current text
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	XL_RET$=""
	
	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			Return GAD\INP\TXT$
		Default
			Return GAD\CAP$
	End Select
End Function

Function GUI_SETTEXT(XL_GAD,XL_TXT$)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a gadgets TEXT
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	Select GAD\TYP
		Case gad_BUTTON,gad_SWITCH
			GAD\CAP$=XL_TXT$
			If GAD\ICON<>Null
				XL_IW=GAD\ICON\W
				If XL_TXT$<>""
					XL_IW=XL_IW+4
				EndIf
			EndIf
			GAD\W=QLIMIT(GAD\W,GUI_STRINGWIDTH(XL_TXT$)+6+XL_IW,999999)
			GUI_REFRESH(GAD\WIN\OBJ)
		Case gad_CYCLE
			If Right$(XL_TXT$,1)<>"|"
				XL_TXT$=XL_TXT$+"|"
			EndIf
			GAD\TXT$=XL_TXT$
			GAD\CAP$=GUI_PARSE$(XL_TXT$)
			GAD\MAX=GUI_TXTITEMS(XL_TXT$)
			XL_WIDEST=GUI_WIDEST_ITM(XL_TXT$)
			GAD\W=QLIMIT(GAD\W,XL_WIDEST+24,999999)
			GUI_REFRESH(GAD\WIN\OBJ)
		Case gad_TICK,gad_RADIO
			GAD\W=GUI_STRINGWIDTH(XL_TXT$)+18
			GAD\CAP$=XL_TXT$
			GUI_REFRESH(GAD\WIN\OBJ)
		Case gad_FRAME
			GAD\CAP$=XL_TXT$
			GUI_REFRESH(GAD\WIN\OBJ)
		Case gad_INTEGER,gad_FLOAT
			GAD\TXT$=XL_TXT$
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_LABEL
			GAD\CAP$=XL_TXT$
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_TEXTINP
			GUI_GAD_CLEARTEXT(XL_GAD)
			If XL_TXT$<>""
				GAD\INP\ALPHA=New TXTBNK
				GAD\INP\ZETA=GAD\INP\ALPHA
				GAD\INP\ALPHA\TXT$=XL_TXT$
				GAD\INP\INPUTPOS=0
				GAD\INP\INPUTBNK=GAD\INP\ALPHA
			EndIf
			XL_TXT$=Replace$(XL_TXT$,Chr$(13)," ")
			GAD\CAP$=XL_TXT$
			GUI_TEXTOUTPUT(GAD)
			GUI_REFRESH_GAD(GAD\OBJ)
		Case gad_TEXTAREA
			XL_START=1
			If XL_TXT$<>""
				GUI_GAD_CLEARTEXT(XL_GAD,False)
				While XL_DONE=False
					XL_END=Instr(XL_TXT$,Chr$(13),XL_START)
					If XL_END=0
						XL_END=Instr(XL_TXT$,Chr$(10),XL_START)
					EndIf
					If XL_END=0
						XL_LINE$=Mid$(XL_TXT$,XL_START)
						XL_DONE=True
					Else
						XL_LINE$=Mid$(XL_TXT$,XL_START,XL_END-XL_START)
					EndIf
					GUI_GAD_ADDLINE(GAD\OBJ,XL_LINE$,False)				
					XL_START=XL_END+1
					If XL_START>=Len(XL_TXT$)
						XL_DONE=True
					EndIf
					If XL_DONE=False
						
					EndIf
				Wend
				GUI_TEXTOUTPUT(GAD)
				GUI_REFRESH_GAD(GAD\OBJ)
			Else
				GUI_GAD_CLEARTEXT(XL_GAD)
			EndIf
		
		Default
			GAD\CAP$=XL_TXT$
			GUI_REFRESH_GAD(GAD\OBJ)
	End Select
End Function



Function GUI_GADLIST(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a GADGETS list
	;---------------------------------------------------------------------------------------------------------------------
	


	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	If GAD\LIST<>Null
		Return GAD\LIST\OBJ
	EndIf
	
	Return 
	
End Function

Function GUI_GAD_ADDLINE(XL_GAD,XL_TXT$,XL_REFRESH=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Adds a new line of text to a Text Input Gadget
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	Select GAD\TYP
		Case gad_TEXTINP
			GUI_SETTEXT(XL_GAD,GUI_GADTEXT$(XL_GAD)+XL_TXT$)
		Case gad_TEXTAREA
		
			XL_NEW.TXTBNK=New TXTBNK
			XL_NEW\TXT$=XL_TXT$
			GAD\INP\LINES=GAD\INP\LINES+1
			GAD\INP\STARTLINE=GAD\INP\STARTLINE+1
			GAD\INP\TXT_Y=GAD\INP\TXT_Y+12
			GAD\INP\INPUTBNK=XL_NEW
			GAD\INP\INPUTPOS=Len(XL_TXT$)
			GAD\INP\MODE=inp_APPEND
	
			If GAD\INP\ZETA<>Null
				XL_NEW\PAR=GAD\INP\ZETA
				GAD\INP\ZETA\NXT=XL_NEW
				If Right$(GAD\INP\ZETA\TXT$,1)<>Chr$(13)
					GAD\INP\ZETA\TXT$=GAD\INP\ZETA\TXT$+Chr$(13)
				EndIf
				GAD\INP\ZETA=XL_NEW
			Else
				GAD\INP\ALPHA=XL_NEW
				GAD\INP\ZETA=XL_NEW
			EndIf
			If XL_REFRESH=True
				GUI_REFRESH_GAD(XL_GAD)
			EndIf
	End Select
	
End Function




Function GUI_ACTGROUP(XL_GRP,XL_GAD1,XL_GAD2=0,XL_GAD3=0,XL_GAD4=0,XL_GAD5=0)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a GADGETS Active Group
	;---------------------------------------------------------------------------------------------------------------------
	
	XL_FOUND=False
	For XL_GROUP.ACTGROUP=Each ACTGROUP
		If XL_GROUP\GROUP=XL_GRP
			XL_FOUND=True
			Exit
		EndIf
	Next
	
	If XL_FOUND=False
		XL_GROUP.ACTGROUP=New ACTGROUP
		XL_GROUP\GROUP=XL_GRP
	EndIf
		
	If XL_GAD1>1
		GUI_GAD_ACT_GROUP(XL_GAD1,XL_GROUP)
	EndIf
	If XL_GAD2>1
		GUI_GAD_ACT_GROUP(XL_GAD2,XL_GROUP)
	EndIf
	If XL_GAD3>1
		GUI_GAD_ACT_GROUP(XL_GAD3,XL_GROUP)
	EndIf
	If XL_GAD4>1
		GUI_GAD_ACT_GROUP(XL_GAD4,XL_GROUP)
	EndIf
	If XL_GAD5>1
		GUI_GAD_ACT_GROUP(XL_GAD5,XL_GROUP)
	EndIf
	
End Function

Function GUI_GAD_ACT_GROUP(XL_GAD,XL_GRP.ACTGROUP)
	If XL_GAD>1
		GAD.GAD=Object.GAD(XL_GAD)
		GAD\ACT_GRP=XL_GRP\GROUP
		XL_ACT.ACTGAD=New ACTGAD
		XL_ACT\GAD=GAD
		XL_ACT\GRP=XL_GRP
		For XL_T=0 To 3
			If GAD\LINK[XL_T]<>Null
				GUI_GAD_ACT_GROUP(GAD\LINK[XL_T]\OBJ,XL_GRP)
			EndIf
		Next
	EndIf
End Function

Function GUI_SET_ACTGROUP(XL_GRP,XL_ACT=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an Active Group gadgets On or Off
	;---------------------------------------------------------------------------------------------------------------------
		
	For XL_GAD.ACTGAD=Each ACTGAD
		If XL_GAD\GRP\GROUP=XL_GRP
			If XL_GAD\GAD\PARENT=Null
				GUI_SETACTIVE(XL_GAD\GAD\OBJ,XL_ACT)
			EndIf
		EndIf
	Next
	
End Function

Function GUI_RANGE(XL_GAD,XL_VAL#,XL_MIN#,XL_MAX#)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an INT or Float gadgets range
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	If GAD\TYP=gad_INTEGER Or GAD\TYP=gad_FLOAT
		GAD\MIN=XL_MIN
		GAD\MAX=XL_MAX
		GAD\FMIN=XL_MIN
		GAD\FMAX=XL_MAX
		GAD\VAL=QLIMIT(XL_VAL,XL_MIN,XL_MAX)
		GAD\FVAL=FLIMIT#(XL_VAL,XL_MIN,XL_MAX)
		GUI_REFRESH_GAD(XL_GAD)
	EndIf
	
End Function

Function GUI_PROP_RANGE(xl_GAD,XL_MIN,XL_MAX,XL_REFRESH=True,XL_SIZE=1)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a PROP or SLIDER gadgets MIN and MAX values
	
	;PAD[0]=SIZE OF POT
	;PAD[1]=HORZ / VERT
	;PAD[2]=VALUE RANGE
	;PAD[3]=SIZE OF BAR
	;PAD[4]=??
	;PAD[5]=SIZE OF EACH PIXEL (17 FOR TEXT BOXES ETC)
	;PAD[6]=NUMBER OF ELEMENTS VISIBLE
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return

	GAD.GAD=Object.GAD(XL_GAD)
	
	GAD\PAD[2]=XL_MAX-XL_MIN
	GAD\PAD[5]=XL_SIZE
	
	XL_TOTAL=GAD\PAD[2]
	XL_VIS=Int((GAD\PAD[0]+32)/XL_SIZE)
	
	GAD\FMIN=XL_MIN
	GAD\FMAX=FLIMIT(XL_MAX-XL_VIS,0,9999999)
	GAD\FVAL=FLIMIT(GAD\FVAL,XL_MIN,GAD\FMAX)
	GAD\VAL=QLIMIT(GAD\VAL,GAD\MIN,GAD\MAX)
	GAD\MIN=Int(XL_MIN)
	GAD\MAX=QLIMIT(Int(XL_MAX)-XL_VIS,0,9999999)
	
	GAD\PAD[6]=XL_VIS
			
	XL_TMP#=GAD\PAD[0]						;
	XL_TMP#=XL_TMP#/GAD\PAD[2]			;SIZE / RANGE
	GAD\FPAD[0]=XL_TMP
	
	XL_PROP=GAD\PAD[6]*GAD\FPAD[0]
	If GAD\PAD[2]<=GAD\PAD[6]
		GAD\PAD[3]=GAD\PAD[0]
	Else
		GAD\PAD[3]=QLIMIT(XL_PROP,10,GAD\PAD[0])
	EndIf
	If GAD\PAD[3]<1 Then GAD\PAD[3]=1	
	If GAD\IMG>0
		FreeImage GAD\IMG
	EndIf	
	
	XL_S=13
	If GAD\TYP=gad_PROP		
		If GAD\PAD[1]=True
			GAD\IMG=CreateImage(GAD\PAD[3],XL_S)
			SetBuffer ImageBuffer(GAD\IMG)
			GUI_GFXBOX(0,0,GAD\PAD[3],XL_S,GAD\COL[0],GAD\COL[1],GAD\COL[2],True,False,True,GAD\COL[3],True,0)
			SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
		Else
			GAD\IMG=CreateImage(XL_S,GAD\PAD[3])
			SetBuffer ImageBuffer(GAD\IMG)
			GUI_GFXBOX(0,0,XL_S,GAD\PAD[3],GAD\COL[0],GAD\COL[1],GAD\COL[2],True,False,True,GAD\COL[3],True,0)
			SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
		EndIf
	Else
		XL_S=3
		GAD\PAD[3]=XL_S
		If GAD\PAD[1]=True
			GAD\IMG=CreateImage(GAD\PAD[0],16)
			SetBuffer ImageBuffer(GAD\IMG)
			GUI_GFXBOX(0,0,GAD\PAD[0],16,GAD\COL[3],GAD\COL[3],GAD\COL[3],False,True,True,GAD\COL[0],True,0,True)
			
			SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
		Else
			GAD\IMG=CreateImage(16,GAD\PAD[0])
			SetBuffer ImageBuffer(GAD\IMG)
			GUI_GFXBOX(0,0,16,GAD\PAD[0],GAD\COL[3],GAD\COL[3],GAD\COL[3],False,True,True,GAD\COL[0],True,0)
			SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
		EndIf	
	EndIf
	If XL_REFRESH=True
		GUI_REFRESH_GAD(GAD\OBJ)
	EndIf
End Function

;==========================================================================================
;GENERAL WINDOW FUNCTIONS
;==========================================================================================

Function GUI_FREEWIN(XL_WIN)

	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	For GAD.GAD=Each GAD
		If GAD\WIN=WIN
			GUI_FREEGAD(GAD\OBJ)
		EndIf
	Next
	If WIN\IMG>0
		FreeImage WIN\IMG
	EndIf
	If WIN\IMG2>0
		FreeImage WIN\IMG2
	EndIf
	If WIN\IMG_SCALE>0
		FreeImage WIN\IMG_SCALE
	EndIf
	GUI_WINBACK(WIN)
	If GUI_ACTIVEWIN=WIN
		GUI_WINMODE=Wmode_NONE
		GUI_ACTIVEWIN=Null
	EndIf
	Delete WIN
End Function

Function GUI_OPENWIN(XL_WIN,XL_STATUS=win_OPEN)
	;---------------------------------------------------------------------------------------------------------------------
	;Initialises a window ready for further GUI access
	;Only call it ONCE when you have finished adding all a windows gadgets
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	Insert WIN After Last WIN
	WIN\STATUS=XL_STATUS
	WIN\OLD_STATUS=XL_STATUS
	GUI_REFRESH(WIN\OBJ)
	If XL_STATUS=win_OPEN
		GUI_ACTIVEWIN=WIN
		GUI_WINFRONT(WIN)
	EndIf
End Function

Function GUI_WINSTATUS(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a windows status:
	;0 = doesn't exist - use GUI_OPENWIN() first
	;1 = Open
	;2 = Minimised
	;3 = Hidden
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	Return WIN\STATUS
End Function

Function GUI_WINMIN(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Minimse an OPEN window
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	GUI_WINMODE=Wmode_NONE
	If WIN\STATUS=win_OPEN
		WIN\PAD[0]=WIN\W
		WIN\PAD[1]=WIN\H
		;WIN\W=140
		WIN\H=22
		WIN\PANEL\W=140
		WIN\PANEL\H=22
		WIN\STATUS=win_MIN
		GUI_REFRESH(WIN\OBJ)
	EndIf
End Function

Function GUI_WINMAX(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Maximise a minimised window
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	GUI_WINMODE=Wmode_NONE
	If WIN\STATUS=win_MIN
		WIN\W=WIN\PAD[0]
		WIN\H=WIN\PAD[1]
		WIN\PANEL\W=WIN\PAD[0]
		WIN\PANEL\H=WIN\PAD[1]
		WIN\STATUS=win_OPEN
		GUI_REFRESH(WIN\OBJ)
		WIN\PAD[0]=WIN\W
		WIN\PAD[1]=WIN\H
	EndIf
End Function

Function GUI_WINHIDE(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Hide an OPEN window
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	If WIN\STATUS<>win_HIDE
		GUI_WINMODE=Wmode_NONE
		WIN\MODAL=-WIN\MODAL
		WIN\OLD_STATUS=WIN\STATUS
		WIN\STATUS=win_HIDE
	EndIf
	
End Function

Function GUI_WINSHOW(XL_WIN,XL_MAX=False)
	;---------------------------------------------------------------------------------------------------------------------
	;Shows a HIDDEN window.
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return

	WIN.WIN=Object.WIN(XL_WIN)
	If WIN\STATUS=win_HIDE
		GUI_WINMODE=Wmode_NONE
		WIN\STATUS=WIN\OLD_STATUS
		WIN\MODAL=Abs(WIN\MODAL)
		GUI_WINFRONT(WIN)
	EndIf
	If XL_MAX=True
		GUI_WINMAX(XL_WIN)
	EndIf
	
End Function

Function GUI_WINTAB(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a windows current OPEN tab
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	WIN.WIN=Object.WIN(XL_WIN)
	Return WIN\TAB
End Function

Function GUI_SETTAB(XL_WIN,XL_TAB)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a windows current OPEN tab
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	For XL_GAD.GAD=Each GAD
		If XL_GAD\WIN=WIN And XL_GAD\STATUS=1
			If XL_GAD\TYP=gad_TAB And XL_GAD\PAD[0]=XL_TAB
				GUI_GROUP_OFF(XL_GAD\OBJ)
				XL_GAD\ON=True
			EndIf
		EndIf
	Next
	WIN\TAB=XL_TAB
	GUI_REFRESH(WIN\OBJ)
End Function

Function GUI_WINX(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a windows X position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	Return WIN\X
End Function

Function GUI_WINY(XL_WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a windows Y position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	Return WIN\Y
End Function

Function GUI_WIN_XY(XL_WIN,XL_X,XL_Y)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a windows position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	WIN\X=XL_X:WIN\Y=XL_Y
End Function

Function GUI_WIN_TITLE(XL_WIN,XL_TITLE$)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a windows title
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_WIN<1 Return
	
	WIN.WIN=Object.WIN(XL_WIN)
	WIN\TITLE$=XL_TITLE$
	GUI_REFRESH(WIN\OBJ)
End Function

;==========================================================================================
;MENU FUNCTIONS
;==========================================================================================

Function GUI_MENU_ACT(XL_MENU,XL_ACT)
	;---------------------------------------------------------------------------------------------------------------------
	;Activates [True] or deactivates [False] a whole menu
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENU<1 Return
	
	MENU.MENU=Object.MENU(XL_MENU)
	MENU\ACT=XL_ACT
	GUI_MENU_REFRESH(MENU\OBJ)
End Function

Function GUI_MENU_ITMACT(XL_MENUITM,XL_ACT)
	;---------------------------------------------------------------------------------------------------------------------
	;Activates [True] or deactives [False] a menu item
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENUITM<1 Return
	
	MENUITM.MENUITEM=Object.MENUITEM(XL_MENUITM)
	MENUITM\ACT=XL_ACT
	GUI_MENU_REFRESH(MENUITM\MENU\OBJ)
End Function

Function GUI_MENU_GETON(XL_MENUITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns if a Menu Tick or Radio item is On [True] or Off [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENUITM<1 Return
	
	MENUITM.MENUITEM=Object.MENUITEM(XL_MENUITM)
	If MENUITM\TYP=1 Or MENUITM\TYP=2
		Return MENUITM\ON
	EndIf
	Return False
End Function

Function GUI_MENU_SETON(XL_MENUITM,XL_ON)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a Menu Tick or Radio item On [True] or Off [False]
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENUITM<1 Return
	
	MENUITM.MENUITEM=Object.MENUITEM(XL_MENUITM)
	Select MENUITM\TYP
		Case 1
			MENUITM\ON=XL_ON
			GUI_MENU_REFRESH(MENUITM\MENU\OBJ)
		Case 2
			If XL_ON=True
				GUI_MENU_GROUP(MENUITM\MENU,MENUITM\GRP)
				MENUITM\ON=True
				GUI_MENU_REFRESH(MENUITM\MENU\OBJ)
			EndIf
	End Select
	
	Return False
End Function

Function GUI_FREEMENU(XL_MENU)
	;---------------------------------------------------------------------------------------------------------------------
	;Deletes a menu and all its items and sub menus
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENU<1 Return
	
	MENU.MENU=Object.MENU(XL_MENU)
	If MENU<>Null
		For XL_ITM.MENUITEM=Each MENUITEM
			If XL_ITM\MENU=MENU
				If XL_ITM\TYP=5
					GUI_FREEMENU(XL_ITM\NXT_MNU\OBJ)
				EndIf
				Delete XL_ITM
			EndIf
		Next
		If MENU\IMG>0
			FreeImage MENU\IMG
		EndIf
		Delete MENU
		GUI_WINMODE=Wmode_NONE
	EndIf
End Function

Function GUI_ITEM_GETVAL(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a LIST ITEMS value data
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	XL_LISTITM.LISTITM=Object.LISTITM(XL_ITM)
	Return XL_LISTITM\VAL
	
End Function

Function GUI_ITEM_GETTEXT$(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a LIST ITEMS text data
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	XL_LISTITM.LISTITM=Object.LISTITM(XL_ITM)
	Return XL_LISTITM\DATUM$
	
End Function

Function GUI_ITEM_SETVAL(XL_ITM,XL_VAL)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a LIST ITEMs value data
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	XL_LISTITM.LISTITM=Object.LISTITM(XL_ITM)
	XL_LISTITM\VAL=XL_VAL	
End Function

Function GUI_ITEM_SETTEXT(XL_ITM,XL_TXT$)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a LIST ITEMs text data
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	XL_LISTITM.LISTITM=Object.LISTITM(XL_ITM)
	XL_LISTITM\DATUM$=XL_TXT$
End Function

;==========================================================================================
;TEXT BLOCK FUNCTIONS
;==========================================================================================

Function GUI_BLOCK_ACTIVE(XL_GAD,XL_ACT)
	;---------------------------------------------------------------------------------------------------------------------
	;Sets a text input gadget's Copy & Paste abilities
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	Select GAD\TYP
		Case gad_TEXTAREA,gad_TEXTINP
			GAD\INP\EDIT=XL_ACT
	End Select
End Function

Function GUI_BLOCK_COPY$()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns String$ containing current selected Text block
	;---------------------------------------------------------------------------------------------------------------------

	XL_RET$=""
	
	If GUI_BLOCKGAD=Null
		Return ""
	Else
		XL_AREA.TXTAREA=GUI_BLOCKGAD\INP
		If XL_AREA\BLOCK=False Or XL_AREA\EDIT=False Or XL_AREA\INV_START=-1
			Return ""
		EndIf
	EndIf
		
	If XL_AREA\INV_START<XL_AREA\INV_END
		XL_START=QLIMIT(XL_AREA\INV_START-1,0,XL_AREA\INV_END)
		XL_END=XL_AREA\INV_END
		XL_POS=XL_START
	Else
		XL_START=QLIMIT(XL_AREA\INV_END,0,XL_AREA\INV_START)
		XL_END=XL_AREA\INV_START
		XL_POS=XL_START
	EndIf
	
	XL_TEXT$=GUI_GADTEXT$(GUI_BLOCKGAD\OBJ)
	
	If XL_START<1 Then XL_START=1
	
	XL_RET$=Mid$(XL_TEXT$,XL_START,XL_END-XL_START)
		
	Return XL_RET$
	
End Function

Function GUI_BLOCK_DELETE()
	;---------------------------------------------------------------------------------------------------------------------
	;Deletes current selected text block from gadget
	;---------------------------------------------------------------------------------------------------------------------
	
	If GUI_BLOCKGAD=Null
		Return 
	Else
		XL_AREA.TXTAREA=GUI_BLOCKGAD\INP
		If XL_AREA\BLOCK=False Or XL_AREA\EDIT=False Or XL_AREA\INV_START=-1
			Return 
		EndIf
	EndIf
	
	If XL_AREA\INV_START<XL_AREA\INV_END
		XL_START=QLIMIT(XL_AREA\INV_START-1,0,XL_AREA\INV_END)
		XL_END=XL_AREA\INV_END+1
		XL_POS=XL_START
	Else
		XL_START=QLIMIT(XL_AREA\INV_END-1,0,XL_AREA\INV_START)
		XL_END=XL_AREA\INV_START+1
		XL_POS=XL_START
	EndIf
	
	XL_TEXT$=GUI_GADTEXT$(GUI_BLOCKGAD\OBJ)
	
	If XL_START>0
		XL_LEFT$=Left$(XL_TEXT$,XL_START)
	Else
		Return
	EndIf
	
	If XL_END>0
		XL_RIGHT$=Mid$(XL_TEXT$,XL_END)
	Else
		Return
	EndIf
	
	XL_TEXT$=""
	XL_TEXT$=XL_LEFT$+XL_RIGHT$
	
	GUI_SETTEXT(GUI_BLOCKGAD\OBJ,XL_TEXT$)
		
	XL_AREA\BLOCK=False:XL_AREA\INV_START=-1:XL_AREA\INV_END=-1
		
	For XL_BNK.TXTBNK=Each TXTBNK
		If XL_BNK\GAD=GUI_BLOCKGAD
			If XL_BNK\POS_START<=XL_POS And XL_BNK\POS_END>=XL_POS
				XL_AREA\INPUTBNK=XL_BNK
				XL_AREA\INPUTPOS=XL_POS-XL_BNK\POS_START+1
				XL_AREA\MODE=inp_APPEND
				If XL_AREA\INPUTPOS<Len(XL_BNK\TXT$)
					XL_AREA\MODE=inp_INSERT
				EndIf
				GUI_TEXTOUTPUT(GUI_BLOCKGAD)
				Exit
			EndIf
		EndIf
	Next
	
	GUI_BLOCKGAD=Null
	GUI_WINMODE=Wmode_INPUT	
	
	Return XL_POS		
End Function

Function GUI_BLOCK_INSERT(XL_GAD,XL_BLOCK$,XL_POS=-1)
	
	;---------------------------------------------------------------------------------------------------------------------
	;Inserts block of text at specified position
	;
	;POS = -1 	: INSERT AT CURRENT CURSOR POSITION - GADGET MUST BE CURRENT INPUT GADGET
	;POS = 0	: INSERT AT START OF TEXT
	;POS = >0	: INSERT AT ANY OTHER POSITION
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return 
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	XL_AREA.TXTAREA=GAD\INP
	
	If XL_POS=-1
		If GAD=GUI_INPUTGAD And XL_AREA\INPUTBNK<>Null
			XL_POS=XL_AREA\INPUTBNK\POS_START+XL_AREA\INPUTPOS
		Else
			XL_POS=0	
		EndIf
	EndIf
	
	XL_TXT$=GUI_GADTEXT$(XL_GAD)
	
	If XL_POS>=Len(XL_TXT$)
		XL_TXT$=XL_TXT$+XL_BLOCK$
	Else
		If XL_POS>1
			XL_LEFT$=Left$(XL_TXT$,XL_POS-1)
		EndIf
		XL_RIGHT$=Mid$(XL_TXT$,XL_POS)
		XL_TXT$=XL_LEFT$+XL_BLOCK$+XL_RIGHT$
	EndIf
	
	XL_POS=XL_POS+Len(XL_BLOCK$)
	GUI_SETTEXT(XL_GAD,XL_TXT$)
		
	For XL_BNK.TXTBNK=Each TXTBNK
		If XL_BNK\GAD=GAD
			If XL_BNK\POS_START<=XL_POS And XL_BNK\POS_END>=XL_POS
				XL_AREA\INPUTBNK=XL_BNK
				XL_AREA\INPUTPOS=XL_POS-XL_BNK\POS_START
				XL_AREA\MODE=inp_APPEND
				If XL_AREA\INPUTPOS<Len(XL_BNK\TXT$)
					XL_AREA\MODE=inp_INSERT
				EndIf
				Exit
			EndIf
		EndIf
	Next
		
	GUI_TEXTOUTPUT(GAD)
	
End Function

Function GUI_BLOCK_REPLACE(XL_BLOCK$)
	;---------------------------------------------------------------------------------------------------------------------
	;Replaces the currently selected block of text with the passed string
	;---------------------------------------------------------------------------------------------------------------------
	
	If GUI_BLOCKGAD=Null
		Return 
	Else
		XL_AREA.TXTAREA=GUI_BLOCKGAD\INP
		If XL_AREA\BLOCK=False Or XL_AREA\EDIT=False Or XL_AREA\INV_START=-1
			Return 
		EndIf
	EndIf
	
	GAD.GAD=GUI_BLOCKGAD
	XL_POS=GUI_BLOCK_DELETE()
	
	GUI_BLOCKGAD=GAD
	GUI_BLOCK_INSERT(GUI_BLOCKGAD\OBJ,XL_BLOCK,XL_POS+1)
	
	GAD\INP\BLOCK=False:GAD\INP\INV_START=-1:GAD\INP\INV_END=-1
	GUI_BLOCKGAD=Null
				
End Function

Function GUI_BLOCK_CURSORPOS(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns cursor position in passed gadget
	;---------------------------------------------------------------------------------------------------------------------
	If XL_GAD<1 Return
	
	GAD.GAD=Object.GAD(XL_GAD)
	
	If GAD=GUI_INPUTGAD And GAD\INP\INPUTBNK<>Null
		Return GAD\INP\INPUTBNK\POS_START+GAD\INP\INPUTPOS
	Else
		Return 	
	EndIf
	
End Function

Function GUI_INPUT_GAD()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns current input gad ID or 0
	;---------------------------------------------------------------------------------------------------------------------
	
	If GUI_INPUTGAD<>Null
		Return GUI_INPUTGAD\OBJ
	Else
		Return
	EndIf
End Function

Function GUI_BLOCK_GAD()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns current BLOCK Text gad or 0 if no block selected
	;---------------------------------------------------------------------------------------------------------------------
	
	If GUI_BLOCKGAD<>Null
		If GUI_BLOCKGAD\INP\BLOCK=True
			Return GUI_BLOCKGAD\OBJ
		Else
			Return
		EndIf
	Else
		Return
	EndIf
End Function

Function GUI_SKIN(XL_BASE,XL_FLAG=False)

	If XL_BASE>0
		XL_SKIN.SKIN=New SKIN
		XL_SKIN\OBJ=Handle(XL_SKIN)
		XL_SKIN\IMG[0]=XL_BASE
		XL_SKIN\FLAG=XL_FLAG
		XL_RET=XL_SKIN\OBJ		
	EndIf
	
	Return XL_RET
		
End Function

Function GUI_SKIN_WINDOW(XL_WIN_ID,XL_SKIN_ID,XL_TAB=0)
	
	If XL_WIN_ID<1 Return
	
	XL_WIN.WIN=Object.WIN(XL_WIN_ID)
	
	If XL_SKIN_ID>0
		XL_WIN\SKIN[XL_TAB]=Object.SKIN(XL_SKIN_ID)
	Else
		XL_WIN\SKIN[XL_TAB]=Null
	EndIf
	
	For XL_MENU.MENU=Each MENU
		If XL_MENU\WIN=XL_WIN
			GUI_MENU_REFRESH(XL_MENU\OBJ)
		EndIf
	Next		
	
	GUI_REFRESH(XL_WIN_ID)
	
End Function

;==========================================================================================
;EVENT FUNCTIONS
;==========================================================================================

Function EV_HIT()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns ID of any gadget that has JUST been pressed down
	;---------------------------------------------------------------------------------------------------------------------
		
	If EVENT\GAD>0 Or EVENT\GAD_ACT>0
		If EVENT\GAD_LMBHIT
			If EVENT\GAD>0
				Return EVENT\GAD
			Else	
				Return EVENT\GAD_ACT
			EndIf
		EndIf
	EndIf
	Return False
End Function

Function EV_HOLD()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns ID of any gadget that has is beening held down
	;---------------------------------------------------------------------------------------------------------------------
		
	If EVENT\GAD>0 Or EVENT\GAD_ACT>0
		If EVENT\GAD_LMBHIT Or EVENT\GAD_LMBHOLD
			If EVENT\GAD>0
				Return EVENT\GAD
			Else	
				Return EVENT\GAD_ACT
			EndIf
		EndIf
	EndIf
	Return False
End Function

Function EV_RELEASE()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns ID of any gadget that has been released
	;---------------------------------------------------------------------------------------------------------------------
		
	If EVENT\GAD>0 Or EVENT\GAD_ACT>0
		If EVENT\GAD_LMBRELEASE=True
			If EVENT\GAD>0
				Return EVENT\GAD
			Else	
				Return EVENT\GAD_ACT
			EndIf
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_HIT(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns true if the GADGET has JUST been pressed down
	;---------------------------------------------------------------------------------------------------------------------
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_LMBHIT
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_DOWN(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadget is being held down
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_LMBHIT=True Or EVENT\GAD_LMBHOLD=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_RELEASE(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadget has been released
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_LMBRELEASE=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_RMBRELEASE(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadget has been released using the RMB
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_RMBRELEASE=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_TOGGLE(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadget has switched from ON/OFF or OFF/ON
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_NEWSTATE=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_NEWTEXT(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadgets TEXT has been changed
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_NEWTEXT=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_NEWVAL(GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed gadgets VALUE has been changed
	;---------------------------------------------------------------------------------------------------------------------
	
	If GAD<1 Return
	
	If EVENT\GAD=GAD Or EVENT\GAD_ACT=GAD
		If EVENT\GAD_NEWVAL=True
			Return True
		EndIf
	EndIf
	Return False
End Function

Function EV_GAD_INPUTGAD()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns ID of current INPUT FOCUSED gadget
	;---------------------------------------------------------------------------------------------------------------------
	
	Return EVENT\INPUTGAD
	
End Function

Function EV_GROUP_TOGGLE(XL_GRP)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns any gadget in the passed GROUP that has been hit
	;---------------------------------------------------------------------------------------------------------------------
		
	If EVENT\GROUP=XL_GRP And EVENT\GROUP_CHANGE=True
		Return EVENT\GAD
	EndIf
	Return False
End Function

Function EV_GROUP_DOWN(XL_GRP)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the gadget in the passed GROUP that is DOWN
	;---------------------------------------------------------------------------------------------------------------------
		
	If EVENT\GROUP=XL_GRP And EVENT\GROUP_CHANGE=True
		Return EVENT\GAD
	EndIf
	Return False
End Function

Function EV_WIN_CLOSE(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has had its CLOSE button pressed
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_KILL=True
		Return True
	EndIf
	Return False
End Function

Function EV_WIN()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the currently active WINDOW
	;---------------------------------------------------------------------------------------------------------------------

	Return EVENT\WIN

End Function

Function EV_WIN_OVER()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns which WINDOW the mouse is currently over
	;---------------------------------------------------------------------------------------------------------------------
	
	Return EVENT\WIN_OVER

End Function

Function EV_WIN_MIN(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has been MINIMIZED
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_MIN=True
		Return True
	EndIf
	Return False
End Function

Function EV_WIN_MAX(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has been MAXIMIZED
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_MAX=True
		Return True
	EndIf
	Return False
End Function

Function EV_WIN_DRAG(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has been MOVED
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_DRAG=True
		Return True
	EndIf
	Return False
End Function

Function EV_WIN_SCALE(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has been RESIZED
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_SCALE=True
		Return True
	EndIf
	Return False
End Function

Function EV_WIN_FRONT(WIN)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed window has been made ACTIVE
	;---------------------------------------------------------------------------------------------------------------------
	
	If WIN<1 Return
	
	If EVENT\WIN=WIN And EVENT\WIN_CHANGE=True
		Return True
	EndIf
	Return False
End Function

Function EV_ITEM_SELECT(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed LIST ITEM has been selected
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\LISTITEM=XL_ITM And EVENT\ITM_SELECT=True
		Return True
	EndIf
	Return False
End Function

Function EV_ITEM_OVER(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the mouse is OVER the passed LIST ITEM 
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\LISTITEM=XL_ITM
		Return True
	EndIf
	Return False
End Function

Function EV_ITEM_DBLCLICK(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed LIST ITEM has been DOUBLE CLICKED with the LMB
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\LISTITEM=XL_ITM And EVENT\ITM_DBLCLICK=True
		Return True
	EndIf
	Return False
End Function

Function EV_ITEM_RMBCLICK(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed LIST ITEM has been CLICKED with the RMB
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\LISTITEM=XL_ITM And EVENT\ITM_RMBHIT=True
		Return True
	EndIf
	Return False
End Function

Function EV_SELECTOR(XL_GAD)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns any selected item in a pull down selector
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_GAD<1 Return
	
	If EVENT\GAD=XL_GAD And EVENT\SELECTOR_HIT=True
		Return EVENT\LISTITEM
	EndIf
	Return False
End Function

Function EV_LIST_OVER(XL_LIST)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ITEM  the mouse is OVER in the passed LIST
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_LIST<1 Return
	
	If EVENT\LISTITEM<>0 And EVENT\LIST=XL_LIST
		Return EVENT\LISTITEM
	EndIf
	Return False
End Function


Function EV_LIST_SELECT(XL_LIST)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ID of any clicked LIST ITEM
	;---------------------------------------------------------------------------------------------------------------------
	
	If EVENT\LIST=XL_LIST And (EVENT\ITM_SELECT=True Or EVENT\ITM_DBLCLICK=True)
		Return EVENT\LISTITEM
	EndIf
End Function

Function EV_LIST_DBLCLICK(XL_LIST)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ID of any DOUBLE clicked LIST ITEM
	;---------------------------------------------------------------------------------------------------------------------
	
	If EVENT\ITM_DBLCLICK=True And EVENT\LIST=XL_LIST
		Return EVENT\LISTITEM
	EndIf
End Function

Function EV_LIST_RMBSELECT(XL_LIST)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ID of any RMB clicked LIST ITEM
	;---------------------------------------------------------------------------------------------------------------------
	
	If EVENT\ITM_RMBHIT=True And EVENT\LIST=XL_LIST
		Return EVENT\LISTITEM
	EndIf
End Function

Function EV_MENU_SELECT(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed MENU ITEM has been CLICKED
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\MENU_ITEM=XL_ITM And EVENT\MENU_OPEN=True
		Return True
	EndIf
	Return False
End Function

Function EV_MENU_HIT()
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ID of any CLICKED MENU ITEM
	;---------------------------------------------------------------------------------------------------------------------
	
	If EVENT\MENU_OPEN=True
		Return EVENT\MENU_ITEM
	EndIf
	Return False
End Function

Function EV_MENU_TOGGLE(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns TRUE if the passed MENU ITEM has switched from ON/OFF or OFF/ON
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	If EVENT\MENU_ITEM=XL_ITM And EVENT\MENU_OPEN=True And EVENT\MENU_TOGGLE=True
		Return True
	EndIf
	Return False
End Function

Function EV_MENU_GROUP(XL_MENU,XL_GRP)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns  the ID of any ITEM in the passed MENU GROUP that has been hit
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_MENU<1 Return
	
	If EVENT\MENU=XL_MENU And EVENT\MENU_GRP_TOGGLE=True And EVENT\MENU_GRP=XL_GRP
		Return EVENT\MENU_ITEM
	EndIf
	Return False
End Function