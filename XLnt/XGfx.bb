Function GUI_COL(XL_COL)
	;SETS COLOR TO PASSED RGB VALUE
	Color 0,0,XL_COL
End Function

Function GUI_RGB(XL_R,XL_G,XL_B)
	Return (XL_R Shl 16)+(XL_G Shl 8)+XL_B
End Function

Function GUI_CLSCOL(XL_COL)
	;SETS COLOR TO PASSED RGB VALUE
	XL_R=(XL_COL Shr 16) And $FF
	XL_G=(XL_COL Shr 8) And $FF
	XL_B=XL_COL And $FF
	ClsColor XL_R,XL_G,XL_B
End Function

Function GUI_RGB_SHADE(XL_COL,XL_PLUS)
	;ALTERS AN RGB BY PASSED SHADE AMOUNT
	XL_R=(XL_COL Shr 16) And $FF
	XL_G=(XL_COL Shr 8) And $FF
	XL_B=XL_COL And $FF
	XL_R=QLIMIT(XL_R+XL_PLUS,0,255)
	XL_G=QLIMIT(XL_G+XL_PLUS,0,255)
	XL_B=QLIMIT(XL_B+XL_PLUS,0,255)
	XL_COL=(XL_R Shl 16)+(XL_G Shl 8)+XL_B
	Return XL_COL
End Function

Function GUI_TRUECOL(xl_COL)
	xl_IMG=CreateImage(1,1)
	WritePixel 0,0,xl_COL,ImageBuffer(xl_IMG)
	RET=ReadPixel(0,0,ImageBuffer(xl_IMG)) And $FFFFFF
	FreeImage xl_IMG
	Return RET
End Function

Function GUI_ICON_MASK(XL_NAME$,XL_MASK)
	XL_ICON.ICON=GUI_FINDICON(XL_NAME$)
	If XL_ICON=Null Return
	For XL_T=0 To 3
		If XL_ICON\IMG[XL_T]>0
			MaskImage XL_ICON\IMG[XL_T],(XL_MASK Shr 16) And $FF,(XL_MASK Shr 8) And $FF,XL_MASK And $FF
		EndIf
	Next
End Function

Function GUI_GFXSETUP()
	GUI_GFXW=GraphicsWidth()
	GUI_GFXH=GraphicsHeight()
	
	Restore FONT_DATA
	Read XL_MAXCHAR
	For XL_T=0 To XL_MAXCHAR-1
		XL_CHAR.CHAR=New CHAR
		Read XL_CHAR\ID
		Read XL_CHAR\X
		Read XL_CHAR\Y
		Read XL_CHAR\W
		Read XL_CHAR\H
		XL_CHAR\A$=Chr$(XL_CHAR\ID)
		GUI_CHAR(XL_CHAR\ID)=XL_CHAR
	Next
			
	GUI_FONTIMG_ALPHA=LoadImage(PAK("FONT.PNG"))
	GUI_FONTIMG_BETA=CopyImage(GUI_FONTIMG_ALPHA)
	GUI_FONTIMG_INV=LoadImage(PAK("INVFONT.PNG"))
	GUI_FONTIMG_INVBETA=CopyImage(GUI_FONTIMG_INV)

	MaskImage GUI_FONTIMG_ALPHA,255,0,255
	MaskImage GUI_FONTIMG_BETA,0,0,0
	MaskImage GUI_FONTIMG_INV,255,0,255
	MaskImage GUI_FONTIMG_INVBETA,0,0,0
		
	icn_HSLIDER=LoadImage(PAK("gad_HSLIDER.PNG")):MaskImage icn_HSLIDER,255,0,255
	icn_VSLIDER=LoadImage(PAK("gad_VSLIDER.PNG")):MaskImage icn_VSLIDER,255,0,255
	icn_DEFAULT=Object.ICON(GUI_GADICON("DEFAULT",LoadImage(PAK("ICON_DEFAULT.PNG"))))
	icn_TICKBOX=Object.ICON(GUI_GADICON("TICK BOX",LoadImage(PAK("GAD_BOX_TICK.PNG")),2))
	icn_CHKBOX=Object.ICON(GUI_GADICON("CHECK BOX",LoadImage(PAK("GAD_BOX_CROSS.PNG")),2))
	icn_LARROW=Object.ICON(GUI_GADICON("icn_LARROW",LoadImage(PAK("GAD_BLK_AL.PNG"))))
	icn_RARROW=Object.ICON(GUI_GADICON("icn_RARROW",LoadImage(PAK("GAD_BLK_AR.PNG"))))
	icn_UARROW=Object.ICON(GUI_GADICON("icn_UARROW",LoadImage(PAK("GAD_BLK_AU.PNG"))))
	icn_DARROW=Object.ICON(GUI_GADICON("icn_DARROW",LoadImage(PAK("GAD_BLK_AD.PNG"))))
	icn_INCARROW=Object.ICON(GUI_GADICON("icn_INCARROW",LoadImage(PAK("GAD_BLK_AU2.PNG"))))
	icn_DECARROW=Object.ICON(GUI_GADICON("icn_DECARROW",LoadImage(PAK("GAD_BLK_AD2.PNG"))))
	icn_LISTDIR=Object.ICON(GUI_GADICON("OPEN DIR",LoadImage(PAK("GAD_DIR.PNG")),4))
	
	icn_MIN=Object.ICON(GUI_GADICON("icn_MIN",LoadImage(PAK("BUT_MIN.PNG")),2))
	icn_BACK=Object.ICON(GUI_GADICON("icn_BACK",LoadImage(PAK("BUT_BACK.PNG")),2))
	icn_QUIT=Object.ICON(GUI_GADICON("icn_QUIT",LoadImage(PAK("BUT_QUIT.PNG")),2))
	icn_PANEL=Object.ICON(GUI_GADICON("icn_PANEL",LoadImage(PAK("GAD_PANEL.PNG")),4))
	
	GUI_GADICON("io_DRIVE",LoadImage(PAK("ICON_HD.PNG")))
	GUI_GADICON("io_TEXT",LoadImage(PAK("ICON_TEXT.PNG")))
	GUI_GADICON("io_IMAGE",LoadImage(PAK("ICON_IMG.PNG")))
	GUI_GADICON("io_FILE",LoadImage(PAK("ICON_FILE.PNG")))
	GUI_GADICON("io_BLITZ",LoadImage(PAK("ICON_BLITZ.PNG")))

	GUI_GADICON("io_FOLDER",LoadImage(PAK("ICON_FOLDER.PNG")),2)
	GUI_GADICON("io_PARENT",LoadImage(PAK("ICON_PARENT.PNG")),2)
	
	GUI_IMG_SCALE=LoadImage(PAK("BUT_SIZE.PNG")):MaskImage GUI_IMG_SCALE,255,0,255
	GUI_IMG_SCALE1=LoadImage(PAK("BUT_SIZE1.PNG")):MaskImage GUI_IMG_SCALE1,255,0,255

	GUI_MOUSE_POINTER=LoadImage(PAK("MOUSE_POINTER.PNG")):MaskImage GUI_MOUSE_POINTER,255,0,255
	GUI_MOUSE_HSLIDER=LoadImage(PAK("MOUSE_HSLIDER.PNG")):MaskImage GUI_MOUSE_HSLIDER,255,0,255:MidHandle GUI_MOUSE_HSLIDER
	GUI_MOUSE_VSLIDER=LoadImage(PAK("MOUSE_VSLIDER.PNG")):MaskImage GUI_MOUSE_VSLIDER,255,0,255:MidHandle GUI_MOUSE_VSLIDER
	GUI_MOUSE_WDRAG=LoadImage(PAK("MOUSE_DRAG.PNG")):MaskImage GUI_MOUSE_WDRAG,255,0,255:MidHandle GUI_MOUSE_WDRAG
	GUI_MOUSE_WSCALE=LoadImage(PAK("MOUSE_SCALE.PNG")):MaskImage GUI_MOUSE_WSCALE,255,0,255;MidHandle GUI_MOUSE_WSCALE
	
	GUI_WINBORDER=$7682BC
	GUI_WINCOL=$DADADA
	GUI_WINHILITE=GUI_RGB_SHADE(GUI_WINCOL,32)
	GUI_WINLOLITE=GUI_RGB_SHADE(GUI_WINCOL,-40)
	GUI_GADCOL=GUI_RGB_SHADE(GUI_WINCOL,20)
	GUI_GADHILITE=GUI_RGB_SHADE(GUI_WINCOL,32)
	GUI_GADLOLITE=GUI_RGB_SHADE(GUI_WINCOL,-16)
	GUI_WINTXTCOL=$FFFFFF
	
	GUI_SHADOW=CreateImage(2,2)
	SetBuffer ImageBuffer(GUI_SHADOW)
	Color 255,0,255
	Plot 0,0
	Plot 1,1
	Color 0,0,0
	Plot 0,1
	Plot 1,0
	MaskImage GUI_SHADOW,255,0,255
	
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0:GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_GFXH
End Function

Function GUI_GRADRECT(xl_X,xl_Y,xl_W,xl_H,xl_C1,xl_C2,xl_HORZ=False)
	xl_R#=(xl_C1 Shr 16) And $FF
	xl_G#=(xl_C1 Shr 8) And $FF
	xl_B#=xl_C1 And $FF
	xl_RP#=xl_R-((xl_C2 Shr 16) And $FF)
	xl_GP#=xl_G-((xl_C2 Shr 8) And $FF)
	xl_BP#=xl_B-(xl_C2 And $FF)
	If XL_HORZ=False
		xl_RP#=xl_RP#/(xl_H-1)
		xl_GP#=xl_GP#/(xl_H-1)
		xl_BP#=xl_BP#/(xl_H-1)
		For xl_YY=xl_Y To xl_Y+xl_H-1
			Color xl_R,xl_G,Xl_B
			Rect xl_X,xl_YY,xl_W,1
			xl_R=FLIMIT(xl_R-xl_RP,0,255)
			xl_G=FLIMIT(xl_G-xl_GP,0,255)
			xl_B=FLIMIT(xl_B-xl_BP,0,255)
		Next
	Else
		xl_RP#=xl_RP#/(xl_W-1)
		xl_GP#=xl_GP#/(xl_W-1)
		xl_BP#=xl_BP#/(xl_W-1)
		For xl_XX=xl_X To xl_X+xl_W-1
			Color xl_R,xl_G,Xl_B
			Rect xl_XX,xl_Y,1,xl_H
			xl_R=FLIMIT(xl_R-xl_RP,0,255)
			xl_G=FLIMIT(xl_G-xl_GP,0,255)
			xl_B=FLIMIT(xl_B-xl_BP,0,255)
		Next
	EndIf
End Function

Function GUI_GFXBOX(XL_X,XL_Y,XL_W,XL_H,XL_COL0,XL_COL1,XL_COL2,XL_OLINE=True,XL_INV=False,XL_GRAD=False,XL_COL3=$FF00FF,XL_FILL=True,XL_COLPLUS=0,xl_HORZ=False,XL_SKIN=0)
	If XL_SKIN=0
		If XL_COL3=$FF00FF
			XL_COL3=XL_COL0
		EndIf
		If XL_COLPLUS<>0
			XL_COL0=GUI_RGB_SHADE(XL_COL0,XL_COLPLUS)
			XL_COL1=GUI_RGB_SHADE(XL_COL1,XL_COLPLUS)
			XL_COL2=GUI_RGB_SHADE(XL_COL2,XL_COLPLUS)
			XL_COL3=GUI_RGB_SHADE(XL_COL3,XL_COLPLUS)
		EndIf
		If XL_OLINE=True
			Color 0,0,0
			Rect XL_X,XL_Y,XL_W,XL_H,0
			XL_X=XL_X+1
			XL_Y=XL_Y+1
			XL_W=XL_W-2
			XL_H=XL_H-2
		EndIf
		If XL_INV=True
			GUI_COL(XL_COL2)
			Rect XL_X,XL_Y,XL_W-1,1
			Rect XL_X,XL_Y,1,XL_H-1
			GUI_COL(XL_COL1)
			Rect XL_X,XL_Y+XL_H-1,XL_W,1
			Rect XL_X+XL_W-1,XL_Y,1,XL_H
		Else
			GUI_COL(XL_COL1)
			Rect XL_X,XL_Y,XL_W-1,1
			Rect XL_X,XL_Y,1,XL_H-1
			GUI_COL(XL_COL2)
			Rect XL_X,XL_Y+XL_H-1,XL_W,1
			Rect XL_X+XL_W-1,XL_Y,1,XL_H
		EndIf
		
		XL_X=XL_X+1:XL_W=XL_W-2
		XL_Y=XL_Y+1:XL_H=XL_H-2
		
		If XL_COL0=XL_COL3
			XL_GRAD=False
		EndIf
		
		If XL_FILL=True
			If XL_GRAD=False
				GUI_COL(XL_COL0)
				Rect XL_X,XL_Y,XL_W,XL_H
			Else
				If XL_INV=False
					GUI_GRADRECT(XL_X,XL_Y,XL_W,XL_H,XL_COL0,XL_COL3,xl_HORZ)
				Else
					GUI_GRADRECT(XL_X,XL_Y,XL_W,XL_H,XL_COL3,XL_COL0,xl_HORZ)
				EndIf
			EndIf
		EndIf
	Else
		;CopyRect 0,0,XL_W,XL_H,XL_X,XL_Y,ImageBuffer(XL_SKIN)
		TileBlock XL_SKIN,XL_X,XL_Y
	EndIf
End Function

Function GUI_DRAWWIN(WIN.WIN)
	SetBuffer ImageBuffer(WIN\IMG)
	GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=WIN\W:GUI_VPH=WIN\H
	Color 0,0,0
	Rect 0,0,WIN\W,WIN\H
		
	XL_SKIN=0
	If WIN\TAB>0
		If WIN\SKIN[WIN\TAB]<>Null
			XL_SKIN=WIN\TAB
		EndIf
	EndIf
	If WIN\SKIN[XL_SKIN]<>Null And WIN\STATUS=win_OPEN
		XL_FILL=False
		If WIN\TITLE$<>""
			XL_YOFF=20
		EndIf
		If GUI_FLAG(WIN\FLAG,flg_MENU)
			XL_YOFF=XL_YOFF+22
		EndIf
		If WIN\SKIN[XL_SKIN]\FLAG=True
			WIN\W=ImageWidth(WIN\SKIN[XL_SKIN]\IMG[0])+2
			WIN\H=ImageHeight(WIN\SKIN[XL_SKIN]\IMG[0])+XL_YOFF+2
			WIN\FLAG=WIN\FLAG And %11011111
		EndIf
		FreeImage WIN\IMG
		WIN\IMG=CreateImage(WIN\W,WIN\H)
		SetBuffer ImageBuffer(WIN\IMG)
		GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=WIN\W:GUI_VPH=WIN\H
		Color 0,0,0
		Rect 0,0,WIN\W,WIN\H
		Viewport 1,1,WIN\W-2,WIN\H-2
		Origin 0,0
		TileBlock WIN\SKIN[XL_SKIN]\IMG[0],0,XL_YOFF
		Viewport 0,0,WIN\W,WIN\H
	Else
		XL_FILL=True
	EndIf
	
	XL_X=1:XL_Y=1:XL_W=WIN\W-2:XL_H=WIN\H-2
	WIN\PANEL\W=WIN\W
	WIN\PANEL\H=WIN\H
	WIN\PANEL\X=0
	WIN\PANEL\Y=0
	XL_TITLEH=20
	XL_WINICON=0
	If GUI_FLAG(WIN\FLAG,flg_QUIT)
		XL_WINICON=XL_WINCON+1
	EndIf
	If GUI_FLAG(WIN\FLAG,flg_BACK)
		XL_WINICON=XL_WINICON+1
	EndIf
	If GUI_FLAG(WIN\FLAG,flg_MIN)
		XL_WINICON=XL_WINICON+1
	EndIf
	
	If XL_WINICON=0
		XL_TITW=WIN\W-2
	Else
		XL_TITW=WIN\W-((XL_WINICON*17)+5)
	EndIf
	
	If GUI_FLAG(WIN\FLAG,flg_BORDER)=True And XL_BORDER=True
		GUI_GFXBOX(XL_X+4,XL_Y,XL_W-8,4,WIN\BCOL[0],WIN\BCOL[1],WIN\BCOL[2],False,False,False)
		GUI_GFXBOX(XL_X+4,XL_Y+XL_H-4,XL_W-8,4,WIN\WCOL[3],WIN\WCOL[1],WIN\WCOL[2],False,False,False)
		If WIN\TITLE$<>""
			GUI_GFXBOX(XL_X,XL_Y+XL_TITLEH,4,XL_H-XL_TITLEH,WIN\BCOL[0],WIN\BCOL[1],WIN\BCOL[2],False,True,WIN\WCOL[3])
			GUI_GFXBOX(XL_X+XL_W-4,XL_Y+XL_TITLEH,4,XL_H-XL_TITLEH,WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,True,WIN\WCOL[3])
			GUI_GFXBOX(XL_X,XL_Y,XL_TITW,XL_TITLEH,WIN\BCOL[0],WIN\BCOL[1],WIN\BCOL[2],False,False,True,WIN\BCOL[3])
			GUI_GFXBOX(XL_X+4,XL_Y+XL_TITLEH,XL_W-8,XL_H-(XL_TITLEH+4),WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,GUI_FLAG(WIN\FLAG,flg_GRAD),WIN\WCOL[3])
		Else
			GUI_GFXBOX(XL_X,XL_Y,4,XL_H,WIN\BCOL[0],WIN\BCOL[1],WIN\BCOL[2],False,False,True,WIN\WCOL[3])
			GUI_GFXBOX(XL_X+XL_W-4,XL_Y,4,XL_H,WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,True,WIN\WCOL[3])
			GUI_GFXBOX(XL_X+4,XL_Y+4,XL_W-8,XL_H-8,WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,GUI_FLAG(WIN\FLAG,flg_GRAD),WIN\WCOL[3])
		EndIf
	Else
		If WIN\TITLE$<>""
			GUI_GFXBOX(XL_X,XL_Y,XL_TITW,XL_TITLEH,WIN\BCOL[0],WIN\BCOL[1],WIN\BCOL[2],False,False,True,WIN\BCOL[3],True,0,False);True)
			If WIN\H>22
				GUI_GFXBOX(XL_X,XL_Y+XL_TITLEH+1,XL_W,XL_H-(XL_TITLEH+1),WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,GUI_FLAG(WIN\FLAG,flg_GRAD),WIN\WCOL[3],XL_FILL,0,False)
			EndIf
		Else
			GUI_GFXBOX(XL_X,XL_Y,XL_W,XL_H,WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,GUI_FLAG(WIN\FLAG,flg_GRAD),WIN\WCOL[3],XL_FILL,0,False)
		EndIf
	EndIf
	
	XL_FILL=True
	
	If WIN\TITLE$<>""
		XL_TX=3:XL_TY=4:XL_TW=XL_TITW-6
		If WIN\ICON<>Null
			DrawImage WIN\ICON\IMG[0],3,3
			XL_TW=XL_TW-(WIN\ICON\W+4)
			XL_TX=XL_TX+18
		EndIf
		GUI_FONTCOL(~WIN\TITLE_COL)
		GUI_TEXT(WIN\TITLE$,XL_TX+2,XL_TY+1,XL_TW,14)
		GUI_FONTCOL(WIN\TITLE_COL)
		GUI_TEXT(WIN\TITLE$,XL_TX+1,XL_TY,XL_TW,14)
	EndIf
	
	If XL_WINICON>0 And WIN\TITLE$<>""
		GUI_GFXBOX(XL_X+XL_TITW,XL_Y,XL_W-XL_TITW,XL_TITLEH,WIN\WCOL[0],WIN\WCOL[1],WIN\WCOL[2],False,False,True,WIN\WCOL[3],XL_FILL)
		XL_IX=(WIN\W-2)
		If GUI_FLAG(WIN\FLAG,flg_MIN)
			If WIN\gad_MIN<>Null
				WIN\gad_MIN\X=XL_IX-(17*XL_WINICON)
			Else
				WIN\gad_MIN=Object.GAD(GUI_IMGSWITCH(WIN\OBJ,XL_IX-(17*XL_WINICON),3,"icn_MIN"))
			EndIf
			XL_WINICON=XL_WINICON-1
		EndIf
		If GUI_FLAG(WIN\FLAG,flg_BACK)
			If WIN\gad_BACK<>Null
				WIN\gad_BACK\X=XL_IX-(17*XL_WINICON)
			Else
				WIN\gad_BACK=Object.GAD(GUI_IMGBUTTON(WIN\OBJ,XL_IX-(17*XL_WINICON),3,"icn_BACK"))
			EndIf
			XL_WINICON=XL_WINICON-1
		EndIf
		If GUI_FLAG(WIN\FLAG,flg_QUIT)
			If WIN\gad_QUIT<>Null
				WIN\gad_QUIT\X=XL_IX-(17*XL_WINICON)
			Else
				WIN\gad_QUIT=Object.GAD(GUI_IMGBUTTON(WIN\OBJ,XL_IX-(17*XL_WINICON),3,"icn_QUIT"))
			EndIf
			XL_WINICON=XL_WINICON-1
		EndIf

	EndIf
	
	If GUI_FLAG(WIN\FLAG,flg_MENU)
		If WIN\TITLE$<>""
			XL_X=0:XL_Y=21:XL_W=win\w:XL_H=22
		Else
			XL_X=0:XL_Y=0:XL_W=win\w:XL_H=22
		EndIf
		If GUI_FLAG(WIN\FLAG,flg_BORDER)=False Or XL_BORDER=False
			GUI_GFXBOX(XL_X,XL_Y,XL_W,XL_H,WIN\WCOL[3],WIN\WCOL[1],WIN\WCOL[2],True,False,True,WIN\WCOL[0],XL_FILL,16)
			WIN\MENUX=XL_X+2
			WIN\MENUY=XL_Y+2
		Else
			GUI_GFXBOX(XL_X+5,XL_Y,XL_W-10,XL_H,WIN\WCOL[3],WIN\WCOL[1],WIN\WCOL[2],True,False,True,WIN\WCOL[0],XL_FILL,16)
			WIN\MENUX=XL_X+7
			WIN\MENUY=XL_Y+2
		EndIf
	EndIf	
	
	If WIN\GRID>0 And WIN\IMG>0
		LockBuffer ImageBuffer(WIN\IMG)
		XL_X=0
		XL_Y=0
		Repeat
			Repeat
				WritePixelFast XL_X,XL_Y,~WIN\WCOL[0],ImageBuffer(WIN\IMG)
				XL_X=XL_X+WIN\GRID
			Until XL_X>WIN\W-1
			XL_X=0
			XL_Y=XL_Y+WIN\GRID
		Until XL_Y>WIN\H-1
		UnlockBuffer ImageBuffer(WIN\IMG)
	EndIf
	
	;GUI_GFXBOX(XL_X+10,XL_Y+60,12,12,WIN\WCOL[3],WIN\WCOL[1],WIN\WCOL[2],True,False,True,WIN\WCOL[0])
	If WIN\IMG2>0
		FreeImage WIN\IMG2
	EndIf
	WIN\IMG2=CopyImage(WIN\IMG)
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0:GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_GFXH
End Function

Function GUI_DRAWTAB(GAD.GAD,XL_XP=0,XL_YP=0,XL_TXTXP=0,XL_TXTYP=0)
	XL_X=GAD\X+XL_XP
	XL_Y=GAD\Y+XL_YP
	XL_W=GAD\W
	XL_H=GAD\H
	
	XL_SKIN=0
	
	If GAD\WIN\TAB>0
		If GAD\WIN\SKIN[GAD\WIN\TAB]<>Null
			XL_SKIN=GAD\WIN\TAB
		EndIf
	EndIf
		
	If GAD\WIN\SKIN[XL_SKIN]<>Null
		XL_FILL=False
		XL_IMG=CreateImage(XL_W-4,3)
		If GAD\WIN\TITLE$<>""
			XL_YOFF=20
		EndIf
		If GUI_FLAG(GAD\WIN\FLAG,flg_MENU)
			XL_YOFF=XL_YOFF+22
		EndIf
	Else
		XL_FILL=True
	EndIf
	
	If GAD\PAD[1]=True
		If GAD\CAP$<>""
			Select GAD\PAD[2]
				Case 0			
					If GAD\ON=True
						GUI_GFXBOX(XL_X+1,XL_Y+1,XL_W-2,XL_H,GAD\COL[0],GAD\COL[1],GAD\COL[2],False,False,False,GAD\COL[0],XL_FILL)
						Color 0,0,0
						Rect XL_X+2,XL_Y,XL_W-4,1
						Rect XL_X,XL_Y+2,1,XL_H-2
						Rect XL_X+XL_W-1,XL_Y+2,1,XL_H-2
						Rect XL_X+1,XL_Y+1,1,1
						Rect XL_X+XL_W-2,XL_Y+1,1,1
						GUI_COL(GAD\COL[1])
						Rect XL_X+1,XL_Y+2,1,XL_H-1
						GUI_COL(GAD\COL[0])
						If XL_FILL=True
							Rect XL_X+2,XL_Y+XL_H-2,XL_W-4,3
						Else
							SetBuffer ImageBuffer(GAD\WIN\IMG2)
							GrabImage XL_IMG,XL_X+2,XL_Y+XL_H-2
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(GAD)
							DrawBlock XL_IMG,XL_X+2,XL_Y+XL_H-2
							FreeImage XL_IMG
						EndIf
						;Rect XL_X,XL_Y+XL_H-1,XL_W,1
					Else
						XL_H=XL_H-2
						XL_Y=XL_Y+2
						GUI_GFXBOX(XL_X+1,XL_Y+1,XL_W-2,XL_H-2,GAD\COL[0],GAD\COL[1],GAD\COL[2],False,False,False,GAD\COL[3],XL_FILL,-8)
						Color 0,0,0
						Rect XL_X+2,XL_Y,XL_W-4,1
						Rect XL_X,XL_Y+2,1,XL_H-2
						Rect XL_X+XL_W-1,XL_Y+2,1,XL_H-2
						Rect XL_X+1,XL_Y+1,1,1
						Rect XL_X+XL_W-2,XL_Y+1,1,1
						Rect XL_X,XL_Y+XL_H-1,XL_W,1
					EndIf
				Default
					;XL_H=XL_H-1
					If GAD\ON=True
						GUI_GFXBOX(XL_X+1,XL_Y-1,XL_W-2,XL_H,GAD\COL[0],GAD\COL[1],GAD\COL[2],False,False,False,GAD\COL[0],XL_FILL)
						Color 0,0,0
						Rect XL_X,XL_Y-1,1,XL_H-1
						Rect XL_X+2,XL_Y+XL_H-1,XL_W-4,1
						Rect XL_X+XL_W-1,XL_Y-1,1,XL_H-1
						Rect XL_X+1,XL_Y+XL_H-2,1,1
						Rect XL_X+XL_W-2,XL_Y+XL_H-2,1,1
						
						
						GUI_COL(GAD\COL[1])
						Rect XL_X+1,XL_Y-2,1,1
						GUI_COL(GAD\COL[0])
						If XL_FILL=True
							Rect XL_X+2,XL_Y-2,XL_W-4,3
						Else
							SetBuffer ImageBuffer(GAD\WIN\IMG2)
							GrabImage XL_IMG,XL_X+2,XL_Y-2
							SetBuffer ImageBuffer(GAD\WIN\IMG)
							GUI_GAD_BUFFER(GAD)
							DrawBlock XL_IMG,XL_X+2,XL_Y-2
							FreeImage XL_IMG
						EndIf
						GUI_COL(GAD\COL[2])
						Rect XL_X+XL_W-2,XL_Y-2,1,1
					Else
						XL_H=XL_H-2
						XL_Y=XL_Y-1
						GUI_GFXBOX(XL_X+1,XL_Y,XL_W-2,XL_H-1,GAD\COL[0],GAD\COL[1],GAD\COL[2],False,False,False,GAD\COL[3],XL_FILL,-8)
						Color 0,0,0
						Rect XL_X,XL_Y-1,1,XL_H-1
						Rect XL_X+2,XL_Y+XL_H-1,XL_W-4,1
						Rect XL_X+XL_W-1,XL_Y-1,1,XL_H-1
						Rect XL_X+1,XL_Y+XL_H-2,1,1
						Rect XL_X+XL_W-2,XL_Y+XL_H-2,1,1
					EndIf
			End Select	
		EndIf
	EndIf	
	
	XL_X=XL_X+XL_TXTXP
	XL_Y=XL_Y+XL_TXTYP
	XL_W=XL_W-XL_TXTXP
	XL_H=XL_H-XL_TXTYP
		
	If GAD\ICON<>Null
		If GAD\CAP$<>""
			If GAD\ACTIVE=True
				DrawImage GAD\ICON\IMG[GAD\ON],XL_X+3,(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			Else
				DrawImage GAD\ICON\IMG[3],XL_X+3,(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			EndIf
		Else
			If GAD\ACTIVE=True
				DrawImage GAD\ICON\IMG[GAD\ON],(XL_X+(GAD\W/2))-(GAD\ICON\W/2),(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			Else
				DrawImage GAD\ICON\IMG[3],(XL_X+(GAD\W/2))-(GAD\ICON\W/2),(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			EndIf
		EndIf
		XL_X=XL_X+GAD\ICON\W+1
		XL_W=XL_W-(GAD\ICON\W+2)
	EndIf
	
	XL_Y=(XL_YP+XL_TXTYP)
	XL_Y=XL_Y+(GAD\Y+(GAD\H/2))-(GUI_STRINGHEIGHT(GAD\CAP$)/2)
	XL_Y=XL_Y-3
	If GAD\ON=False And GAD\PAD[1]=True And GAD\PAD[2]=0
		XL_Y=XL_Y+1
	Else
		If GAD\PAD[1]=True And GAD\PAD[2]<>0
			XL_Y=XL_Y-(2+(1-GAD\ON))
		EndIf
	EndIf
	
	If GAD\ACTIVE=True
		GUI_FONTCOL(GAD\TXT_COL)
		If GAD\ON=True
			GUI_TEXT(GAD\CAP$,XL_X+2,XL_Y+3,XL_W-4,14,1,False)
		Else	
			GUI_TEXT(GAD\CAP$,XL_X+2,XL_Y+3,XL_W-4,14,1,False)
		EndIf
	Else
		GUI_FONTCOL(GUI_RGB_SHADE(GAD\COL[0],64))
		GUI_TEXT(GAD\CAP$,XL_X+3,XL_Y+4,XL_W-4,14,1,False)
		GUI_FONTCOL(GUI_RGB_SHADE(GAD\COL[0],-64))
		GUI_TEXT(GAD\CAP$,XL_X+2,XL_Y+3,XL_W-4,14,1,False)
	EndIf
	
	If XL_IMG>0
		FreeImage XL_IMG
	EndIf
End Function

Function GUI_DRAWBUTTON(GAD.GAD,XL_INV=False,XL_XP=0,XL_YP=0,XL_TYP=gad_STYLE,xl_TXTXP=0,XL_TXTYP=0)
	XL_X=GAD\X+XL_XP
	XL_Y=GAD\Y+XL_YP
	XL_W=GAD\W
	XL_H=GAD\H
	If XL_INV=0
		GUI_GFXBOX(XL_X+1,XL_Y+1,XL_W-2,XL_H-2,GAD\COL[0],GAD\COL[1],GAD\COL[2],False,XL_INV,True,GAD\COL[3],True,0)
	Else
		GUI_GFXBOX(XL_X+1,XL_Y+1,XL_W-2,XL_H-2,GAD\COL[3],GAD\COL[1],GAD\COL[2],False,XL_INV,True,GAD\COL[0],True,-20)
	EndIf	
	Select XL_TYP
		Case 0
			Color 0,0,0
			Rect XL_X+2,XL_Y,XL_W-4,1
			Rect XL_X+2,XL_Y+XL_H-1,XL_W-4,1
			Rect XL_X,XL_Y+2,1,XL_H-4
			Rect XL_X+XL_W-1,XL_Y+2,1,XL_H-4
			Rect XL_X+1,XL_Y+1,1,1
			Rect XL_X+XL_W-2,XL_Y+1,1,1
			Rect XL_X+1,XL_Y+XL_H-2,1,1
			Rect XL_X+XL_W-2,XL_Y+XL_H-2,1,1
		Case 1
			Color 0,0,0
			Rect XL_X,XL_Y,XL_W,XL_H,0
		Case 2
			Color 0,0,0
			Rect XL_X+2,XL_Y,XL_W-4,1
			Rect XL_X,XL_Y+2,1,XL_H-2
			Rect XL_X+XL_W-1,XL_Y+2,1,XL_H-2
			Rect XL_X+1,XL_Y+1,1,1
			Rect XL_X+XL_W-2,XL_Y+1,1,1
			Rect XL_X,XL_Y+XL_H-1,XL_W,1
	End Select
	
	XL_X=XL_X+XL_TXTXP
	XL_Y=XL_Y+XL_TXTYP
	XL_W=XL_W-XL_TXTXP
	XL_H=XL_H-XL_TXTYP
		
	If GAD\ICON<>Null
		If GAD\CAP$<>""
			If GAD\ACTIVE=True
				DrawImage GAD\ICON\IMG[GAD\ON],XL_X+3,(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			Else
				DrawImage GAD\ICON\IMG[3],XL_X+3,(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			EndIf
		Else
			If GAD\ACTIVE=True
				DrawImage GAD\ICON\IMG[GAD\ON],(XL_X+(GAD\W/2))-(GAD\ICON\W/2),(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			Else
				DrawImage GAD\ICON\IMG[3],(XL_X+(GAD\W/2))-(GAD\ICON\W/2),(XL_Y+(GAD\H/2))-(GAD\ICON\H/2)
			EndIf
		EndIf
		XL_X=XL_X+GAD\ICON\W+1
		XL_W=XL_W-(GAD\ICON\W+2)
	EndIf
	
	XL_Y=(XL_YP+XL_TXTYP)
	XL_Y=XL_Y+(GAD\Y+(GAD\H/2))-(GUI_STRINGHEIGHT(GAD\CAP$)/2)
	XL_Y=XL_Y-3
	
	If GAD\ACTIVE=True
		GUI_FONTCOL(GAD\TXT_COL)
		If XL_INV=True
			GUI_TEXT(GAD\CAP$,XL_X+3,XL_Y+3,XL_W-4,14,1,False)
		Else	
			GUI_TEXT(GAD\CAP$,XL_X+2,XL_Y+3,XL_W-4,14,1,False)
		EndIf
	Else
		GUI_FONTCOL(GUI_RGB_SHADE(GAD\COL[0],64))
		GUI_TEXT(GAD\CAP$,XL_X+3,XL_Y+4,XL_W-4,14,1,False)
		GUI_FONTCOL(GUI_RGB_SHADE(GAD\COL[0],-64))
		GUI_TEXT(GAD\CAP$,XL_X+2,XL_Y+3,XL_W-4,14,1,False)
	EndIf
	
End Function

Function GUI_DRAWICON(ICON.ICON,XL_X,XL_Y,XL_ON,XL_ACT=True)
	If XL_ACT=False
		DrawImage ICON\IMG[3],XL_X,XL_Y
	Else
		If ICON\IMG[XL_ON]>0
			DrawImage ICON\IMG[XL_ON],XL_X,XL_Y
		Else
			DrawImage ICON\IMG[0],XL_X,XL_Y
		EndIf
	EndIf
End Function

Function GUI_GAD_BUFFER(GAD.GAD,XL_X=0,XL_Y=0)
	WIN.WIN=GAD\WIN
	
	If GAD\PANEL\ACT=True
		If GAD\PANEL\PAR=Null 
			Viewport XL_X+2,XL_Y+2,WIN\W-4,WIN\H-4
			Origin 0,0
			GUI_VPX=XL_X+2
			GUI_VPY=XL_Y+2
			GUI_VPW=WIN\W-4
			GUI_VPH=WIN\H-4
			GUI_OX=0
			GUI_OY=0
		Else
			PANEL.PANEL=GAD\PANEL
			XL_VW=WIN\W-2
			XL_VH=WIN\H-2
			XL_X2=XL_VW
			XL_Y2=XL_VH
			While PANEL<>Null
				XL_VX=XL_VX+PANEL\X
				XL_VY=XL_VY+PANEL\Y
				If PANEL\X+PANEL\W<XL_X2
					XL_X2=PANEL\X+PANEL\W
					XL_VW=PANEL\W
				EndIf
				If PANEL\Y+PANEL\H<XL_Y2
					XL_Y2=PANEL\Y+PANEL\H
					XL_VH=PANEL\H
				EndIf
				PANEL=PANEL\PAR
			Wend
			XL_X1=QLIMIT(XL_VX,0,WIN\W-2)
			XL_Y1=QLIMIT(XL_VY,0,WIN\H-2)
			XL_X2=QLIMIT(XL_VW,0,WIN\W-XL_X1)
			XL_Y2=QLIMIT(XL_VH,0,WIN\H-XL_Y1)
			Viewport XL_X+XL_X1+2,XL_Y+XL_Y1+2,XL_X2-4,XL_Y2-4
			Origin XL_VX,XL_VY
			GUI_VPX=XL_X+XL_X1+2
			GUI_VPY=XL_Y+XL_Y1+2
			GUI_VPW=XL_X2-4
			GUI_VPH=XL_Y2-4
			GUI_OX=XL_VX
			GUI_OY=XL_VY
		EndIf
	Else
		Viewport 0,0,0,0
		Origin 0,0
		GUI_VPX=0
		GUI_VPY=0
		GUI_VPW=0
		GUI_VPH=0
		GUI_OX=0
		GUI_OY=0
	EndIf
End Function

Function GUI_BUFFERX(GAD.GAD)
	WIN.WIN=GAD\WIN
	If GAD\PANEL\ACT=True
		If GAD\PANEL\PAR=Null
			Return 0
		Else
			PANEL.PANEL=GAD\PANEL
			If GAD\TYP=gad_PANEL
				PANEL=PANEL\PAR
			EndIf
			While PANEL<>Null
				XL_VX=XL_VX+PANEL\X
				PANEL=PANEL\PAR
			Wend
			Return XL_VX
		EndIf
	Else
		Return 0
	EndIf
End Function

Function GUI_BUFFERY(GAD.GAD)
	WIN.WIN=GAD\WIN
	If GAD\PANEL\ACT=True
		If GAD\PANEL\PAR=Null
			Return 0
		Else
			PANEL.PANEL=GAD\PANEL
			If GAD\TYP=gad_PANEL
				PANEL=PANEL\PAR
			EndIf
			While PANEL<>Null
				XL_VY=XL_VY+PANEL\Y
				PANEL=PANEL\PAR
			Wend
			Return XL_VY
		EndIf
	Else
		Return 0
	EndIf
End Function

Function GUI_BOXIMAGE(GAD.GAD,XL_IMG,XL_DEL=False,XL_CNT=0)
	If XL_IMG>0
		TFormFilter True
		If GAD\IMG>0 And XL_DEL=True
			FreeImage GAD\IMG
		EndIf
		GAD\IMG=XL_IMG
		XL_IW#=ImageWidth(XL_IMG)
		XL_IH#=ImageHeight(XL_IMG)
		XL_BW#=GAD\W;-2
		XL_BH#=GAD\H;-2
		
		If GAD\PAD[2]=True
			XL_HR#=XL_IW/XL_BW
			XL_VR#=XL_IH/XL_BH
			If XL_HR#>XL_VR#
				If XL_HR#>0
					XL_NH#=FLIMIT(XL_IH/XL_HR,1,XL_BH)
					ResizeImage XL_IMG,XL_BW,XL_NH
				EndIf
			Else
				If XL_VR#>0
					XL_NW#=FLIMIT(XL_IW/XL_VR,1,XL_BW)
					ResizeImage XL_IMG,XL_NW,XL_BH
				EndIf
			EndIf
		Else	
			ResizeImage XL_IMG,XL_BW,XL_BH
		EndIf
					
		GAD\PAD[0]=(XL_BW/2)-(ImageWidth(XL_IMG)/2)
		GAD\PAD[1]=(XL_BH/2)-(ImageHeight(XL_IMG)/2)
		
		Return XL_IMG
	EndIf
End Function


Function GUI_REFRESH_IMGAREA(GAD.GAD)
	If GAD\WIN\IMG And GAD\IMG
		SetBuffer ImageBuffer(GAD\WIN\IMG)
		WIN.WIN=GAD\WIN
		GUI_GAD_BUFFER(GAD)
		XL_XOFF=GAD\X+1
		XL_YOFF=GAD\Y+1
		
		If ImageWidth(GAD\IMG)<GAD\W-2
			XL_XOFF=(GAD\X+(GAD\W/2))-(ImageWidth(GAD\IMG)/2)
		EndIf
		If ImageHeight(GAD\IMG)<GAD\H-2
			XL_YOFF=(GAD\Y+(GAD\H/2))-(ImageHeight(GAD\IMG)/2)
		EndIf
		
		DrawBlockRect GAD\IMG,XL_XOFF,XL_YOFF,GAD\PAD[0],GAD\PAD[1],GAD\W-2,GAD\H-2
	EndIf
	SetBuffer BackBuffer():Viewport 0,0,GUI_GFXW,GUI_GFXH:Origin 0,0:GUI_OX=0:GUI_OY=0:GUI_VPX=0:GUI_VPY=0:GUI_VPW=GUI_GFXW:GUI_VPH=GUI_GFXH
End Function

Function GUI_FONTCOL(XL_COL)
	If XL_COL<>GUI_FONTCOL
		XL_BUFF=GraphicsBuffer()
		GUI_FONTCOL=XL_COL
		SetBuffer ImageBuffer(GUI_FONTIMG_ALPHA)
		ClsColor 0,0,XL_COL
		Cls
		ClsColor 0,0,0
		DrawImage GUI_FONTIMG_BETA,0,0
		
		SetBuffer ImageBuffer(GUI_FONTIMG_INV)
		ClsColor 0,0,GUI_FONTCOL
		Cls
		ClsColor 0,0,0
		DrawImage GUI_FONTIMG_INVBETA,0,0
		SetBuffer XL_BUFF
		Viewport GUI_VPX,GUI_VPY,GUI_VPW,GUI_VPH
		Origin GUI_OX,GUI_OY
	EndIf
End Function

Function GUI_GADICON(XL_NAME$,XL_MAIN,XL_NUM=1)
	RET.ICON=New ICON
	RET\NAME$=XL_NAME$
	XL_NUM=QLIMIT(XL_NUM,1,4)
	
	XL_W=ImageWidth(XL_MAIN)/XL_NUM
	RET\W=XL_W
	RET\H=ImageHeight(XL_MAIN)

	Select XL_NUM
		Case 1
			RET\IMG[0]=XL_MAIN
			RET\IMG[1]=XL_MAIN
		Default
			For XL_T=1 To XL_NUM
				RET\IMG[XL_T-1]=CreateImage(RET\W,RET\H)
				SetBuffer ImageBuffer(XL_MAIN)
				GrabImage RET\IMG[XL_T-1],RET\W*(XL_T-1),0
			Next
			FreeImage XL_MAIN
	End Select
	
	XL_MASK=GUI_TRUECOL($FF00FF)
	If RET\IMG[3]=0
		RET\IMG[3]=CopyImage(RET\IMG[0])
		For Y=0 To RET\H-1
			For X=0 To RET\W-1
				XL_COL=ReadPixel(X,Y,ImageBuffer(RET\IMG[3]))And $FFFFFF
				If XL_COL<>XL_MASK
					XL_R=(XL_COL Shr 16) And $FF
					XL_G=(XL_COL Shr 8) And $FF
					XL_B=XL_COL And $FF
					If XL_R>=XL_G And XL_R>=XL_B
						XL_GREY=XL_R
					EndIf
					If XL_G>=XL_R And XL_G>=XL_B
						XL_GREY=XL_G
					EndIf
					If XL_B>=XL_G And XL_B>=XL_G
						XL_GREY=XL_B
					EndIf
					XL_GREY=QLIMIT(XL_GREY-64,0,255)
					XL_COL=(XL_GREY Shl 16)+(XL_GREY Shl 8)+XL_GREY
					WritePixel X,Y,XL_COL,ImageBuffer(RET\IMG[3])
				EndIf
			Next
		Next
	EndIf
	XL_MSKCOL=GUI_TRUECOL($FF00FF)
	For XL_T=0 To 3
		If RET\IMG[XL_T]>0
			MaskImage RET\IMG[XL_T],(XL_MSKCOL Shr 16) And $FF,(XL_MSKCOL Shr 8) And $FF,XL_MSKCOL And $FF
		EndIf
	Next
	RET\OBJ=Handle(RET)
	SetBuffer BackBuffer()
	Insert RET Before First ICON
	Return RET\OBJ
End Function

.FONT_DATA
Data 194
Data 32,0,0,3,13
Data 33,3,0,3,13
Data 34,6,0,5,13
Data 35,11,0,7,13
Data 36,18,0,6,13
Data 37,24,0,8,13
Data 38,32,0,6,13
Data 39,38,0,2,13
Data 40,40,0,3,13
Data 41,43,0,3,13
Data 42,46,0,4,13
Data 43,50,0,6,13
Data 44,56,0,3,13
Data 45,59,0,3,13
Data 46,62,0,3,13
Data 47,65,0,5,13
Data 48,70,0,6,13
Data 49,76,0,6,13
Data 50,82,0,6,13
Data 51,88,0,6,13
Data 52,94,0,6,13
Data 53,100,0,6,13
Data 54,106,0,6,13
Data 55,112,0,6,13
Data 56,118,0,6,13
Data 57,124,0,6,13
Data 58,0,13,3,13
Data 59,3,13,3,13
Data 60,6,13,6,13
Data 61,12,13,6,13
Data 62,18,13,6,13
Data 63,24,13,6,13
Data 64,30,13,11,13
Data 65,41,13,7,13
Data 66,48,13,7,13
Data 67,55,13,7,13
Data 68,62,13,8,13
Data 69,70,13,7,13
Data 70,77,13,6,13
Data 71,83,13,8,13
Data 72,91,13,8,13
Data 73,99,13,3,13
Data 74,102,13,5,13
Data 75,107,13,7,13
Data 76,114,13,6,13
Data 77,120,13,9,13
Data 78,0,26,8,13
Data 79,8,26,8,13
Data 80,16,26,7,13
Data 81,23,26,8,13
Data 82,31,26,8,13
Data 83,39,26,7,13
Data 84,46,26,7,13
Data 85,53,26,8,13
Data 86,61,26,7,13
Data 87,68,26,11,13
Data 88,79,26,7,13
Data 89,86,26,7,13
Data 90,93,26,7,13
Data 91,100,26,3,13
Data 92,103,26,5,13
Data 93,108,26,3,13
Data 94,111,26,6,13
Data 95,117,26,6,13
Data 96,123,26,3,13
Data 97,126,26,6,13
Data 98,0,39,6,13
Data 99,6,39,6,13
Data 100,12,39,6,13
Data 101,18,39,6,13
Data 102,24,39,3,13
Data 103,27,39,6,13
Data 104,33,39,6,13
Data 105,39,39,2,13
Data 106,41,39,2,13
Data 107,43,39,6,13
Data 108,49,39,2,13
Data 109,51,39,8,13
Data 110,59,39,6,13
Data 111,65,39,6,13
Data 112,71,39,6,13
Data 113,77,39,6,13
Data 114,83,39,3,13
Data 115,86,39,5,13
Data 116,91,39,3,13
Data 117,94,39,6,13
Data 118,100,39,6,13
Data 119,106,39,8,13
Data 120,114,39,5,13
Data 121,119,39,5,13
Data 122,124,39,5,13
Data 123,0,52,4,13
Data 124,4,52,2,13
Data 125,6,52,4,13
Data 126,10,52,7,13
Data 127,17,52,3,13
Data 128,20,52,6,13
Data 160,26,52,3,13
Data 161,29,52,3,13
Data 162,32,52,6,13
Data 163,38,52,6,13
Data 164,44,52,6,13
Data 165,50,52,6,13
Data 166,56,52,2,13
Data 167,58,52,6,13
Data 168,64,52,3,13
Data 169,67,52,9,13
Data 170,76,52,4,13
Data 171,80,52,6,13
Data 172,86,52,6,13
Data 173,92,52,3,13
Data 174,95,52,8,13
Data 175,103,52,6,13
Data 176,109,52,4,13
Data 177,113,52,6,13
Data 178,119,52,3,13
Data 179,122,52,3,13
Data 180,125,52,3,13
Data 181,128,52,6,13
Data 182,0,65,6,13
Data 183,6,65,3,13
Data 184,9,65,3,13
Data 185,12,65,3,13
Data 186,15,65,4,13
Data 187,19,65,6,13
Data 188,25,65,8,13
Data 189,33,65,8,13
Data 190,41,65,8,13
Data 191,49,65,6,13
Data 192,55,65,7,13
Data 193,62,65,7,13
Data 194,69,65,7,13
Data 195,76,65,7,13
Data 196,83,65,7,13
Data 197,90,65,7,13
Data 198,97,65,10,13
Data 199,107,65,7,13
Data 200,114,65,7,13
Data 201,121,65,7,13
Data 202,128,65,7,13
Data 203,0,78,7,13
Data 204,7,78,3,13
Data 205,10,78,3,13
Data 206,13,78,3,13
Data 207,16,78,3,13
Data 208,19,78,8,13
Data 209,27,78,8,13
Data 210,35,78,8,13
Data 211,43,78,8,13
Data 212,51,78,8,13
Data 213,59,78,8,13
Data 214,67,78,8,13
Data 215,75,78,6,13
Data 216,81,78,8,13
Data 217,89,78,8,13
Data 218,97,78,8,13
Data 219,105,78,8,13
Data 220,113,78,8,13
Data 221,121,78,7,13
Data 222,128,78,7,13
Data 223,0,91,6,13
Data 224,6,91,6,13
Data 225,12,91,6,13
Data 226,18,91,6,13
Data 227,24,91,6,13
Data 228,30,91,6,13
Data 229,36,91,6,13
Data 230,42,91,10,13
Data 231,52,91,6,13
Data 232,58,91,6,13
Data 233,64,91,6,13
Data 234,70,91,6,13
Data 235,76,91,6,13
Data 236,82,91,2,13
Data 237,84,91,4,13
Data 238,88,91,4,13
Data 239,92,91,4,13
Data 240,96,91,6,13
Data 241,102,91,6,13
Data 242,108,91,6,13
Data 243,114,91,6,13
Data 244,120,91,6,13
Data 245,126,91,6,13
Data 246,0,104,6,13
Data 247,6,104,6,13
Data 248,12,104,6,13
Data 249,18,104,6,13
Data 250,24,104,6,13
Data 251,30,104,6,13
Data 252,36,104,6,13
Data 253,42,104,5,13
Data 254,47,104,6,13
Data 255,53,104,5,13
Data 256,58,104,3,13