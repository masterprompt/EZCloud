Function GUI_LIST()
	RET.LIST=New LIST
	RET\ID=GUI_ID()
	RET\OBJ=Handle(RET)
	RET\LISTGRP=Object.LISTGROUP(GUI_LIST_NEWGROUP(RET\OBJ,1))
	Return RET\OBJ
End Function

Function GUI_LIST_REFRESH(XL_LIST)

	XL_ON=GUI_LIST_TXTON(XL_LIST)
	If XL_ON>0
		XL_ITM.LISTITM=Object.LISTITM(XL_ON)
		XL_TXT$=XL_ITM\TXT$
		XL_ICON.ICON=XL_ITM\ICON
	EndIf

	LIST.LIST=Object.LIST(XL_LIST)
	For XL_GAD.GAD=Each GAD
		If XL_GAD\LIST=LIST
			
			XL_GAD\CAP$=XL_TXT$
			If XL_GAD\TYP=gad_COMBO
				XL_GAD\ICON=XL_ICON
			EndIf
			GUI_REFRESH_GAD(XL_GAD\OBJ)
			For XL_T=0 To 3
				If XL_GAD\LINK[XL_T]<>Null
					GUI_REFRESH_GAD(XL_GAD\LINK[XL_T]\OBJ)
				EndIf
			Next
			Exit
		EndIf
	Next
End Function

Function GUI_LIST_ADDITM(XL_LIST,xl_TXT$,XL_PAR=0,xl_ICON$="",xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=0
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDINT(XL_LIST,XL_VALUE,XL_MIN,XL_MAX,xl_TXT$="",XL_PAR=0,xl_ICON$="",xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=6
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\INTV=XL_VALUE
	RET\INTMIN=XL_MIN
	RET\INTMAX=XL_MAX
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDINPUT(XL_LIST,xl_TXT$,XL_PAR=0,xl_ICON$="",xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	If XL_LIST<1 Then Return
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=5
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDFLOAT(XL_LIST,XL_VALUE#,XL_MIN#,XL_MAX#,xl_TXT$="",XL_PAR=0,xl_ICON$="",XL_STEP#=.01,XL_DEC=2,xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=7
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\FLTV=XL_VALUE
	RET\FLTMIN=XL_MIN
	RET\FLTMAX=XL_MAX
	RET\FPAD[0]=XL_STEP
	RET\PAD[0]=XL_DEC
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDTICK(XL_LIST,xl_TXT$,XL_PAR=0,xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=icn_TICKBOX
	RET\X=0
	RET\TYP=2
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDICON(XL_LIST,xl_TXT$,XL_PAR=0,xl_ICON$,xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=8
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDCHECK(XL_LIST,xl_TXT$,XL_PAR=0,XL_GRP=0,xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\X=0
	RET\TYP=3
	RET\ID=LIST\CNT
	RET\ACTIVE=False
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_ADDLINE(XL_LIST,XL_PAR=0,XL_REFRESH=True,XL_TCOL=$C8C8C8,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=icn_CHKBOX
	RET\X=0
	RET\TYP=4
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\GRP=XL_GRP
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function


Function GUI_LIST_ADDNODE(XL_LIST,xl_TXT$,XL_PAR=0,xl_ICON$="",xl_ACT=True,XL_VAL=0,XL_TEXT$="",XL_REFRESH=True,XL_TCOL=0,XL_BCOL=$FFFFFF)
	RET.LISTITM=New LISTITM
	LIST.LIST=Object.LIST(XL_LIST)
	XL_PARDIR.LISTITM=Object.LISTITM(XL_PAR)
	RET\OBJ=Handle(RET)
	RET\TXT$=XL_TXT$
	RET\ICON=GUI_FINDICON(XL_ICON$)
	RET\X=0
	RET\TYP=1
	RET\ID=LIST\CNT
	RET\ACTIVE=XL_ACT
	LIST\CNT=LIST\CNT+1
	LIST\TREE=True
	RET\Y=RET\ID*17
	RET\LIST=LIST
	RET\VIS=True
	RET\VAL=XL_VAL:RET\DATUM$=XL_TEXT$
	RET\PARENT=XL_PARDIR;GUI_LIST_PARENT(LIST,XL_PARDIR)
	RET\TCOL=XL_TCOL:RET\BCOL=XL_BCOL
	RET\ITEMGRP=LIST\LISTGRP
	If XL_REFRESH=True
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
	Return RET\OBJ
End Function

Function GUI_LIST_COUNTSELECTED(XL_LIST)

	If XL_LIST<1 Return
	
	LST.LIST=Object.LIST(XL_LIST)
	
	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=LST
			If XL_ITM\SELECTED=True
				RET=RET+1
			EndIf
		EndIf
	Next
	
	Return RET
	
End Function

Function GUI_LIST_PARENT.LISTITM(LIST.LIST,XL_DIR)
	If XL_DIR=-1
		Return Null
	Else
		For XL_ITM.LISTITM=Each LISTITM
			If XL_ITM\LIST=LIST
				If XL_ITM\ID=XL_DIR
					Return XL_ITM
				EndIf
			EndIf
		Next
	EndIf
	Return Null
End Function

Function GUI_LIST_PROCESSNODE(GAD.GAD,PAR.LISTITM,XL_CNT=0,XL_X=0,XL_Y=0)
	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=GAD\LIST And XL_ITM\VIS=True And XL_ITM\PARENT=PAR
			XL_DSP.DSPLST=New DSPLST
			XL_DSP\ITM=XL_ITM
			XL_DSP\ITM\XP=XL_X
			XL_DSP\ITM\YP=XL_CNT
			Select XL_ITM\TYP
				Case 1
					XL_CNT=XL_CNT+1
					If XL_ITM\OPEN=True
						XL_CNT=GUI_LIST_PROCESSNODE(GAD,XL_ITM,XL_CNT,XL_X+1,XL_Y)
					EndIf
				Default
					XL_CNT=XL_CNT+1
			End Select
		EndIf
	Next
	Return XL_CNT
End Function

Function GUI_LIST_DRAW(LIST.LIST,GAD.GAD,XL_NODRAW=False)
	If LIST\IMG>0
		FreeImage LIST\IMG
	EndIf
	LIST\IMG=CreateImage(GAD\W-4,GAD\H-4)
	LIST\LISTGRP\MAX=GAD\PAD[2]
	SetBuffer ImageBuffer(LIST\IMG)
	Origin 0,0:Viewport 0,0,ImageWidth(LIST\IMG),ImageHeight(LIST\IMG)
	GUI_OX=0:GUI_OY=0
	GUI_VPX=0:GUI_VPY=0:GUI_VPW=ImageWidth(LIST\IMG):GUI_VPH=ImageHeight(LIST\IMG)
	ClsColor 255,255,255
	Cls
	ClsColor 0,0,0
	Color 0,0,0
	XL_YPOS=-100000000
	For XL_ITM2.LISTITM=Each LISTITM
		If XL_ITM2\LIST=LIST
			XL_ITM2\X=-100000:XL_ITM2\Y=XL_YPOS
			XL_YPOS=XL_YPOS+1
			Select XL_ITM2\TYP
				Case 5
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_INPUT(GAD\WIN\OBJ,0,0,GAD\W,XL_ITM2\DATUM$,False,GAD\TAB,XL_ITM2\ACTIVE,"",XL_ITM2\TCOL,GUI_RGB_SHADE(XL_ITM2\BCOL,0))
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
						XL_ITM2\SUBGAD\PAD[11]=True
					EndIf
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\STATUS=0
					EndIf
				Case 6
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_INTEGER(GAD\WIN\OBJ,0,0,XL_ITM2\INTV,XL_ITM2\INTMIN,XL_ITM2\INTMAX,XL_ITM2\TXT$,1,0,GAD\TAB,XL_ITM2\ACTIVE,"")
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
					EndIf
					
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\LINK[0]\X=-10000000
						XL_ITM2\SUBGAD\LINK[1]\X=-10000000
						XL_ITM2\SUBGAD\STATUS=0
						XL_ITM2\SUBGAD\LINK[0]\STATUS=0
						XL_ITM2\SUBGAD\LINK[1]\STATUS=0
					EndIf
				Case 7
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_FLOAT(GAD\WIN\OBJ,0,0,XL_ITM2\FLTV,XL_ITM2\FLTMIN,XL_ITM2\FLTMAX,XL_ITM2\TXT$,XL_ITM2\FPAD[0],0,XL_ITM2\PAD[0],GAD\TAB,XL_ITM2\ACTIVE,"")
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
					EndIf
					
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\LINK[0]\X=-10000000
						XL_ITM2\SUBGAD\LINK[1]\X=-10000000
						XL_ITM2\SUBGAD\STATUS=0
						XL_ITM2\SUBGAD\LINK[0]\STATUS=0
						XL_ITM2\SUBGAD\LINK[1]\STATUS=0
					EndIf

			End Select
		EndIf
	Next
	Delete Each DSPLST
	XL_VIS_ITMS=GUI_LIST_PROCESSNODE(GAD,Null)
	XL_ITM.DSPLST=First DSPLST
	XL_CNT=0
	If XL_ITM<>Null
		Repeat
			If XL_ITM\ITM\LIST=LIST
				If XL_CNT=GAD\PAD[1]
					XL_DONE=True
					Exit
				Else
					XL_CNT=XL_CNT+1
				EndIf
			EndIf
			If XL_DONE=False
				XL_ITM=After XL_ITM
			EndIf
			If XL_ITM=Null
				XL_DONE=True
			EndIf
		Until XL_DONE=True
		XL_DONE=False
		XL_X=1:XL_Y=1:XL_H=17
		XL_CNT=0
		
		While XL_DONE=False And XL_ITM<>Null
			If XL_ITM\ITM\LIST=LIST And XL_ITM\ITM\VIS=True
				If XL_CNT=0
					XL_INITIAL=XL_ITM\ITM\YP
				EndIf
				XL_TX=XL_X+(XL_ITM\ITM\XP*16)
				XL_TW=GAD\W-(4+XL_TX)
				Select XL_ITM\ITM\TYP
					Case 5
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
						If XL_ITM\ITM\ACTIVE=True
							GUI_FONTCOL(XL_ITM\ITM\TCOL)
						Else
							GUI_FONTCOL($E0E0E0)
						EndIf
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+18,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
							XL_IW=21
						Else
							GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW)
							XL_IW=2
						EndIf
						
						XL_GX=(XL_TX+XL_IW+GUI_STRINGWIDTH(XL_ITM\ITM\TXT)+1)
						XL_GY=XL_Y+1
						XL_GW=(GAD\W-(XL_GX+2))
						XL_GH=16
						
						XL_ITM\ITM\SUBGAD\PAD[9]=True
						XL_ITM\ITM\SUBGAD\INP\IMG_W=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VIS_W=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VPORTW=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VIS_H=XL_GH-2
						XL_ITM\ITM\SUBGAD\INP\TXT_COL=XL_ITM\ITM\TCOL
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\W=XL_GW
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W,XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
							Color 0,0,0
							Rect XL_GX-2,XL_GY-2,XL_GAD\W,XL_GAD\H+1,0
						EndIf
												
					Case 6
						
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
												
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							XL_GX=XL_TX+16+3
						Else
							XL_GX=XL_TX+2
						EndIf
						
						XL_GY=XL_Y+2
						
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[0]\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[1]\STATUS=GAD\STATUS
						
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[0]\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[1]\ACTIVE=XL_ITM\ITM\ACTIVE

						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[0]\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[1]\PANEL=GAD\PANEL
						
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\LINK[0]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[0]\Y=XL_ITM\ITM\SUBGAD\Y
						
						XL_ITM\ITM\SUBGAD\LINK[1]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[1]\Y=XL_ITM\ITM\SUBGAD\Y+7
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[0]\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[1]\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W+12+2+GUI_STRINGWIDTH(XL_GAD\TXT$),XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
						EndIf
					
					Case 7
						
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
												
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							XL_GX=XL_TX+16+3
						Else
							XL_GX=XL_TX+2
						EndIf
						
						XL_GY=XL_Y+2
						
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[0]\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[1]\STATUS=GAD\STATUS
						
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[0]\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[1]\ACTIVE=XL_ITM\ITM\ACTIVE

						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[0]\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[1]\PANEL=GAD\PANEL
						
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\LINK[0]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[0]\Y=XL_ITM\ITM\SUBGAD\Y
						
						XL_ITM\ITM\SUBGAD\LINK[1]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[1]\Y=XL_ITM\ITM\SUBGAD\Y+7
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[0]\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[1]\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W+12+2+GUI_STRINGWIDTH(XL_GAD\TXT$),XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
						EndIf
						
					Default
						If XL_ITM\ITM\TYP=2
							XL_ITM\ITM\SELECTED=False
						EndIf
						If XL_ITM\ITM\SELECTED=False Or XL_NODRAW=True
							If XL_ITM\ITM\TYP=4
								GUI_COL(XL_ITM\ITM\BCOL)
								Rect XL_TX-2,XL_Y,XL_TW+2,16
								Color 0,0,XL_ITM\ITM\TCOL
								Rect XL_TX-2,XL_Y+8,XL_TW+2,1	
							Else
								GUI_COL(XL_ITM\ITM\BCOL)
								Rect XL_TX-2,XL_Y,XL_TW+2,16
								If XL_ITM\ITM\ACTIVE=True
									GUI_FONTCOL(XL_ITM\ITM\TCOL)
								Else
									GUI_FONTCOL($E0E0E0)
								EndIf
								If XL_ITM\ITM\ICON<>Null
									GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
									GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+16+2,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
								Else
									GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW);Text XL_X,XL_Y,XL_ITM\TXT
								EndIf
							EndIf
						Else
							Color 0,0,0
							Rect XL_TX-2,XL_Y,XL_TW+2,16
							Color 182,189,230
							Rect XL_TX-1,XL_Y+1,XL_TW,14
							GUI_FONTCOL(XL_ITM\ITM\TCOL)
							If XL_ITM\ITM\ICON<>Null
								GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
								GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+16+2,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
							Else
								GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW)
							EndIf
						EndIf
				End Select
				XL_ITM\ITM\X=XL_X:XL_ITM\ITM\Y=XL_Y
				XL_Y=XL_Y+17
				XL_H=XL_H+17
				XL_CNT=XL_CNT+1
				If XL_CNT=GAD\PAD[0] Or XL_CNT=XL_VIS_ITMS
					XL_DONE=True
					XL_LAST=XL_INITIAL+XL_CNT
				Else
					XL_ITM=After XL_ITM
				EndIf
			Else
				XL_ITM=After XL_ITM
			EndIf
			
		Wend

	GUI_PROP_RANGE(GAD\LINK[0]\OBJ,0,QLIMIT(XL_VIS_ITMS,0,99999),0,17)
	
	EndIf
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0:GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_GFXH
End Function

Function GUI_TREE_DRAW(LIST.LIST,GAD.GAD)
	If LIST\IMG>0
		FreeImage LIST\IMG
	EndIf
	LIST\IMG=CreateImage(GAD\W-4,GAD\H-4)
	LIST\LISTGRP\MAX=GAD\PAD[2]
	SetBuffer ImageBuffer(LIST\IMG)
	Origin 0,0:Viewport 0,0,ImageWidth(LIST\IMG),ImageHeight(LIST\IMG)
	GUI_OX=0:GUI_OY=0
	GUI_VPX=0:GUI_VPY=0:GUI_VPW=ImageWidth(LIST\IMG):GUI_VPH=ImageHeight(LIST\IMG)
	ClsColor 255,255,255
	Cls
	ClsColor 0,0,0
	Color 0,0,0
	XL_YPOS=-100000000
	For XL_ITM2.LISTITM=Each LISTITM
		If XL_ITM2\LIST=LIST
			XL_ITM2\X=-100000:XL_ITM2\Y=XL_YPOS
			XL_YPOS=XL_YPOS+1
			Select XL_ITM2\TYP
				Case 5
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_INPUT(GAD\WIN\OBJ,0,0,GAD\W,XL_ITM2\DATUM$,False,GAD\TAB,XL_ITM2\ACTIVE,"",XL_ITM2\TCOL,GUI_RGB_SHADE(XL_ITM2\BCOL,0))
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
						XL_ITM2\SUBGAD\PAD[11]=True
					EndIf
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\STATUS=0
					EndIf
				Case 6
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_INTEGER(GAD\WIN\OBJ,0,0,XL_ITM2\INTV,XL_ITM2\INTMIN,XL_ITM2\INTMAX,XL_ITM2\TXT$,1,0,GAD\TAB,XL_ITM2\ACTIVE,"")
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
					EndIf
					
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\LINK[0]\X=-10000000
						XL_ITM2\SUBGAD\LINK[1]\X=-10000000
						XL_ITM2\SUBGAD\STATUS=0
						XL_ITM2\SUBGAD\LINK[0]\STATUS=0
						XL_ITM2\SUBGAD\LINK[1]\STATUS=0
					EndIf
				Case 7
					If XL_ITM2\SUBGAD=Null
						XL_SGAD=GUI_FLOAT(GAD\WIN\OBJ,0,0,XL_ITM2\FLTV,XL_ITM2\FLTMIN,XL_ITM2\FLTMAX,XL_ITM2\TXT$,XL_ITM2\FPAD[0],0,XL_ITM2\PAD[0],GAD\TAB,XL_ITM2\ACTIVE,"")
						XL_ITM2\SUBGAD=Object.GAD(XL_SGAD)
					EndIf
					
					If XL_ITM2\SUBGAD<>Null
						XL_ITM2\SUBGAD\X=-100000000
						XL_ITM2\SUBGAD\LINK[0]\X=-10000000
						XL_ITM2\SUBGAD\LINK[1]\X=-10000000
						XL_ITM2\SUBGAD\STATUS=0
						XL_ITM2\SUBGAD\LINK[0]\STATUS=0
						XL_ITM2\SUBGAD\LINK[1]\STATUS=0
					EndIf

			End Select
		EndIf
	Next
	Delete Each DSPLST
	XL_VIS_ITMS=GUI_LIST_PROCESSNODE(GAD,Null)
	XL_ITM.DSPLST=First DSPLST
	XL_CNT=0
	If XL_ITM<>Null
		Repeat
			If XL_ITM\ITM\LIST=LIST
				If XL_CNT=GAD\PAD[1]
					XL_DONE=True
					Exit
				Else
					XL_CNT=XL_CNT+1
				EndIf
			EndIf
			If XL_DONE=False
				XL_ITM=After XL_ITM
			EndIf
			If XL_ITM=Null
				XL_DONE=True
			EndIf
		Until XL_DONE=True
		XL_DONE=False
		XL_X=1:XL_Y=1:XL_H=17
		
		XL_CNT=0
		
		While XL_DONE=False And XL_ITM<>Null
			If XL_ITM\ITM\LIST=LIST And XL_ITM\ITM\VIS=True
				If XL_CNT=0
					XL_INITIAL=XL_ITM\ITM\YP
				EndIf
				XL_TX=XL_X+(XL_ITM\ITM\XP*16)
				XL_TW=GAD\W-(4+XL_TX)
				Select XL_ITM\ITM\TYP
					Case 0,2,3,4,8
						If XL_ITM\ITM\TYP=2
							XL_ITM\ITM\SELECTED=False
						EndIf
						If XL_ITM\ITM\SELECTED=False
							If XL_ITM\ITM\TYP=4
								GUI_COL(XL_ITM\ITM\BCOL)
								Rect XL_TX-2,XL_Y,XL_TW+2,16
								Color 0,0,XL_ITM\ITM\TCOL
								Rect XL_TX-2,XL_Y+8,XL_TW+2,1	
							Else
								GUI_COL(XL_ITM\ITM\BCOL)
								Rect XL_TX-2,XL_Y,XL_TW+2,16
								If XL_ITM\ITM\ACTIVE=True
									GUI_FONTCOL(XL_ITM\ITM\TCOL)
								Else
									GUI_FONTCOL($E0E0E0)
								EndIf
								If XL_ITM\ITM\ICON<>Null
									GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
									GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+16+2,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
								Else
									GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW);Text XL_X,XL_Y,XL_ITM\TXT
								EndIf
							EndIf
						Else
							Color 0,0,0
							Rect XL_TX-2,XL_Y,XL_TW+2,16,0
							Color 182,189,230
							Rect XL_TX-1,XL_Y+1,XL_TW,14
										
							GUI_FONTCOL(XL_ITM\ITM\TCOL)
							If XL_ITM\ITM\ICON<>Null
								GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
								GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+16+2,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
							Else
								GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW)
							EndIf
						EndIf
						If XL_ITM\ITM\PARENT<>Null
							XL_PX=XL_ITM\ITM\PARENT\XP
							XL_PY=XL_ITM\ITM\PARENT\Y
							Color 200,200,200
							Rect (XL_PX*16)+8,XL_Y+8,(XL_TX-2)-((XL_PX*16)+8),1
							If XL_PY>0
								Rect (XL_PX*16)+8,XL_PY+16,1,(XL_Y+8)-(XL_PY+16)
							Else
								Rect (XL_PX*16)+8,1,1,(XL_Y+8)-1
							EndIf
						EndIf
					Case 5
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
						If XL_ITM\ITM\ACTIVE=True
							GUI_FONTCOL(XL_ITM\ITM\TCOL)
						Else
							GUI_FONTCOL($E0E0E0)
						EndIf
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							GUI_TEXT(XL_ITM\ITM\TXT,XL_TX+18,XL_Y+1,XL_TW-(XL_ITM\ITM\ICON\W+2))
							XL_IW=21
						Else
							GUI_TEXT(XL_ITM\ITM\TXT,XL_TX,XL_Y+1,XL_TW)
							XL_IW=2
						EndIf
						
						XL_GX=(XL_TX+XL_IW+GUI_STRINGWIDTH(XL_ITM\ITM\TXT)+1)
						XL_GY=XL_Y+1
						XL_GW=(GAD\W-(XL_GX+2))
						XL_GH=16
					
						XL_ITM\ITM\SUBGAD\PAD[9]=True
						XL_ITM\ITM\SUBGAD\INP\IMG_W=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VIS_W=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VPORTW=XL_GW-2
						XL_ITM\ITM\SUBGAD\INP\VIS_H=XL_GH-2
						XL_ITM\ITM\SUBGAD\INP\TXT_COL=XL_ITM\ITM\TCOL
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\W=XL_GW
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W,XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
							Color 0,0,0
							Rect XL_GX-2,XL_GY-2,XL_GAD\W,XL_GAD\H+1,0
						EndIf
						
						;LEFT HAND TREE LINE						
						If XL_ITM\ITM\PARENT<>Null
							XL_PX=XL_ITM\ITM\PARENT\XP
							XL_PY=XL_ITM\ITM\PARENT\Y
							Color 200,200,200
							Rect (XL_PX*16)+8,XL_Y+8,(XL_TX-2)-((XL_PX*16)+8),1
							If XL_PY>0
								Rect (XL_PX*16)+8,XL_PY+16,1,(XL_Y+8)-(XL_PY+16)
							Else
								Rect (XL_PX*16)+8,1,1,(XL_Y+8)-1
							EndIf
						EndIf
								
					Case 6
						
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
												
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							XL_GX=XL_TX+16+3
						Else
							XL_GX=XL_TX+2
						EndIf
						
						XL_GY=XL_Y+2
						
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[0]\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[1]\STATUS=GAD\STATUS
						
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[0]\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[1]\ACTIVE=XL_ITM\ITM\ACTIVE

						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[0]\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[1]\PANEL=GAD\PANEL
						
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\LINK[0]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[0]\Y=XL_ITM\ITM\SUBGAD\Y
						
						XL_ITM\ITM\SUBGAD\LINK[1]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[1]\Y=XL_ITM\ITM\SUBGAD\Y+7
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[0]\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[1]\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W+12+2+GUI_STRINGWIDTH(XL_GAD\TXT$),XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
						EndIf
						
						If XL_ITM\ITM\PARENT<>Null
							XL_PX=XL_ITM\ITM\PARENT\XP
							XL_PY=XL_ITM\ITM\PARENT\Y
							Color 200,200,200
							Rect (XL_PX*16)+8,XL_Y+8,(XL_TX-2)-((XL_PX*16)+8),1
							If XL_PY>0
								Rect (XL_PX*16)+8,XL_PY+16,1,(XL_Y+8)-(XL_PY+16)
							Else
								Rect (XL_PX*16)+8,1,1,(XL_Y+8)-1
							EndIf
						EndIf
					
					Case 7
						
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
												
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							XL_GX=XL_TX+16+3
						Else
							XL_GX=XL_TX+2
						EndIf
						
						XL_GY=XL_Y+2
						
						XL_ITM\ITM\SUBGAD\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[0]\STATUS=GAD\STATUS
						XL_ITM\ITM\SUBGAD\LINK[1]\STATUS=GAD\STATUS
						
						XL_ITM\ITM\SUBGAD\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[0]\ACTIVE=XL_ITM\ITM\ACTIVE
						XL_ITM\ITM\SUBGAD\LINK[1]\ACTIVE=XL_ITM\ITM\ACTIVE

						XL_ITM\ITM\SUBGAD\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[0]\PANEL=GAD\PANEL
						XL_ITM\ITM\SUBGAD\LINK[1]\PANEL=GAD\PANEL
						
						XL_ITM\ITM\SUBGAD\X=XL_GX+GAD\X
						XL_ITM\ITM\SUBGAD\Y=XL_GY+GAD\Y
						XL_ITM\ITM\SUBGAD\LINK[0]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[0]\Y=XL_ITM\ITM\SUBGAD\Y
						
						XL_ITM\ITM\SUBGAD\LINK[1]\X=XL_ITM\ITM\SUBGAD\X+XL_ITM\ITM\SUBGAD\W-1
						XL_ITM\ITM\SUBGAD\LINK[1]\Y=XL_ITM\ITM\SUBGAD\Y+7
						
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[0]\OBJ)
						GUI_REFRESH_GAD(XL_ITM\ITM\SUBGAD\LINK[1]\OBJ)
						
						SetBuffer ImageBuffer(LIST\IMG)
						
						If GAD\WIN\IMG 
							XL_GAD.GAD=XL_ITM\ITM\SUBGAD
							XL_TMP=CreateImage(XL_GAD\W+12+2+GUI_STRINGWIDTH(XL_GAD\TXT$),XL_GAD\H)
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(XL_GAD)
							GrabImage XL_TMP,XL_GAD\X,XL_GAD\Y
							SetBuffer ImageBuffer(LIST\IMG)
							Origin 0,0
							DrawBlock XL_TMP,XL_GX-2,XL_GY-2
							FreeImage XL_TMP
						EndIf
						
						If XL_ITM\ITM\PARENT<>Null
							XL_PX=XL_ITM\ITM\PARENT\XP
							XL_PY=XL_ITM\ITM\PARENT\Y
							Color 200,200,200
							Rect (XL_PX*16)+8,XL_Y+8,(XL_TX-2)-((XL_PX*16)+8),1
							If XL_PY>0
								Rect (XL_PX*16)+8,XL_PY+16,1,(XL_Y+8)-(XL_PY+16)
							Else
								Rect (XL_PX*16)+8,1,1,(XL_Y+8)-1
							EndIf
						EndIf

					Case 1
						GUI_COL(XL_ITM\ITM\BCOL)
						Rect XL_TX-2,XL_Y,XL_TW+2,16
					;	If XL_ITM\ITM\OPEN=True
							GUI_DRAWICON(icn_LISTDIR,XL_TX,XL_Y,XL_ITM\ITM\OPEN,XL_ITM\ITM\ACTIVE)
					;	Else
					;		GUI_DRAWICON(icn_DIRCLOSE,XL_TX,XL_Y,0,XL_ITM\ITM\ACTIVE)
					;	EndIf
						If XL_ITM\ITM\PARENT<>Null
							XL_PX=XL_ITM\ITM\PARENT\XP
							XL_PY=XL_ITM\ITM\PARENT\Y
							Color 200,200,200
							Rect (XL_PX*16)+8,XL_Y+8,(XL_TX-2)-((XL_PX*16)+8),1
							If XL_PY>0
								Rect (XL_PX*16)+8,XL_PY+16,1,(XL_Y+8)-(XL_PY+16)
							Else
								Rect (XL_PX*16)+8,1,1,(XL_Y+8)-1
							EndIf
						EndIf
						If XL_ITM\ITM\ACTIVE=True
							GUI_FONTCOL(XL_ITM\ITM\TCOL)
						Else
							Color 200,200,200
						EndIf
						If XL_ITM\ITM\ICON<>Null
							GUI_DRAWICON(XL_ITM\ITM\ICON,XL_TX+17,(XL_Y+8)-XL_ITM\ITM\ICON\H/2,XL_ITM\ITM\ON,XL_ITM\ITM\ACTIVE)
							GUI_TEXT(XL_ITM\ITM\TXT,17+XL_TX+16+2,XL_Y+1,XL_TW-(17+XL_ITM\ITM\ICON\W+2))
						Else
							GUI_TEXT(XL_ITM\ITM\TXT,17+XL_TX,XL_Y+1,XL_TW-17);Text XL_X,XL_Y,XL_ITM\TXT
						EndIf
						
				End Select		
						
				XL_ITM\ITM\X=XL_X:XL_ITM\ITM\Y=XL_Y
				XL_Y=XL_Y+17
				XL_H=XL_H+17
				XL_CNT=XL_CNT+1
				If XL_CNT=GAD\PAD[0] Or XL_CNT=XL_VIS_ITMS
					XL_DONE=True
					XL_LAST=XL_INITIAL+XL_CNT
				Else
					XL_ITM=After XL_ITM
				EndIf
			Else
				XL_ITM=After XL_ITM
			EndIf
			
		Wend

	;Stop
	
	GUI_PROP_RANGE(GAD\LINK[0]\OBJ,0,QLIMIT(XL_VIS_ITMS,0,99999),0,17)
	
	SetBuffer ImageBuffer(LIST\IMG)
	Origin 0,0:Viewport 0,0,ImageWidth(LIST\IMG),ImageHeight(LIST\IMG)
	GUI_OX=0:GUI_OY=0
	GUI_VPX=0:GUI_VPY=0:GUI_VPW=ImageWidth(LIST\IMG):GUI_VPH=ImageHeight(LIST\IMG)

	XL_CNT=0
	XL_X=1:XL_Y=1:XL_H=17
	For XL_ITM.DSPLST=Each DSPLST
		If XL_ITM\ITM\LIST=LIST And XL_ITM\ITM\VIS=True
			If XL_ITM\ITM\PARENT<>Null
				If XL_ITM\ITM\X<0 And XL_ITM\ITM\Y<0
					XL_FIRST.DSPLST=GUI_ITEMNODE(XL_ITM)		
					If XL_FIRST<>Null
						If XL_FIRST\ITM\X>0 And XL_FIRST\ITM\Y>0
							Color 200,200,200
							Rect (XL_FIRST\ITM\XP*16)+8,XL_FIRST\ITM\Y+16,1,GAD\H-(XL_FIRST\ITM\Y+16)
						Else
							Color 200,200,200
							If XL_FIRST\ITM\YP<XL_INITIAL And XL_ITM\ITM\YP>XL_LAST
								Rect (XL_FIRST\ITM\XP*16)+8,1,1,GAD\H-2
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			XL_CNT=XL_CNT+1
		EndIf
	Next
	EndIf
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0:GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_GFXH
End Function

Function GUI_ITEMNODE.DSPLST(XL_DIR.DSPLST)
	XL_ITM.DSPLST=XL_DIR
	While XL_ITM<>Null
		If XL_ITM\ITM\LIST=XL_DIR\ITM\LIST And XL_ITM\ITM\VIS=True
			If XL_ITM\ITM=XL_DIR\ITM\PARENT And XL_ITM\ITM\OPEN=True
				Return XL_ITM
			EndIf
		EndIf
		XL_ITM=Before XL_ITM
	Wend 
	Return Null
End Function

Function GUI_PREVITEM.LISTITM(XL_ITM.LISTITM)
	RET.LISTITM=Null
	
	XL_NXT.LISTITM=XL_ITM
	
	While XL_DONE=False
		XL_NXT=Before XL_NXT
		If XL_NXT<>Null
			If XL_NXT\PARENT=XL_ITM\PARENT And XL_NXT\LIST=XL_ITM\LIST
				RET=XL_NXT
				XL_DONE=True
			EndIf
		Else
			XL_DONE=True
		EndIf
	Wend
	
	Return RET
End Function

Function GUI_NEXTITEM.LISTITM(XL_ITM.LISTITM)
	RET.LISTITM=Null
	
	XL_NXT.LISTITM=XL_ITM
	
	While XL_DONE=False
		XL_NXT=After XL_NXT
		If XL_NXT<>Null
			If XL_NXT\PARENT=XL_ITM\PARENT And XL_NXT\LIST=XL_ITM\LIST
				RET=XL_NXT
				XL_DONE=True
			EndIf
		Else
			XL_DONE=True
		EndIf
	Wend
	
	Return RET
End Function

Function GUI_LIST_TXTON(XL_LST)
			
	If XL_LST<1 Return
	
	XL_LIST.LIST=Object.LIST(XL_LST)
		
	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=XL_LIST
			If XL_ITM\TYP=0 And XL_ITM\ON=True
				RET=XL_ITM\OBJ
				Exit
			EndIf
		EndIf
	Next
	
	Return RET
	
End Function

Function GUI_LIST_GROUP_OFF(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Turns an ITEMS Group Off (mainly interanl use)
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_OITM.LISTITM=Object.LISTITM(XL_ITEM)

	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=XL_OITM\LIST
			If XL_ITM\TYP=3 And XL_ITM\GRP=XL_OITM\GRP
				XL_ITM\ON=False
			EndIf
		EndIf
	Next
End Function

Function GUI_LIST_KILLNODE(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;INTERNAL USE - use Gui_List_KillItm()
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	If XL_ITM\TYP=1
		XL_LIST.LIST=XL_ITM\LIST
		GUI_DELETE_NODE(XL_LIST\OBJ,XL_ITM\OBJ)
		Delete XL_ITM
		GUI_LIST_REFRESH(XL_LIST\OBJ)
	EndIf
End Function

Function GUI_DELETE_NODE(XL_LST,XL_ND=0)
	;---------------------------------------------------------------------------------------------------------------------
	;INTERNAL USE ONLY - so bugger off!
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_LST<1 Return
	
	XL_LIST.LIST=Object.LIST(XL_LST)
	XL_NODE.LISTITM=Object.LISTITM(XL_ND)

	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=XL_LIST And XL_ITM<>XL_NODE And XL_ITM\PARENT=XL_NODE
			If XL_ITM\TYP<>1
				Delete XL_ITM
			Else
				GUI_DELETE_NODE(XL_ITM\LIST\OBJ,XL_ITM\OBJ)
				Delete XL_ITM
			EndIf
		EndIf
	Next
End Function

;=====================================================================================

Function GUI_LIST_NUMITEMS(XL_LST,XL_ND=0)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the number of items ina list node
	;---------------------------------------------------------------------------------------------------------------------

	If XL_LST<1 Return
	
	XL_LIST.LIST=Object.LIST(XL_LST)
	XL_NODE.LISTITM=Object.LISTITM(XL_ND)

	For XL_ITEM.LISTITM=Each LISTITM
		If XL_ITEM\LIST=XL_LIST And XL_ITEM\PARENT=XL_NODE
			RET=RET+1
		EndIf
	Next	
	
	Return RET
	
End Function

Function GUI_LIST_CURPOS(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a list items current position
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	For XL_IT.LISTITM=Each LISTITM
		If XL_IT\LIST=XL_ITM\LIST
			If XL_IT\PARENT=XL_ITM\PARENT
				If XL_IT=XL_ITM
					Return RET
				Else
					RET=RET+1
				EndIf
			EndIf
		EndIf
	Next
	
	Return False
	
End Function

Function GUI_ITEM_ON(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a TRUE if the Item is ON
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	Return XL_ITM\ON
	
End Function

Function GUI_ITEM_SELECTED(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a TRUE if the Item is SELECTED
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	Return XL_ITM\SELECTED
	
End Function

Function GUI_LIST_SELECTED(XL_LIST,XL_INDEX=1)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a first selected item in a list
	;---------------------------------------------------------------------------------------------------------------------

	If XL_LIST<1 Return
	
	LST.LIST=Object.LIST(XL_LIST)
	
	XL_CURINDEX=1
	
	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=LST
			If XL_ITM\SELECTED=True
				If XL_INDEX=1 Or XL_INDEX=XL_CURINDEX
					Return XL_ITM\OBJ
				Else
					XL_CURINDEX=XL_CURINDEX+1
				EndIf
			EndIf
		EndIf
	Next
	
End Function

Function GUI_LIST_ON(XL_LIST,XL_INDEX=1)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a first ON item in a list
	;---------------------------------------------------------------------------------------------------------------------

	If XL_LIST<1 Return
	
	LST.LIST=Object.LIST(XL_LIST)
	
	XL_CURINDEX=1
	
	For XL_ITM.LISTITM=Each LISTITM
		If XL_ITM\LIST=LST
			If XL_ITM\ON=True
				If XL_INDEX=1 Or XL_INDEX=XL_CURINDEX
					Return XL_ITM\OBJ
				Else
					XL_CURINDEX=XL_CURINDEX+1
				EndIf
			EndIf
		EndIf
	Next
	
End Function

Function GUI_LIST_ITEM(XL_LST,XL_ND=0,XL_POS=1)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the ID of an ITEM in a NODE
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_LST<1 Return
	
	XL_LIST.LIST=Object.LIST(XL_LST)
	XL_NODE.LISTITM=Object.LISTITM(XL_ND)
	
	XL_RET=-1
	XL_ITM=1
	
	For XL_ITEM.LISTITM=Each LISTITM
		If XL_ITEM\LIST=XL_LIST And XL_ITEM\PARENT=XL_NODE
			XL_RET=XL_ITEM\OBJ
			If XL_POS=XL_ITM
				Exit
			Else
				XL_ITM=XL_ITM+1
			EndIf
		EndIf
	Next
	
	Return XL_RET
		
End Function

Function GUI_ITEM_TYPE(XL_ITM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns a List ITEMS Type
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
	
	XL_ITEM.LISTITM=Object.LISTITM(XL_ITM)
	
	Return XL_ITEM\TYP
			
End Function

Function GUI_ITEM_ICON$(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns the Items icon
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	If XL_ITM\ICON<>Null
		Return XL_ITM\ICON\NAME$
	Else
		Return ""
	EndIf
	
End Function

Function GUI_ITEM_SETICON(XL_ITEM,XL_ICON$)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an items icon
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	XL_ITM\ICON=GUI_FINDICON(XL_ICON$)
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
	
End Function

Function GUI_ITEM_GAD(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns an items gadget
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	If XL_ITM\SUBGAD<>Null
		Return XL_ITM\SUBGAD\OBJ
	Else
		Return 
	EndIf
	
End Function

Function GUI_ITEM_POS(XL_ITEM,XL_POS)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an items position
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	If XL_POS<0 Then XL_POS=0
	
	If XL_ITM\PARENT<>Null
		XL_MAX=GUI_LIST_NUMITEMS(XL_ITM\LIST\OBJ,XL_ITM\PARENT\OBJ)
	Else
		XL_MAX=GUI_LIST_NUMITEMS(XL_ITM\LIST\OBJ,0)-1
	EndIf
	
	If XL_POS>XL_MAX Then XL_POS=XL_MAX
	
	XL_CURPOS=GUI_LIST_CURPOS(XL_ITEM)	
	
	XL_STEP=XL_POS-XL_CURPOS
	
	If XL_STEP=0 Return
	
	XL_PLUS=Sgn(XL_STEP)
	
	Repeat
		If XL_PLUS=-1
			XL_NXT.LISTITM=GUI_PREVITEM(XL_ITM)
			If XL_NXT=Null
				XL_DONE=True
			Else
				Insert XL_ITM Before XL_NXT
				XL_STEP=XL_STEP-XL_PLUS
			EndIf
		Else
			XL_NXT.LISTITM=GUI_NEXTITEM(XL_ITM)
			If XL_NXT=Null
				XL_DONE=True
			Else
				Insert XL_ITM After XL_NXT
				XL_STEP=XL_STEP-XL_PLUS
			EndIf
		EndIf
		If XL_STEP=0
			XL_DONE=True
		EndIf	
	Until XL_DONE=True
	
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
	
End Function

Function GUI_ITEM_MOVE(XL_ITEM,XL_STEP)
	;---------------------------------------------------------------------------------------------------------------------
	;Move an items position up or down
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	If XL_STEP=0 Return
		
	If XL_ITM\PARENT<>Null
		XL_MAX=GUI_LIST_NUMITEMS(XL_ITM\LIST\OBJ,XL_ITM\PARENT\OBJ)
	Else
		XL_MAX=GUI_LIST_NUMITEMS(XL_ITM\LIST\OBJ,0)-1
	EndIf
	
	XL_CURPOS=GUI_LIST_CURPOS(XL_ITEM)
	
	XL_POS=XL_CURPOS+XL_STEP
	
	If XL_POS>XL_MAX Then XL_POS=XL_MAX
	If XL_POS<0 Then XL_POS=0
		
	XL_STEP=XL_POS-XL_CURPOS
	
	If XL_STEP=0 Return
	
	XL_PLUS=Sgn(XL_STEP)
	
	Repeat
		If XL_PLUS=-1
			XL_NXT.LISTITM=GUI_PREVITEM(XL_ITM)
			If XL_NXT=Null
				XL_DONE=True
			Else
				Insert XL_ITM Before XL_NXT
				XL_STEP=XL_STEP-XL_PLUS
			EndIf
		Else
			XL_NXT.LISTITM=GUI_NEXTITEM(XL_ITM)
			If XL_NXT=Null
				XL_DONE=True
			Else
				Insert XL_ITM After XL_NXT
				XL_STEP=XL_STEP-XL_PLUS
			EndIf
		EndIf
		If XL_STEP=0
			XL_DONE=True
		EndIf	
	Until XL_DONE=True
	
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
	
End Function

Function GUI_LIST_SETACT(XL_ITEM,XL_ACT)
	;---------------------------------------------------------------------------------------------------------------------
	;Set a LIST ITEM Active or Inactive
	;---------------------------------------------------------------------------------------------------------------------

	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	XL_ITM\ACTIVE=XL_ACT
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
End Function

Function GUI_LIST_SETTEXT(XL_ITEM,XL_TXT$)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an ITEMS Text
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	XL_ITM\TXT$=XL_TXT$
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
End Function

Function GUI_LIST_SETCOL(XL_ITEM,XL_TCOL,XL_BCOL=$FFFFFF)
	;---------------------------------------------------------------------------------------------------------------------
	;Set an ITEMS color
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	XL_ITM\TCOL=XL_TCOL
	XL_ITM\BCOL=XL_BCOL
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
End Function

Function GUI_LIST_GETTEXT$(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns an ITEMS text
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	Return XL_ITM\TXT$
End Function

Function GUI_LIST_ITMLIST(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Returns an ITEMS LIST
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	
	Return XL_ITM\LIST\OBJ
End Function

Function GUI_LIST_TOGGLE(XL_ITEM,XL_ON,XL_OPEN=True)
	;---------------------------------------------------------------------------------------------------------------------
	;Toggle a LIST ITEM On/Selected or Off/Not Selected
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	GAD.GAD=XL_ITM\LIST\GAD
	If GAD=Null Return
	
	XL_GRP.LISTGROUP=XL_ITM\ITEMGRP
		
	Select XL_ITM\TYP
		Case 0
			If XL_ITM\ITEMGRP=XL_ITM\LIST\LISTGRP
				If XL_ON=True
					If XL_ITM\SELECTED=True
						XL_ITM\SELECTED=False
						XL_ITM\ON=False
						EVENT\ITM_TOGGLE=True
						XL_GRP\CUR=XL_GRP\CUR-1
					Else
						If XL_GRP\MAX=1
							XL_ITM2.LISTITM=First LISTITM
							While XL_ITM2<>Null
								If XL_ITM2\LIST=GAD\LIST
									XL_ITM2\SELECTED=False
									XL_ITM2\ON=False
								EndIf
								XL_ITM2=After XL_ITM2
							Wend
							XL_ITM\SELECTED=True
							XL_ITM\ON=True
							EVENT\ITM_TOGGLE=True
						Else
							If XL_GRP\CUR<XL_GRP\MAX
								XL_GRP\CUR=XL_GRP\CUR+1
								XL_ITM\SELECTED=True
								XL_ITM\ON=True
								EVENT\ITM_TOGGLE=True
							EndIf
						EndIf
					EndIf
				Else
					XL_ITM\SELECTED=False
					XL_ITM\ON=False
					EVENT\ITM_TOGGLE=True
					XL_GRP\CUR=XL_GRP\CUR-1
				EndIf
			Else
				If XL_ON=True
					If XL_ITM\SELECTED=False
						If XL_GRP\CUR<XL_GRP\MAX
							XL_GRP\CUR=XL_GRP\CUR+1
							XL_ITM\TIMER=MilliSecs()
							XL_ITM\SELECTED=True
							XL_ITM\ON=True
						Else
							XL_TIMER=MilliSecs()
							XL_LASTITM.LISTITM=Null
							XL_ITM2.LISTITM=First LISTITM
							While XL_ITM2<>Null
								If XL_ITM2\LIST=XL_ITM\LIST And XL_ITM2\ITEMGRP=XL_GRP And (XL_ITM2\ON=True Or XL_ITM2\SELECTED=True) And XL_ITM2<>XL_ITM
									If XL_ITM2\TIMER<XL_TIMER And XL_ITM2\TIMER>0
										XL_LASTITM=XL_ITM2
									EndIf
								EndIf
								XL_ITM2=After XL_ITM2
							Wend
							If XL_LASTITM<>Null
								XL_LASTITM\SELECTED=False
								XL_LASTITM\ON=False
								XL_LASTITM\TIMER=0
								XL_ITM\ON=True
								XL_ITM\SELECTED=True
								XL_ITM\TIMER=MilliSecs()
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		Case 1
			XL_ITM\OPEN=XL_ON
			XL_ITM\SELECTED=XL_ON
		Case 2,8
			If XL_GRP=XL_ITM\LIST\LISTGRP
				XL_ITM\ON=XL_ON
				XL_ITM\SELECTED=XL_ON
			Else
				If XL_ON=True
					If XL_GRP\CUR<XL_GRP\MAX
						XL_GRP\CUR=XL_GRP\CUR+1
						XL_ITM\ON=True
						XL_ITM\SELECTED=True
						XL_ITM\TIMER=MilliSecs()
					Else
						XL_TIMER=MilliSecs()
						XL_LASTITM.LISTITM=Null
						XL_ITM2.LISTITM=First LISTITM
						While XL_ITM2<>Null
							If XL_ITM2\LIST=XL_ITM\LIST And XL_ITM2\ITEMGRP=XL_GRP And (XL_ITM2\ON=True Or XL_ITM2\SELECTED=True) And XL_ITM2<>XL_ITM
								If XL_ITM2\TIMER<XL_TIMER And XL_ITM2\TIMER>0
									XL_LASTITM=XL_ITM2
								EndIf
							EndIf
							XL_ITM2=After XL_ITM2
						Wend
						If XL_LASTITM<>Null
							XL_LASTITM\SELECTED=False
							XL_LASTITM\ON=False
							XL_LASTITM\TIMER=0
							XL_ITM\ON=True
							XL_ITM\SELECTED=True
							XL_ITM\TIMER=MilliSecs()
						EndIf
					EndIf
				EndIf
			EndIf
		Case 3
			XL_ITM\SELECTED=False
			If XL_ITM\ON=False And XL_ON=True
				GUI_LIST_GROUP_OFF(XL_ITM\OBJ)
				XL_ITM\ON=XL_ON
			EndIf
	End Select
	
	If XL_ON=True And XL_OPEN=True
		GUI_LIST_OPEN(XL_ITM\OBJ)
	EndIf
	
	GUI_LIST_REFRESH(XL_ITM\LIST\OBJ)
End Function

Function GUI_LIST_KILLITM(XL_ITEM)
	;---------------------------------------------------------------------------------------------------------------------
	;Delete a LIST ITEM
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITEM<1 Return
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	XL_LIST=XL_ITM\LIST\OBJ
	
	If XL_ITM\TYP<>1
		If XL_ITM\SUBGAD<>Null
			GUI_FREEGAD(XL_ITM\SUBGAD\OBJ)
		EndIf
		Delete XL_ITM
	Else
		GUI_LIST_KILLNODE(XL_ITM\OBJ)
	EndIf
	
	XL_LST.LIST=Object.LIST(XL_LIST)
	If XL_LST<>Null
		GUI_LIST_REFRESH(XL_LIST)
	EndIf
End Function

Function GUI_LIST_CLEAR(XL_LST)
	;---------------------------------------------------------------------------------------------------------------------
	;Clear a LIST of all Items
	;---------------------------------------------------------------------------------------------------------------------

	If XL_LST<1 Return
	
	XL_LIST.LIST=Object.LIST(XL_LST)

	GUI_DELETE_NODE(XL_LIST\OBJ)
	GUI_LIST_REFRESH(XL_LIST\OBJ)
End Function

Function GUI_LIST_OPEN(XL_ITEM)
	
	XL_ITM.LISTITM=Object.LISTITM(XL_ITEM)
	If XL_ITM\PARENT<>Null
		XL_ITM\PARENT\OPEN=True
		GUI_LIST_OPEN(XL_ITM\PARENT\OBJ)
	EndIf
	
End Function

Function GUI_LIST_NEWGROUP(XL_LIST,XL_MAX)
	;---------------------------------------------------------------------------------------------------------------------
	;Create a new LIST GROUP
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_LIST<1 Return False
	
	XL_GROUP.LISTGROUP=New LISTGROUP
	XL_GROUP\OBJ=Handle(XL_GROUP)
	XL_GROUP\MAX=XL_MAX
	XL_GROUP\LIST=Object.LIST(XL_LIST)
	
	Return XL_GROUP\OBJ
	
End Function

Function GUI_ITEM_ADDGROUP(XL_ITM,XL_GRP)
	;---------------------------------------------------------------------------------------------------------------------
	;Add an ITEM to a LIST GROUP
	;---------------------------------------------------------------------------------------------------------------------
	
	If XL_ITM<1 Return
		
	XL_ITEM.LISTITM=Object.LISTITM(XL_ITM)
	
	XL_GROUP.LISTGROUP=Object.LISTGROUP(XL_GRP)
	
	If XL_GROUP=Null
		XL_GROUP=XL_ITEM\LIST\LISTGRP
	EndIf
	
	XL_ITEM\ITEMGRP=XL_GROUP
	
End Function