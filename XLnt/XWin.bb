Function GUI_WINDOW(XL_X,XL_Y,XL_W,XL_H,XL_TXT$,XL_ICON$="",XL_FLAG=flg_DEFAULT,XL_MOD=mod_NORM,XL_COL0=$FF00FF,XL_BCOL0=$FF00FF,XL_TXTCOL=$FF00FF,XL_COL1=$FF00FF,XL_BCOL1=$FF00FF)
	RET.WIN=New WIN
	RET\ID=GUI_ID()
	RET\W=XL_W
	RET\H=XL_H
	RET\OBJ=Handle(RET)
	RET\ICON=GUI_FINDICON(XL_ICON$)
	
	If XL_X=-1
		XL_X=GUI_GFXW/2-RET\W/2
	EndIf
	If XL_Y=-1
		XL_Y=GUI_GFXH/2-RET\H/2
	EndIf
	
	RET\TITLE$=XL_TXT$
	If XL_TXT$<>""
		RET\MINH=22+16
	Else
		RET\MINH=16
	EndIf
	RET\MODAL=XL_MOD
	RET\X=XL_X:RET\Y=XL_Y
	RET\FLAG=XL_FLAG
	If XL_COL0<>$FF00FF;GUI_WINCOL
		RET\WCOL[0]=XL_COL0
		RET\WCOL[1]=GUI_RGB_SHADE(RET\WCOL[0],32)
		RET\WCOL[2]=GUI_RGB_SHADE(RET\WCOL[0],-32)
		If XL_COL1=$FF00FF
			RET\WCOL[3]=GUI_RGB_SHADE(RET\WCOL[0],-18)
		Else
			RET\WCOL[3]=XL_COL1
		EndIf
	Else
		RET\WCOL[0]=GUI_WINCOL
		RET\WCOL[1]=GUI_WINHILITE
		RET\WCOL[2]=GUI_WINLOLITE
		RET\WCOL[3]=GUI_RGB_SHADE(RET\WCOL[0],-18)
	EndIf
	
	If XL_BCOL0<>$FF00FF
		RET\BCOL[0]=XL_BCOL0
		RET\BCOL[1]=GUI_RGB_SHADE(RET\BCOL[0],32)
		RET\BCOL[2]=GUI_RGB_SHADE(RET\BCOL[0],-32)
		If XL_BCOL1=$FF00FF
			RET\BCOL[3]=GUI_RGB_SHADE(RET\BCOL[0],-18)
		Else
			RET\BCOL[3]=XL_BCOL1
		EndIf
	Else
		RET\BCOL[0]=GUI_WINBORDER
		RET\BCOL[1]=GUI_RGB_SHADE(RET\BCOL[0],32)
		RET\BCOL[2]=GUI_RGB_SHADE(RET\BCOL[0],-32)
		RET\BCOL[3]=GUI_RGB_SHADE(RET\BCOL[0],-18)
	EndIf
	RET\MENU=New MENU
	RET\MENU\WIN=RET
	RET\MENU\OBJ=Handle(RET\MENU)
	If GUI_FLAG(XL_FLAG,flg_MENU)=True
		If RET\TITLE$<>""
			RET\MENUY=23
			RET\MINH=43;+12
		Else
			RET\MENUY=2
			RET\MINH=22;+12
		EndIf
		If GUI_FLAG(RET\FLAG,flg_BORDER)=False
			RET\MENUX=2
		Else
			RET\MENUX=5
		EndIf
	EndIf
	RET\PAD[4]=RET\TABX
	RET\PAD[0]=RET\W
	RET\PAD[1]=RET\H
	
	If XL_TXTCOL<>$FF00FF
		RET\TITLE_COL=XL_TXTCOL
	Else
		RET\TITLE_COL=GUI_WINTXTCOL
	EndIf
	
	RET\PANEL=New PANEL
	RET\PANEL\W=RET\W
	RET\PANEL\H=RET\H
	RET\TAB_PANEL=New PANEL
	RET\TAB_PANEL\PAR=RET\PANEL
	RET\TAB_PANEL\ACT=True
	
	RET\PANEL\ACT=True
	If GUI_FLAG(XL_FLAG,flg_SCALE)
		RET\IMG_SCALE=CopyImage(GUI_IMG_SCALE)
		RET\IMG_SCALE1=CopyImage(GUI_IMG_SCALE1)
		GUI_COLORTAB(RET\IMG_SCALE,RET\WCOL[0],RET\WCOL[1],RET\WCOL[2])
		GUI_COLORTAB(RET\IMG_SCALE1,RET\WCOL[0],RET\WCOL[1],RET\WCOL[2])
	EndIf
	Return RET\OBJ
End Function

Function GUI_COLORTAB(XL_IMG,XL_COL0,XL_COL1,XL_COL2)
	XL_MASK=GUI_TRUECOL($FF00FF)
	XL_BK=GUI_TRUECOL(0)
	XL_RD=GUI_TRUECOL($FF0000)
	XL_GR=GUI_TRUECOL($00FF00)
	XL_BL=GUI_TRUECOL($0000FF)
	LockBuffer ImageBuffer(XL_IMG)
	For XL_Y=0 To ImageHeight(XL_IMG)
		For XL_X=0 To ImageWidth(XL_IMG)
			XL_COL=ReadPixelFast(XL_X,XL_Y,ImageBuffer(XL_IMG)) And $FFFFFF
			If XL_COL<>XL_MASK
				Select XL_COL
					Case XL_RD
						XL_COL=XL_COL1
					Case XL_GR
						XL_COL=XL_COL0
					Case XL_BL
						XL_COL=XL_COL2
				End Select
				WritePixelFast XL_X,XL_Y,XL_COL,ImageBuffer(XL_IMG)
			EndIf
		Next
	Next
	UnlockBuffer ImageBuffer(XL_IMG)
		
End Function

Function GUI_WIN_TABAREA(XL_WIN,XL_X,XL_Y,XL_W,XL_H)
	WIN.WIN=Object.WIN(XL_WIN)
	WIN\TABX=XL_X
	WIN\TABW=QLIMIT(XL_W,WIN\TABW,(WIN\W-2)-WIN\TABX)
	WIN\TABY=XL_Y
	WIN\TABH=QLIMIT(XL_H,10,(WIN\H-2)-WIN\TABY)
	WIN\PAN_ACT=True
	WIN\PAD[4]=WIN\TABX
	GUI_REFRESH(WIN\OBJ)
End Function

Function GUI_WINFRONT(WIN.WIN)
	Insert WIN After Last WIN
	GUI_ACTIVEWIN=WIN
End Function

Function GUI_WINBACK(WIN.WIN)
	Insert WIN Before First WIN
	GUI_ACTIVEWIN=Last WIN
End Function