;=================GUI Library Definitions File============================

;:::::::::::::::::Includes

;:::::::::::::::::Globals
Dim Slope#(3,3)
Dim GFXMODE(500,4)
Global KeyTrig=0
;:::::::::::::::::Functions
Function DoCamera()
	RotateEntity CamPiv1,0,CamRX#,0
	RotateEntity CamPiv2,CamRY#,0,0
	PositionEntity MainCam,0,0,-CamZoom#
	PositionEntity RenderCam,0,0,-CamZoom#
	ScaleEntity(SPRP,CamZoom#,CamZoom#,CamZoom#)
End Function

Function WriteConfigFile()
	MYFile = WriteFile(APDIR$+"bin/config.dtf")
	WriteInt(MyFile,GFXMODE(GFXSelected,1))
	WriteInt(MyFile,GFXMODE(GFXSelected,2))
	WriteInt(MyFile,GFXMODE(GFXSelected,3))
	WriteInt(MyFile,GFXVW)
	WriteInt(MyFile,GFXVH)
	CloseFile(MyFile)
End Function

Function DoView()
	CheckGUIKeys()
	
	If ViewAction <> 0 Then
		Select ViewAction
			Case 1;Select Object
				DOSelectView()
			Case 2;Rotate Camera
				DORotateView()
			Case 3;Manual Zoom
				DOZoomViewManual()
			Case 4;Auto Zoom
				DOZoomViewAuto()
			Case -1
				If MouseDown(1)=0 Then ViewAction=0
		End Select
	Else
		ViewAction = CheckViewAction()
	EndIf
	If (SelectAction = 2 Or SelectAction = 3) And ViewAction=0 Then CheckOverAxis()
End Function

Function CheckGUIChanges()
	GD = EV_Hit()
	Select GD
		Case Prop1 SetType(1)
		Case Prop2 SetType(2)
		Case Prop3 SetType(3)
		Case Prop7 RegenSelected()
		Case Prop9 
			MYCOLOR =  Color_Dialog()
			If MYCOLOR <> -1 Then
				SetColor(MYCOLOR,1)
			EndIf
		Case Prop10
			MYCOLOR =  Color_Dialog()
			If MYCOLOR <> -1 Then
				SetColor(MYCOLOR,2)
			EndIf		
		Case Prop11
			MYCOLOR =  Color_Dialog()
			If MYCOLOR <> -1 Then
				SetColor(MYCOLOR,4)
			EndIf		
		Case Prop12
			MYCOLOR =  Color_Dialog()
			If MYCOLOR <> -1 Then
				SetColor(MYCOLOR,3)
			EndIf
		Case Prop13
			SetLOD(0)
		Case Prop14
			SetLOD(1)			
				
	End Select
	If EV_Gad_NewVal(Prop4) Then SetNumParticles(GUI_GadVal(Prop4))
	If EV_Gad_NewVal(Prop5) Then SetScale(GUI_GadFloat(Prop5))
	If EV_Gad_NewVal(Prop6) Then SetAlpha(GUI_GadFloat(Prop6))
	If EV_Gad_NewVal(Prop8) Then FormateClouds(GUI_GadFloat(Prop8))
	If EV_Gad_NewVal(Prop15) Then Cloud_LODDistance=GUI_GadVal(Prop15)
	If EV_Gad_NewVal(Prop16) Then SetStretch(GUI_GadFloat(Prop16))
End Function


Function DOSelectView()
	Select SelectAction
		Case 1;Select objects
			If SelectActionSub=0 Then
				If MouseDown(1) = 0 Then 
					SetObjectPicked(2)
					Ent=CameraPick(MainCam,MouseX()-ViewX,MouseY()-ViewY)
					SelectCloud(Ent)
					SetObjectPicked(0)				
					UpdateGUISelection()
					ViewAction=0:SelectActionSub=0
				EndIf
			EndIf
		Case 2;Position Objects
			DoPositionObjects()
			If MouseDown(1) = 0 Then ViewAction=0:SelectActionSub=0
		Case 3;Scale Objects
			DoScaleObjects()
			If MouseDown(1) = 0 Then ViewAction=0:SelectActionSub=0
	End Select
	
End Function

Function DoScaleObjects()
	Local MVX#,MVY#,MVZ#
	MCX# = MouseX() - MX
	MCY# = MouseY() - MY
	If MCY# <> 0 Or MCX# <> 0 Then
				VX# = MCX#
				VY# = MCY#
				LD# = Sqr((VX#*VX#)+(VY#*VY#))
				VX# = VX#/LD#
				VY# = VY#/LD#		
		Select AxisSelected
			Case 1
				Ang# = ACos((VX#*Slope#(1,1))+(VY#*Slope#(1,2)))
				MVX#=Sin(90-Ang#) * LD#
				AXP# = (MVX# / Slope#(1,3))*.2
				MVX# = CamZoom#*AXP#
			Case 2
				Ang# = ACos((VX#*Slope#(2,1))+(VY#*Slope#(2,2)))
				MVY#=Sin(90-Ang#) * LD#
				AXP# = (MVY# / Slope#(2,3))*.2
				MVY# = CamZoom#*AXP#			
			Case 3
				Ang# = ACos((VX#*Slope#(3,1))+(VY#*Slope#(3,2)))
				MVZ#=Sin(90-Ang#) * LD#
				AXP# = (MVZ# / Slope#(3,3))*.2
				MVZ# = (CamZoom#*AXP#)
		End Select
		ScaleSelected(MVX#,MVY#,MVZ#)	
		MX=MouseX():MY=MouseY()	
	EndIf
End Function

Function DoPositionObjects()
	Local MVX#,MVY#,MVZ#
	MCX# = MouseX() - MX
	MCY# = MouseY() - MY
	If MCY# <> 0 Or MCX# <> 0 Then
				VX# = MCX#
				VY# = MCY#
				LD# = Sqr((VX#*VX#)+(VY#*VY#))
				VX# = VX#/LD#
				VY# = VY#/LD#		
		Select AxisSelected
			Case 1
				Ang# = ACos((VX#*Slope#(1,1))+(VY#*Slope#(1,2)))
				MVX#=Sin(90-Ang#) * LD#
				AXP# = (MVX# / Slope#(1,3))*.2
				MVX# = CamZoom#*AXP#
			Case 2
				Ang# = ACos((VX#*Slope#(2,1))+(VY#*Slope#(2,2)))
				MVY#=Sin(90-Ang#) * LD#
				AXP# = (MVY# / Slope#(2,3))*.2
				MVY# = CamZoom#*AXP#			
			Case 3
				Ang# = ACos((VX#*Slope#(3,1))+(VY#*Slope#(3,2)))
				MVZ#=Sin(90-Ang#) * LD#
				AXP# = (MVZ# / Slope#(3,3))*.2
				MVZ# = (CamZoom#*AXP#)*-1
		End Select
		MoveSelected(MVX#,MVY#,MVZ#)	
		MX=MouseX():MY=MouseY()	
	EndIf
End Function

Function CheckOverAxis()
	EntityPickMode(SPRx,2)
	EntityPickMode(SPRY,2)
	EntityPickMode(SPRZ,2)
	Ent=CameraPick(MainCam,MouseX()-ViewX,MouseY()-ViewY)
	Select Ent
		Case SPRX AxisSelected = 1
		Case SPRY AxisSelected = 2
		Case SPRZ AxisSelected = 3
		Default AxisSelected = 0
	End Select
End Function

Function CheckGUIKeys()
	If KeyTrig = 0 And MouseDown(1)=0 And MouseDown(2)=0 And MouseDown(3)=0 Then
		If KeyDown(29) Or KeyDown(157) Then;CTRL keys
			If KeyDown(30) Then SelectAll():KeyTrig=1:Return(0);CTRL-A
			If KeyDown(46) Then copyselected():KeyTrig=1:Return(0);CTRL-c
			If KeyDown(47) Then pasteselected():KeyTrig=1:Return(0);CTRL-v
			If KeyDown(49) Then DeleteAll():SaveFileName$="":AppTitle(AppTit$):KeyTrig=1:Return(0);CTRL-N
			If KeyDown(24) Then SOpenFile():KeyTrig=1:Return(0);CTRL-O
			If KeyDown(31) Then ;CTRL-S
				If DemoApp Then
					FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
				Else
					If SaveFileName$="" Then 
						SSaveFile() 
					Else
						SaveEZC(SaveFileName$)
					EndIf
				EndIf:KeyTrig=1:Return(0)
			EndIf
						
		Else
			If KeyDown(211) Then DeleteSelected():KeyTrig=1:Return(0);Delete
			If KeyDown(57) Then ToggleGroups():KeyTrig=1:Return(0);Space
			If KeyDown(34) Then ToggleGridPlane():KeyTrig=1:Return(0);G
		EndIf
	EndIf
	If KeyTrig Then If Not (KeyDown(30) Or KeyDown(46) Or KeyDown(47) Or KeyDown(211) Or KeyDown(49) Or KeyDown(24) Or KeyDown(31) Or KeyDown(57) Or KeyDown(34)) Then KeyTrig=0
End Function

Function CheckGUI()
	CheckGFXSelection()
	
	GadHT = EV_HIT()
	Select GadHT
		Case btn1;New
			DeleteAll()
			SaveFileName$=""
			AppTitle(AppTit$)
		Case btn2;Open
			SOpenFile()
		Case btn3;Save
			If DemoApp Then
				FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
			Else
				If SaveFileName$="" Then 
					SSaveFile() 
				Else
					SaveEZC(SaveFileName$)
				EndIf
			EndIf
		Case btn4;Create
			CreateCloudGroup(0,0,0)
		Case btn5;Select
			SelectAction=1
			UpdateSelection()
		Case btn6;Position
			SelectAction=2
			UpdateSelection()
		Case btn7;Scale
			SelectAction=3
			UpdateSelection()
		Case CNFG_L7;OK for GFX
			GUI_WinHide(ConfigWin)
			WriteConfigFile()
		Case CNFG_L8;Cancel for GFX
			GUI_WinHide(ConfigWin)
			GFXSelected = OLD_GFXSelected
			GFXVW=OLD_GFXVW
			GFXVH=OLD_GFXVH			
	End Select
	MNUHT = EV_Menu_Hit()
	Select MNUHT
		Case mnu1;New
			DeleteAll()
			SaveFileName$=""
			AppTitle(AppTit$)
		Case mnu2;Open
			SOpenFile()
		Case mnu3;Save
			If DemoApp Then
				FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
			Else
				If SaveFileName$="" Then 
					SSaveFile() 
				Else
					SaveEZC(SaveFileName$)
				EndIf
			EndIf
		Case mnu4;Save as
			If DemoApp Then
				FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
			Else		
				SSaveFile()
			EndIf
		Case mnu5;Properties
				
			GUI_WinShow(ConfigWin)
			OLD_GFXSelected = GFXSelected
			OLD_GFXVW=GFXVW
			OLD_GFXVH=GFXVH	
			RefreshGFXWindow()	
		Case mnu6;Exit
			EndApp=1
		Case mnu7;copy
			CopySelected()
		Case mnu8;paste
			PasteSelected()
		Case mnu9;delete
			DeleteSelected()
		Case mnu10;Show Clouds
			ToggleGroups()
		Case mnu12;About
			If DemoApp Then 
				DMO$ = "Demo"
			Else
				DMO$ = "Registered"
			EndIf
			FUI_MessageBox( "©2005 KCStudios All Rights Reserved.  www.KCStudios.net", AppTit$+" "+DMO$)		
		Case mnu13;Select All
			SelectAll()
		Case mnu14;Show Grid
			ToggleGridPlane()
		Case mnu15;Change Camera Color
			MYCOLOR =  Color_Dialog()
			If MYCOLOR <> -1 Then
				CAMR=FUI_GetRed(MYCOLOR)
				CAMG=FUI_GetGreen(MYCOLOR)
				CAMB=FUI_GetBlue(MYCOLOR)
				CameraClsColor(MainCam,CAMR,CAMG,CAMB)
				CameraClsColor(RenderCam,CamR,CamG,CamB)
			EndIf	
		Case mnu11
			ToggleLOD()	
		Case mnu20;Export As Single Cloud
			If DemoApp Then
				FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
			Else
				ExportCloud(1)
			EndIf		
			
		Case mnu21;Export As Multiple Clouds
			If DemoApp Then
				FUI_MessageBox( DemoSaveTXT$, "Demo Only!")
			Else
				ExportCloud(0)
			EndIf		
		Case mnu24;Merge
			SOpenFile(1)
		Case mnu25;Help
			ExecFile(Chr(34)+SystemProperty("appdir")+"EZCLOUD.HLP"+Chr(34))

	End Select
End Function
Function ToggleLOD(T=-1)
	If T=-1 Then
		Cloud_LODActive=1-Cloud_LODActive
	Else
		Cloud_LODActive=T
	EndIf
	If Cloud_LODActive Then 
		GUI_SetText(LBL5,"Active")
		GUI_SetActive(Prop15,True)
		GUI_SetActive(LBL5,True)
		GUI_SetActive(LBL6,True)
		GUI_SetActive(LBL7,True)
	Else
		GUI_SetText(LBL5,"Inactive")
		GUI_SetActive(Prop15,False)
		GUI_SetActive(LBL5,False)
		GUI_SetActive(LBL6,False)
		GUI_SetActive(LBL7,False)		
	EndIf
End Function

Function ToggleGroups(T=-1)
	If T=-1 Then
		ShowClParts=1-ShowClParts
	Else
		ShowClParts=T
	EndIf
	If ShowClParts Then
			ShowEntity(Cloud_Mesh)
			ChangeGroupAlpha(.0001)
	Else
			HideEntity(Cloud_Mesh)
			ChangeGroupAlpha(1)
	EndIf
End Function

Function ToggleGridPlane(T=-1)
	If T=-1 Then
		ShowGrid = 1-ShowGrid
	Else
		ShowGrid=T
	EndIf
			If ShowGrid Then
				ShowEntity(GridPlane)
			Else
				HideEntity(GridPlane)
			EndIf	
End Function

Function ExportCloud(single=0)
	FileName$ = FUI_SaveDialog( "Select File To Export", "", "Cloud Files (*.clf)|*.clf|All Files (*.*)|*.*|" )
	If Filename$ <> "" Then
		Filename$ = Trim(Filename)
		If Mid(FileName$,Len(FileName$)-3,1) <> "." Then Filename = Filename+".clf"
		ExportCloudFile(FileName$,Single)

	EndIf
End Function

Function SOpenFile(MergeFlag=0)
	FileName$ = FUI_OpenDialog( "Select File To Open", "", "EZCloud Files (*.EZC)|*.EZC|All Files (*.*)|*.*|" )
	If Filename$ <> "" Then
		Filename$ = Trim(Filename)
		OpenEZC_v1_0_(Filename$,MergeFlag)
		SaveFileName$=Filename$
		AppTitle(AppTit$ + " -"+SaveFileName$)
	EndIf
	CheckForOverThosand()
End Function
Function PreloadFile(FileName$)
	If Filename$ <> "" Then
		Filename$ = Trim(Filename)
		OpenEZC_v1_0_(Filename$,MergeFlag)
		SaveFileName$=Filename$
		AppTitle(AppTit$ + " -"+SaveFileName$)
	EndIf
	CheckForOverThosand()
End Function
Function SSaveFile()
	FileName$ = FUI_SaveDialog( "Select File To Save", "", "EZCloud Files (*.EZC)|*.EZC|All Files (*.*)|*.*|" )
	If Filename$ <> "" Then
		Filename$ = Trim(Filename)
		If Mid(FileName$,Len(FileName$)-3,1) <> "." Then Filename = Filename+".EZC"
		SaveEZC(Filename)
		SaveFileName$=Filename$
		AppTitle(AppTit$ + " -"+SaveFileName$)
	EndIf
End Function
Function ExportImage()
	FileName$ = FUI_SaveDialog( "Select File To Save", "", "Bitmap Files (*.BMP)|*.BMP|All Files (*.*)|*.*|" )
	If Filename$ <> "" Then
		Filename$ = Trim(Filename)
		If Mid(FileName$,Len(FileName$)-3,1) <> "." Then Filename = Filename+".bmp"
		CameraProjMode(MainCam,0)
		CameraProjMode(Rendercam,1)
		
		Cls()
		RenderWorld()
		;Flip()
		SaveBuffer(BackBuffer(),FileName$)
		;WaitKey()
		CameraProjMode(Rendercam,0)
		CameraProjMode(MainCam,1)
		
	EndIf
End Function


Function CreateGrabPlane(Axis=1)
	FreeGrabPlane()
	GrabPlane = CreateMesh();:EntityAlpha(GrabPlane,.0001)
	CM=GrabPlane
	CS=CreateSurface(CM)
	;Create 4 Verticies
	v0 = AddVertex(CS,-10000,0, 10000)
	v1 = AddVertex(CS, 10000,0, 10000)
	v2 = AddVertex(CS, 10000,0,-10000)
	v3 = AddVertex(CS,-10000,0,-10000)
	;Add 2 triangles
	AddTriangle(CS,v0,v1,v2)
	AddTriangle(CS,v2,v3,v0)
	
	v0 = AddVertex(CS,-10000,1, 10000)
	v1 = AddVertex(CS, 10000,1, 10000)
	v2 = AddVertex(CS, 10000,1,-10000)
	v3 = AddVertex(CS,-10000,1,-10000)		
	AddTriangle(CS,v2,v1,v0)
	AddTriangle(CS,v0,v3,v2)
	
	;PositionEntity(GrabPlane,EntityX(SPRP,1),EntityY(SPRP,1),EntityZ(SPRP,1))
	EntityFX(GrabPlane,16)
	EntityPickMode(GrabPlane,2)
	Select Axis
		Case 1 RotateEntity(CM,90,0,0)
		Case 2 RotateEntity(CM,90,0,0)
		Case 3 RotateEntity(CM,90,90,0)
	End Select
End Function
Function FreeGrabPlane()
	If GrabPlane<>0 Then FreeEntity(GrabPlane):GrabPlane=0
End Function


Function DORotateView()
			MSpeed# = .2
			MCX# = (MouseX() - MX)*MSpeed#
			MCY# = (MouseY() - MY)*MSpeed#
			CamRX=CamRX-MCX#
			CamRY=CamRY+MCY#
			If CamRY > 89 Then CamRY = 89
			If CamRY < -89 Then CamRY = -89
			MX = MouseX():MY=MouseY()
			If MouseDown(2)=0 Then ViewAction=0 
End Function
Function DOZoomViewManual()
			MSpeed# = 1
			MCY# = (MouseY() - MY)*MSpeed#
			MX = MouseX():MY=MouseY()
			CamZoom# = CamZoom# +MCY# 
			If CamZoom# < 2 Then CamZoom# = 2
			If MouseDown(3)=0 Then ViewAction=0 
End Function
Function DOZoomViewAuto()
			MSpeed# = 10
			CamZoom# = CamZoom# + ((MZ-MouseZ())*MSpeed#)
			If CamZoom# < 2 Then CamZoom# = 2
			MZ = MouseZ()
			ViewAction=0
End Function
Function CheckViewAction()
		MX = MouseX()
		MY = MouseY()		
		If MX > ViewX And MX < ViewX+ViewW And MY > ViewY And MY < ViewY+ViewH Then
			If MouseDown(1) Then
				Return(1)
			EndIf
			If MouseDown(2) Then
				Return(2)	
			EndIf
			If MouseDown(3) Then
				Return(3)	
			EndIf
			If MZ <> MouseZ() Then
				Return(4)
			EndIf
		Else
			Return -1
		EndIf
		Return(0)
End Function
Function CheckForOverThosand()
	If Cloud_OwnedPArticles > 1000 Then
		GUI_Menu_ItmAct(mnu20,False)
	Else
		GUI_Menu_ItmAct(mnu20,True)
	EndIf
End Function

Function DrawViewStuff()
	Color(5,5,5)
	TheorFPS = Timer_GetTime(CloudTimer)
	If TheorFPS = 0 Then TheorFPS=1000 Else TheorFPS=1000/TheorFPS	
	GUI_SetText(LBL1,Cloud_OwnedPArticles)
	GUI_SetText(LBL2,(Cloud_OwnedPArticles*2))
	GUI_SetText(LBL3,Timer_GetTime(CloudTimer))
	GUI_SetText(LBL4,TheorFPS)
	GUI_SetText(LBL6,CamZoom#)


	If ShowArrow Then
		If AxisSelected = 1 And ViewAction < 2 Then 
			DrawAxisLines(1,255,0,0) 
		Else 
			DrawAxisLines(1,0,0,255)
		EndIf
		Slope#(1,1) = VecX#
		Slope#(1,2) = VecY#
		Slope#(1,3) = VDist#
		If AxisSelected = 2 And ViewAction < 2 Then 
			DrawAxisLines(6,255,0,0) 
		Else 
			DrawAxisLines(6,0,0,255)
		EndIf
		Slope#(2,1) = VecX#
		Slope#(2,2) = VecY#
		Slope#(2,3) = VDist#		
		If AxisSelected = 3 And ViewAction < 2 Then 
			DrawAxisLines(11,255,0,0) 
		Else 
			DrawAxisLines(11,0,0,255)
		EndIf
		Slope#(3,1) = VecX#
		Slope#(3,2) = VecY#
		Slope#(3,3) = VDist#		
		ShowEntity(SPRP)
	Else
		HideEntity(SPRP)
	EndIf
End Function

Function DrawAxisLines(Axis,CR,CB,CG)
	AS = GetSurface(AxisArrow,1)
	Color(CR,CB,CG)
	CameraProject(MainCam,EntityX(AxisArrow,1),EntityY(AxisArrow,1),EntityZ(AxisArrow,1))
	CX# = ProjectedX()
	CY# = ProjectedY()
	X# = (VertexX(AS,Axis)*CamZoom)+EntityX(AxisArrow,1)
	Y# = (VertexY(AS,Axis)*CamZoom)+EntityY(AxisArrow,1)
	Z# = (VertexZ(AS,Axis)*CamZoom)+EntityZ(AxisArrow,1)
	CameraProject(MainCam,X#,Y#,Z#):Axis=Axis+1
	XD# = ProjectedX()-CX
	YD# = ProjectedY()-CY
	LineDistance# = Sqr((XD#*XD#)+(YD#*YD#))
	;Get Slope
	SLX# = ProjectedX()-CX
	SLY# = ProjectedY()-CY
	;Get Distance
	VDist# = Sqr((SLX#*SLX#)+(SLY#*SLY#))
	;Get Vector
	VecX# = SLX / VDist#
	VecY# = SLY / VDist#
	
	Line(CX,CY,ProjectedX(),ProjectedY())
	CX=ProjectedX()
	CY=ProjectedY()
	
	X# = (VertexX(AS,Axis)*CamZoom)+EntityX(AxisArrow,1)
	Y# = (VertexY(AS,Axis)*CamZoom)+EntityY(AxisArrow,1)
	Z# = (VertexZ(AS,Axis)*CamZoom)+EntityZ(AxisArrow,1)
	CameraProject(MainCam,X#,Y#,Z#):Axis=Axis+1
	Line(CX,CY,ProjectedX(),ProjectedY())	
	X# = (VertexX(AS,Axis)*CamZoom)+EntityX(AxisArrow,1)
	Y# = (VertexY(AS,Axis)*CamZoom)+EntityY(AxisArrow,1)
	Z# = (VertexZ(AS,Axis)*CamZoom)+EntityZ(AxisArrow,1)
	CameraProject(MainCam,X#,Y#,Z#):Axis=Axis+1
	Line(CX,CY,ProjectedX(),ProjectedY())
	X# = (VertexX(AS,Axis)*CamZoom)+EntityX(AxisArrow,1)
	Y# = (VertexY(AS,Axis)*CamZoom)+EntityY(AxisArrow,1)
	Z# = (VertexZ(AS,Axis)*CamZoom)+EntityZ(AxisArrow,1)
	CameraProject(MainCam,X#,Y#,Z#):Axis=Axis+1
	Line(CX,CY,ProjectedX(),ProjectedY())
	X# = (VertexX(AS,Axis)*CamZoom)+EntityX(AxisArrow,1)
	Y# = (VertexY(AS,Axis)*CamZoom)+EntityY(AxisArrow,1)
	Z# = (VertexZ(AS,Axis)*CamZoom)+EntityZ(AxisArrow,1)
	CameraProject(MainCam,X#,Y#,Z#):Axis=Axis+1
	Line(CX,CY,ProjectedX(),ProjectedY())
End Function

Function PopulateGFXList()
	;Get a list of available GFX Modes less than our current desktop
	GFXModes = CountGfxModes3D()
	For A = 1 To GFXModes
		If GfxModeWidth(A) <= GetSystemMetrics%(0) And GfxModeHeight(A) <= GetSystemMetrics%(1) And GfxModeDepth(A) = 16 Then
			If GfxModeWidth(A) >=640 And GfxModeHeight(A) >= 480 Then
				GFXMs=GFXMs+1
				GFXMODE(A,1) = GfxModeWidth(A)
				GFXMODE(A,2) = GfxModeHeight(A)
				GFXMODE(A,3) = GfxModeDepth(A)
				GFXMODE(A,4) = GUI_List_AddITM(CNFG_List,GfxModeWidth(A)+"x"+GfxModeHeight(A),0,"",1,A)
				If GW = GFXMODE(A,1) And GH = GFXMODE(A,2) Then 
					GFXSelected=A
					GUI_Selector_Set(CNFG_L2,GFXMODE(A,4))
				EndIf
			EndIf
		EndIf
	Next
	GFXVW = ViewW
	GFXVH = ViewH	
	CVW = (GFXVW  / (GFXMODE(GFXSelected,1)-200))*100
	CVH = (GFXVH / ((GFXMODE(GFXSelected,2)-ViewY)-50))*100

	GUI_SetVal(CNFG_L4,CVW)
	GUI_SetVal(CNFG_L6,CVH)
	GUI_SetText(CNFG_L3,"View Width:"+GFXVW)
	GUI_SetText(CNFG_L5,"View Height:"+GFXVH)
End Function

Function RefreshGFXWindow()
	GUI_Selector_Set(CNFG_L2,GFXMODE(GFXSelected,4))
	CVW = (Float(GFXVW)  / Float(GFXMODE(GFXSelected,1)-200))*100
	CVH = (Float(GFXVH) / Float((GFXMODE(GFXSelected,2)-ViewY)-50))*100
	GUI_SetVal(CNFG_L4,CVW)
	GUI_SetVal(CNFG_L6,CVH)
	GUI_SetText(CNFG_L3,"View Width:"+GFXVW)
	GUI_SetText(CNFG_L5,"View Height:"+GFXVH)
End Function

Function CheckGFXSelection()
	MySelect = EV_Selector(CNFG_L2)
	If MySelect <> 0 Then
		For A = 1 To GFXModes
			If GFXMODE(A,4) = MySelect Then
				GFXSelected=A
				GFXVW = GFXMODE(GFXSelected,1)-200
				GFXVH = (GFXMODE(GFXSelected,2)-ViewY)-50
				CVW = (GFXVW  / (GFXMODE(GFXSelected,1)-200))*100
				CVH = (GFXVH / ((GFXMODE(GFXSelected,2)-ViewY)-50))*100
				GUI_SetVal(CNFG_L4,CVW)
				GUI_SetVal(CNFG_L6,CVH)				
				UpdateGFXViewSettings()
			EndIf
		Next
	EndIf
	If EV_Gad_NewVal(CNFG_L4) Or EV_Gad_NewVal(CNFG_L6) Then
		GFXVW=(Float(GUI_GadVal(CNFG_L4)) / 100.0) * (GFXMODE(GFXSelected,1)-200)
		If GFXVW < 200 Then GFXVW=200
		GFXVH=(Float(GUI_GadVal(CNFG_L6)) / 100.0) * ((GFXMODE(GFXSelected,2)-ViewY)-50)
		UpdateGFXViewSettings()
	EndIf
End Function

Function UpdateGFXViewSettings()
	GUI_SetText(CNFG_L3,"View Width:"+GFXVW)
	GUI_SetText(CNFG_L5,"View Height:"+GFXVH)
End Function

Function CheckKeyStatus(Key1,CTRL=0)
	Local KRet
	If Key1 > 0 And KeyDown(Key1) Then KRet=1
	If CTRL = (KeyDown(29) Or KeyDown(157)) Then
		KRet=1
	Else
		KRet=0
	EndIf
	Return KRet
End Function
Function CheckKeyDown()
	Local KRet=0
	For A = 1 To 237
		If KeyDown(A) Then KRet=1
	Next
	Return KRet
End Function