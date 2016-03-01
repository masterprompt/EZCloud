;XLnt ii
;3D SPECIFIC FUNCTIONS
;ONLY TO BE INCLUDED IF B3D IS IN USE
;
;PLEASE SES THE DOCS

Function GUI_3DPORT(XL_WIN,xl_X,xl_Y,XL_W,XL_H,XL_TXT$="",XL_CAM=0,XL_WIRE=False,xl_TAB=0,xl_COL=$B6BDE6,xl_TCOL=0)
	WIN.WIN=Object.WIN(XL_WIN)
	RET.GAD=New GAD
	RET\OBJ=Handle(RET)
	RET\WIN=WIN
	RET\TAB=XL_TAB
	RET\STATUS=1
	RET\ACTIVE=True
	RET\ID=GUI_ID()
	RET\TYP=gad_3D
	RET\X=xl_X:RET\Y=xl_Y
	RET\W=XL_W
	RET\H=XL_H
	RET\CAP$=XL_TXT$
	If XL_CAM=0
		XL_CAM=CreateCamera()
	EndIf
	CameraViewport XL_CAM,0,0,RET\W-2,RET\H-2
	HideEntity XL_CAM
	RET\PAD[0]=XL_CAM
	RET\PAD[1]=XL_WIRE
	RET\LINK[0]=Object.GAD(GUI_FRAME(XL_WIN,XL_X,XL_Y,RET\W,RET\H,"",0,xl_TAB))
	RET\LINK[0]\PARENT=RET
	RET\COL[0]=XL_COL
	RET\TXT_COL=XL_TCOL
	RET\PANEL=WIN\PANEL
	RET\TW=RET\W:RET\TH=RET\H
	GUI_GADTAB(RET\OBJ)
	GUI_3DCAM=RET\OBJ
	Return RET\OBJ
End Function

Function GUI_RENDER3D(XL_GAD,XL_TWEEN=1)
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\TYP=gad_3D
		If (GAD\TAB=0 Or GAD\TAB=GAD\WIN\TAB) And (GAD\STATUS=gad_SHOW Or GAD\STATUS=gad_LOCK)
			If GAD\WIN\STATUS=win_OPEN
				WIN.WIN=GAD\WIN
				SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
				ShowEntity GAD\PAD[0]
				WireFrame GAD\PAD[1]
				RenderWorld(XL_TWEEN)
				WireFrame 0
				HideEntity GAD\PAD[0]
				XL_IMG=CreateImage(GAD\W-2,GAD\H-2)
				DrawViewStuff()
				GrabImage XL_IMG,0,0
				SetBuffer ImageBuffer(GAD\WIN\IMG)
				
				GUI_GAD_BUFFER(GAD)
				DrawBlock XL_IMG,GAD\X+1,GAD\Y+1
				FreeImage XL_IMG
				If GAD\CAP$<>""
					XL_TW=QLIMIT(GUI_STRINGWIDTH(GAD\CAP$)+8,0,GAD\W-2)
					Color 0,0,0
					Rect GAD\X,GAD\Y,XL_TW,17,0
					Color 0,0,GAD\COL[0]
					Rect GAD\X+1,GAD\Y+1,XL_TW-2,15
					GUI_FONTCOL(GAD\TXT_COL)
					GUI_TEXT(GAD\CAP$,GAD\X+2,GAD\Y+2,XL_TW)
					;GUI_FONTCOL(GAD\TXT_COL)
					;GUI_TEXT(GAD\CAP$,GAD\X+2,GAD\Y+2,GAD\W)
				EndIf
				SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0
				Cls
			EndIf
		EndIf
		WIN.WIN=GAD\WIN
		If GUI_FLAG(WIN\FLAG,flg_SCALE) And WIN\STATUS=win_OPEN
			SetBuffer ImageBuffer(WIN\IMG)
			If GUI_WINMODE<>Wmode_RESIZE
				DrawImage WIN\IMG_SCALE,WIN\W-16,WIN\H-16
			Else
				DrawImage WIN\IMG_SCALE1,WIN\W-16,WIN\H-16
			EndIf
			SetBuffer BackBuffer()
		EndIf
	EndIf
End Function

Function GUI_SETWIRE(XL_GAD,XL_WIRE)
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\TYP=gad_3D
		GAD\PAD[1]=XL_WIRE
	EndIf
End Function

Function GUI_GADWIRE(XL_GAD)
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\PAD[1]	
End Function

Function GUI_SETCAM(XL_GAD,XL_CAM,XL_KILL=True)
	GAD.GAD=Object.GAD(XL_GAD)
	If GAD\TYP=gad_3D
		CameraViewport XL_CAM,0,0,GAD\W-2,GAD\H-2
		HideEntity XL_CAM
		If XL_KILL=True
			FreeEntity GAD\PAD[0]
		EndIf
		GAD\PAD[0]=XL_CAM
	EndIf
End Function

Function GUI_GETCAM(XL_GAD)
	GAD.GAD=Object.GAD(XL_GAD)
	Return GAD\PAD[0]
End Function