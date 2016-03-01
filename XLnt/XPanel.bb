Function GUI_PANEL_TOGGLE(GAD.GAD)
	XL_GAD.GAD=First GAD
	If GAD\ON=True
		While XL_GAD<>Null
			If XL_GAD\WIN=GAD\WIN
			XL_MOVE=True
			If GAD<>XL_GAD
					If XL_GAD\TYP=GAD_PANEL
					If XL_GAD\PANEL\PAR=GAD\PANEL\PAR And XL_GAD\TAB=GAD\TAB
						If (RectsOverlap(XL_GAD\X,0,XL_GAD\W,1,GAD\X,0,GAD\W,1) And XL_GAD\Y>=GAD\Y) And XL_GAD\PAD[10]>0
							XL_GAD\Y=XL_GAD\Y-(GAD\TH-GAD\LINK[0]\H)
							XL_GAD\PAD[10]=XL_GAD\PAD[10]-1
							XL_GAD\PANEL\Y=XL_GAD\Y
							XL_GAD\LINK[0]\Y=XL_GAD\Y
							XL_GAD\LINK[1]\Y=XL_GAD\Y
						EndIf
					EndIf
				Else
					If XL_GAD\PANEL=GAD\PANEL\PAR And XL_GAD\PAD[10]>0 And XL_GAD\TAB=GAD\TAB
						If (RectsOverlap(XL_GAD\X,0,XL_GAD\W,1,GAD\X,0,GAD\W,1) And XL_GAD\Y>=GAD\Y)
							If XL_GAD\PARENT<>Null
								If XL_GAD\PARENT\TYP=gad_PANEL
									XL_MOVE=False
								EndIf
							EndIf
							If XL_MOVE=True
								If XL_GAD\PARENT=Null
									XL_GAD\Y=XL_GAD\Y-(GAD\TH-GAD\LINK[0]\H)
									XL_GAD\PAD[10]=XL_GAD\PAD[10]-1
									For XL_T=0 To 3
										If XL_GAD\LINK[XL_T]<>Null
											XL_GAD\LINK[XL_T]\Y=XL_GAD\LINK[XL_T]\Y-(GAD\TH-GAD\LINK[0]\H)
											XL_GAD\LINK[XL_T]\PAD[10]=XL_GAD\LINK[XL_T]\PAD[10]-1
											For XL_TT=0 To 3
												If XL_GAD\LINK[XL_T]\LINK[XL_TT]<>Null
													XL_GAD\LINK[XL_T]\LINK[XL_TT]\Y=XL_GAD\LINK[XL_T]\LINK[XL_TT]\Y-(GAD\TH-GAD\LINK[0]\H)
													XL_GAD\LINK[XL_T]\LINK[XL_TT]\PAD[10]=XL_GAD\LINK[XL_T]\LINK[XL_TT]\PAD[10]-1
												Else
													Exit
												EndIf
											Next
										Else
											Exit
										EndIf
									Next
								EndIf
								If XL_GAD\TYP=gad_TOOLBAR
									XL_GAD\PANEL\Y=XL_GAD\Y
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
		XL_GAD=After XL_GAD
		Wend
		GAD\TH=GAD\LINK[0]\TH
		GAD\LINK[0]\X=GAD\X:GAD\LINK[0]\Y=GAD\Y
		GAD\LINK[1]\X=GAD\X:GAD\LINK[1]\Y=GAD\Y
		GAD\ON=False
		GAD\PANEL\ACT=False
		GAD\LINK[0]\ON=False
		GAD\LINK[1]\STATUS=gad_HIDE
		If GAD\TOOL<>Null
			GUI_REFRESH_TOOLBAR(GAD\TOOL)
		EndIf
		GUI_REFRESH(GAD\WIN\OBJ)
	Else
		GAD\TH=GAD\LINK[1]\H
		While XL_GAD<>Null
			If XL_GAD\WIN=GAD\WIN
			XL_MOVE=True
			If GAD<>XL_GAD
				If XL_GAD\TYP=GAD_PANEL
					If XL_GAD\PANEL\PAR=GAD\PANEL\PAR And XL_GAD\TAB=GAD\TAB
						If RectsOverlap(XL_GAD\X,0,XL_GAD\W,1,GAD\X,0,GAD\W,1) And XL_GAD\Y>=GAD\Y
							XL_GAD\Y=XL_GAD\Y+(GAD\TH-GAD\LINK[0]\H)
							XL_GAD\PAD[10]=XL_GAD\PAD[10]+1
							XL_GAD\PANEL\Y=XL_GAD\Y
							XL_GAD\LINK[0]\Y=XL_GAD\Y
							XL_GAD\LINK[1]\Y=XL_GAD\Y
						EndIf
					EndIf
				Else
					If XL_GAD\PANEL=GAD\PANEL\PAR And XL_GAD\TAB=GAD\TAB
						If RectsOverlap(XL_GAD\X,0,XL_GAD\W,1,GAD\X,0,GAD\W,1) And XL_GAD\Y>=GAD\Y
							If XL_GAD\PARENT<>Null
								If XL_GAD\PARENT\TYP=gad_PANEL
									XL_MOVE=False
								EndIf
							EndIf
							If XL_MOVE=True
								If XL_GAD\PARENT=Null
									XL_GAD\Y=XL_GAD\Y+(GAD\TH-GAD\LINK[0]\H)
									XL_GAD\PAD[10]=XL_GAD\PAD[10]+1
									For XL_T=0 To 3
										If XL_GAD\LINK[XL_T]<>Null
											XL_GAD\LINK[XL_T]\Y=XL_GAD\LINK[XL_T]\Y+(GAD\TH-GAD\LINK[0]\H)
											XL_GAD\LINK[XL_T]\PAD[10]=XL_GAD\LINK[XL_T]\PAD[10]+1
											For XL_TT=0 To 3
												If XL_GAD\LINK[XL_T]\LINK[XL_TT]<>Null
													XL_GAD\LINK[XL_T]\LINK[XL_TT]\Y=XL_GAD\LINK[XL_T]\LINK[XL_TT]\Y+(GAD\TH-GAD\LINK[0]\H)
													XL_GAD\LINK[XL_T]\LINK[XL_TT]\PAD[10]=XL_GAD\LINK[XL_T]\LINK[XL_TT]\PAD[10]+1
												Else
													Exit
												EndIf
											Next
										Else
											Exit
										EndIf
									Next
								EndIf
								If XL_GAD\TYP=gad_TOOLBAR
									XL_GAD\PANEL\Y=XL_GAD\Y
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			EndIf
			XL_GAD=After XL_GAD
		Wend
		GAD\X=GAD\PANEL\X
		GAD\Y=GAD\PANEL\Y
		GAD\LINK[0]\X=GAD\X:GAD\LINK[0]\Y=GAD\Y
		GAD\LINK[1]\X=GAD\X:GAD\LINK[1]\Y=GAD\Y
		GAD\ON=True
		GAD\PANEL\ACT=True
		GAD\LINK[0]\ON=True
		GAD\LINK[1]\STATUS=gad_SHOW
		If GAD\TOOL<>Null
			GUI_REFRESH_TOOLBAR(GAD\TOOL)
		EndIf
		GUI_REFRESH(GAD\WIN\OBJ)
	EndIf
End Function

Function GUI_PANEL_ATTACH(XL_GAD,XL_PANG)
	If XL_GAD<1 Return
	GAD.GAD=Object.GAD(XL_GAD)
	XL_PAN.GAD=Object.GAD(XL_PANG)
	Select GAD\TYP
		Default
			GAD\Y=GAD\Y+XL_PAN\LINK[0]\H+1
			GAD\PANEL=XL_PAN\PANEL
			GAD\TAB=XL_PAN\TAB
			For XL_T=0 To 3
				If GAD\LINK[XL_T]<>Null
					GUI_PANEL_ATTACH(GAD\LINK[XL_T]\OBJ,XL_PAN\OBJ)
				EndIf
			Next
	End Select
	Insert XL_PAN\LINK[1] Before First GAD
End Function

Function GUI_TOOLBAR_ATTACH(XL_GAD,XL_BARG)
	GAD.GAD=Object.GAD(XL_GAD)
	XL_BAR.GAD=Object.GAD(XL_BARG)
	XL_NEW.GADLST=New GADLST
	XL_NEW\PAR=XL_BAR
	XL_NEW\GAD=GAD
	XL_NEW\ID=XL_BAR\VAL
	XL_BAR\VAL=XL_BAR\VAL+1
	GAD\X2=GAD\X
	GAD\Y2=GAD\Y
	GAD\TOOL=XL_BAR
	GAD\TAB=XL_BAR\TAB
	Select GAD\TYP
		Case gad_TOOLBAR,gad_PANEL
			Return
		Default
			GAD\PANEL=XL_BAR\PANEL
			For XL_T=0 To 2
				If GAD\LINK[XL_T]<>Null
					GAD\LINK[XL_T]\PANEL=XL_BAR\PANEL
					GAD\LINK[XL_T]\TAB=XL_BAR\TAB
				EndIf
			Next
	End Select
	XL_BAR\PAD[3]=XL_BAR\PAD[3]+1
	Insert XL_BAR Before First GAD	
	GUI_TOOLBAR_SCROLL(XL_BAR,0)
End Function

Function GUI_TOOLBAR_SCROLL(XL_BAR.GAD,XL_PLUS)
	XL_BARFIRST.GADLST=Null
	XL_BARLAST.GADLST=Null
	XL_FIRST.GADLST=Null
	XL_GADLST.GADLST=First GADLST
	If XL_BAR\PAD[4]=False
		XL_PLUS=0
	EndIf
	While XL_GADLST<>Null
		If XL_GADLST\PAR=XL_BAR And XL_BARFIRST=Null
			XL_BARFIRST=XL_GADLST
		EndIf
		If XL_GADLST\PAR=XL_BAR
			XL_BARLAST=XL_GADLST
		EndIf
		XL_GADLST=After XL_GADLST
	Wend
	
	If XL_PLUS<>0 And (XL_BARFIRST<>Null And XL_BARLAST<>Null)
		If XL_PLUS<0
			Insert XL_BARFIRST After XL_BARLAST
		Else
			Insert XL_BARLAST Before XL_BARFIRST
		EndIf
	Else
		XL_PLUS=0
	EndIf
	
	XL_X=2
	XL_Y=2
	XL_GADLST.GADLST=First GADLST
	While XL_GADLST<>Null
		If XL_GADLST\PAR=XL_BAR
			If XL_BAR\PAD[0]=True
				XL_OX=XL_GADLST\GAD\X
				XL_OY=XL_GADLST\GAD\Y
				XL_GADLST\GAD\X=XL_X
				XL_X=XL_X+XL_GADLST\GAD\TW+1
				If XL_X>XL_BAR\PANEL\W
					XL_BAR\PAD[4]=True
				EndIf
				XL_GADLST\GAD\Y=(XL_BAR\PANEL\H/2)-(XL_GADLST\GAD\TH/2)
				XL_XP=XL_GADLST\GAD\X-XL_OX
				XL_YP=XL_GADLST\GAD\Y-XL_OY
				For XL_T=0 To 3
					If XL_GADLST\GAD\LINK[XL_T]<>Null
						XL_GADLST\GAD\LINK[XL_T]\X=XL_GADLST\GAD\LINK[XL_T]\X+XL_XP
						XL_GADLST\GAD\LINK[XL_T]\Y=XL_GADLST\GAD\LINK[XL_T]\Y+XL_YP
					EndIf
				Next
			Else
				XL_OX=XL_GADLST\GAD\X
				XL_OY=XL_GADLST\GAD\Y
				XL_GADLST\GAD\Y=XL_Y
				XL_Y=XL_Y+XL_GADLST\GAD\TH+1
				If XL_Y>XL_BAR\H
					XL_BAR\PAD[4]=True
				EndIf
				XL_GADLST\GAD\X=(XL_BAR\PANEL\W/2)-(XL_GADLST\GAD\TW/2)
				XL_XP=XL_GADLST\GAD\X-XL_OX
				XL_YP=XL_GADLST\GAD\Y-XL_OY
				For XL_T=0 To 2
					If XL_GADLST\GAD\LINK[XL_T]<>Null
						XL_GADLST\GAD\LINK[XL_T]\X=XL_GADLST\GAD\LINK[XL_T]\X+XL_XP
						XL_GADLST\GAD\LINK[XL_T]\Y=XL_GADLST\GAD\LINK[XL_T]\Y+XL_YP
					Else
						Exit
					EndIf
				Next
			EndIf
		EndIf
		XL_GADLST=After XL_GADLST
	Wend
	If XL_PLUS<>0
		GUI_REFRESH(XL_BAR\WIN\OBJ)	
	EndIf
End Function

Function GUI_REFRESH_TOOLBAR(XL_BAR.GAD)
	GUI_TOOLBAR_SCROLL(XL_BAR,0)
End Function