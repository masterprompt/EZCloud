;=================GUI Library Definitions File============================

;:::::::::::::::::Includes

;:::::::::::::::::Globals


;:::::::::::::::::Functions



Function GUI_Init()
	
	GUI_GFXSetup()
	

	;Setup Main Window
	MainWin=GUI_Window(0,0,GW,GH,"",0,1)
	ConfigWin = GUI_Window(GW/2-55,GH/2-100,110,200,"EZBuilder Properties","",16,2)
	CNFG_List = GUI_List()
	CNFG_L1 = GUI_Label(ConfigWin,5,30,100,20,"Graphics Mode")
	CNFG_L2 = GUI_SElector(ConfigWin,5,50,100,CNFG_List)
	
	CNFG_L3 = GUI_Label(ConfigWin,5,70,100,20,"View Width:")
	CNFG_L4 = GUI_Slider(ConfigWin,5,90,100,10,100)
	CNFG_L5 = GUI_Label(ConfigWin,5,110,100,20,"View Height:")
	CNFG_L6 = GUI_Slider(ConfigWin,5,130,100,10,100)
	CNFG_L7 = GUI_Button(ConfigWin,5,150,100,"OK")
	CNFG_L8 = GUI_Button(ConfigWin,5,170,100,"CANCEL")
	PopulateGFXList()




	;Create Tool Bar
	GUI_Toolbar_Init()
	
	;Setup Menu
	GUI_Menu_Init()
	
	;Setup Properties
	GUI_Properties_Init()
	
	;Setup Camera
	CamPiv1=CreatePivot()
	CamPiv2=CreatePivot(CamPiv1)
	MainCam=CreateCamera(CamPiv2)
	MoveEntity(MainCam,0,0,0)
	CAMR=100
	CAMG=100
	CAMB=100
	CameraClsColor(MainCam,CAMR,CAMG,CAMB)
	CameraRange(MainCam,1,10000)
	RenderCam = CreateCamera(CamPiv2)
	CameraRange(RenderCam,1,10000)
	CameraViewport(Rendercam,0,0,GW,GH)
	CameraProjMode(Rendercam,0)
	CamRX#=-35
	CamRY#=35
	CamZoom#=100
	;Create the Grid
	GridPlane=CreateGrid()
	;Create the Axis
	CreateAxis()
	
	If DemoApp=-1 Then
		MySprite = LoadSprite(PAK("DEMO.png"),2+4,MainCam)
		MoveEntity(MySprite,0,0,2)
		;EntityAlpha(MySprite,.8)
		EntityOrder(MySprite,-10001)
	EndIf
	
	;Get a light
	Light=CreateLight(1,MainCam)
	MoveEntity(Light,-40,-40,0)	
	
	;Setup Viewport
	If DemoApp Then CamText$ = "Demo Version" Else CamText$ = ""
	MainView=GUI_3dPort(MainWin,ViewX,ViewY,ViewW,ViewH,CamText$,MainCam)	
	
	;Open Main Window
	GUI_OpenWin(MainWin)
	GUI_OpenWin(ConfigWin)
	GUI_WinHide(ConfigWin)
	Cloud_Init(MainCam)
	HideEntity(Cloud_Mesh)
	UpdateGUISelection()
	
End Function

Function GUI_Properties_Init()
	XST = ViewX+ViewW+5
	VOff=ViewY+2
	Ref=20
		GUI_Label(MainWin,XST,VOFF,100,20,"Cloud Type",0):VOff=VOff+Ref
	Prop1=GUI_Radio(MainWin,XST,VOff,"Cirrus",4,1,0,0,"Wispy and Feathery"):VOff=VOff+Ref
	Prop2=GUI_Radio(MainWin,XST,VOff,"Stratus",4,0,0,0,"Layered"):VOff=VOff+Ref
	Prop3=GUI_Radio(MainWin,XST,VOFF,"Cumulus",4,0,0,0,"Puffy"):VOFF=VOFF+Ref+5
	Prop4=GUI_Integer(MainWin,XST,VOFF,50,1,1000,"Particle Count",5,60,0,0,"Set Number Of Particles for Selected"):VOFF=VOFF+Ref
	Prop5=GUI_Float(MainWin,XST,VOFF,10.0,.01,1000,"Particle Size",1,60,2,0,0,"Set Size of Particles for Selected"):VOFF=VOFF+Ref
	Prop16=GUI_Float(MainWin,XST,VOFF,1,.01,100,"Particle Stretch",.1,60,2,0,0,"Set Size of Particles for Selected"):VOFF=VOFF+Ref	
		
		GUI_Label(MainWin,XST,VOFF,100,20,"Alpha",0):VOff=VOff+Ref
	Prop6=GUI_Slider(MainWin,XST,VOFF,180,0.0,1.0,1,0,0,"Set Alpha For Selection"):VOFF=VOFF+Ref
		GUI_Label(MainWin,XST,VOFF,100,20,"Cloud Level of Detail",0):VOff=VOff+Ref
	Prop13=GUI_Radio(MainWin,XST,VOFF,"Normal",10,0,0,1,"Toggles Group For Non-LOD"):XST=XST+80
	Prop14=GUI_Radio(MainWin,XST,VOFF,"LOD",10,0,0,1,"Toggles Group For LOD"):VOFF=VOFF+Ref
	
	XST = ViewX+ViewW+5
		GUI_Label(MainWin,XST,VOFF,100,20,"Cloud Color",0):VOff=VOff+Ref
	Prop9=GUI_Button(MainWin,XST,VOFF,90,"TOP LEFT","",0,0,"Set TOP LEFT Color")
	Prop10=GUI_Button(MainWin,XST+90,VOFF,90,"TOP RIGHT","",0,0,"Set TOP RIGHT Color"):VOFF=VOFF+Ref
	Prop11=GUI_Button(MainWin,XST,VOFF,90,"BOTTOM LEFT","",0,0,"Set BOTTOM LEFT Color")
	Prop12=GUI_Button(MainWin,XST+90,VOFF,90,"BOTTOM RIGHT","",0,0,"Set BOTTOM RIGHT Color"):VOFF=VOFF+Ref+10
	
	Prop7=GUI_Button(MainWin,XST+45,VOFF,90,"Regenerate","",0,0,"Regenerate Selected Particles"):VOFF=VOFF+Ref
		GUI_Label(MainWin,XST,VOFF,100,20,"Formation",0):VOff=VOff+Ref
	Prop8=GUI_Slider(MainWin,XST,VOFF,180,0.0,1.0,1,0,1,"Set Overall Formation")
	GUI_SetVal(Prop6,1)
	GUI_SetVal(Prop8,1)
	
	Ref=15
	VOff=VOff+20
	VOFFO = VOFF
	GUI_Label(MainWin,XST,VOFF,80,20,"Active Particles:",2,0,1,"Active Particles"):VOff=VOff+Ref
	GUI_Label(MainWin,XST,VOFF,80,20,"Cloud Polygons:",2,0,1,"Cloud Polygons"):VOff=VOff+Ref
	GUI_Label(MainWin,XST,VOFF,80,20,"Cloud CPU:",2,0,1,"Cloud CPU Time"):VOff=VOff+Ref
	GUI_Label(MainWin,XST,VOFF,80,20,"Logical FPS:",2,0,1,"Logical FPS"):VOff=VOff+Ref

	
	XST=XST+80
	VOFF = VOFFO
	LBL1=GUI_Label(MainWin,XST,VOFF,80,20,"0",0,0,1,"Active Particles"):VOff=VOff+Ref
	LBL2=GUI_Label(MainWin,XST,VOFF,80,20,"0",0,0,1,"Cloud Polygons"):VOff=VOff+Ref
	LBL3=GUI_Label(MainWin,XST,VOFF,80,20,"0",0,0,1,"Cloud CPU Time"):VOff=VOff+Ref
	LBL4=GUI_Label(MainWin,XST,VOFF,80,20,"1000",0,0,1,"Logical FPS"):VOff=VOff+Ref
	
	Ref=20
	XST = ViewX+5
	VOff = ViewH + ViewY+5	
	LBL7=GUI_Label(MainWin,XST,VOFF,80,20,"LOD System:",2,0,0,"Level of Detail System"):XST=XST+80
	LBL5=GUI_Label(MainWin,XST,VOFF,80,20,"Inactive",0,0,0,"Level of Detail"):VOff=VOff+Ref	
	XST = ViewX+5
	Prop15=GUI_Integer(MainWin,XST,VOFF,Cloud_LODDistance,10,1000,"LOD Distance:",50,60,0,0,"Distance From Camera To LOD"):XST=XST+145:VOff=VOff+2
	LBL6=GUI_Label(MainWin,XST,VOFF,80,20,CamZoom#,0,0,0,"Cam Distance"):VOff=VOff+Ref	
End Function
	
Function GUI_Toolbar_Init()
	LoadIcon("NEW","new_icon.png")
	LoadIcon("OPEN","open_icon.png")
	LoadIcon("SAVE","save_icon.png")
	
	LoadIcon("CREATE","cloud_icon.png")
	LoadIcon("SELECT","select_icon.png")
	LoadIcon("POSITION","position_icon.png")
	LoadIcon("SCALE","scale_icon.png")
	
	XOff=3:Offset=32
	btn1=GUI_Button(MainWin,XOff,23,26,"","NEW",0,1,"New Cloud File"):XOff=XOff+Offset
	btn2=GUI_Button(MainWin,XOff,23,26,"","OPEN",0,1,"Open Cloud File"):XOff=XOff+Offset
	btn3=GUI_Button(MainWin,XOff,23,26,"","SAVE",0,1,"Save Cloud File"):XOff=XOff+(Offset*3)
	
	btn4=GUI_Button(MainWin,XOff,23,26,"","CREATE",0,1,"Create a Cloud Group"):XOff=XOff+(Offset*3)
	
	btn5=GUI_Switch(MainWin,XOff,23,26,"",1,"SELECT",0,1,"Select Cloud Groups"):XOff=XOff+Offset
	GUI_Seton(btn5,1)
	btn6=GUI_Switch(MainWin,XOff,23,26,"",1,"POSITION",0,1,"Position Selection"):XOff=XOff+Offset
	btn7=GUI_Switch(MainWin,XOff,23,26,"",1,"SCALE",0,1,"Scale Selection"):XOff=XOff+Offset
End Function

Function GUI_Menu_Init()
	MainMenu = GUI_WinMenu(MainWin)
	
	mnu_File=GUI_Menu(MainMenu,"File")
		mnu1=GUI_Menu_Item(mnu_File,"New")
		GUI_Menu_Line(mnu_File)
		mnu2=GUI_Menu_Item(mnu_File,"Open")
		mnu24=GUI_Menu_Item(mnu_File,"Merge")
		mnu3=GUI_Menu_Item(mnu_File,"Save")
		mnu4=GUI_Menu_Item(mnu_File,"Save As")
		GUI_Menu_Line(mnu_File)
		mnu20 = GUI_Menu_Item(mnu_File,"Export Single Cloud")
		mnu21 = GUI_Menu_Item(mnu_File,"Export Multiple Clouds")

		;mnu18=GUI_Menu_Item(mnu_File,"Export Image")
		GUI_Menu_Line(mnu_File)
		mnu5=GUI_Menu_Item(mnu_File,"Properties")
		GUI_Menu_Line(mnu_File)
		mnu6=GUI_Menu_Item(mnu_File,"Exit")
	
	mnu_Edit=GUI_Menu(MainMenu,"Edit")
		mnu7=GUI_Menu_Item(mnu_Edit,"Copy")
		mnu8=GUI_Menu_Item(mnu_Edit,"Paste")
		GUI_Menu_Line(mnu_Edit)
		mnu13=GUI_Menu_Item(mnu_Edit,"Select All")
		GUI_Menu_Line(mnu_Edit)
		mnu9=GUI_Menu_Item(mnu_Edit,"Delete")
	
	mnu_EZCloud=GUI_Menu(MainMenu,"EZCloud")
		mnu10=GUI_Menu_Item(mnu_EZCloud,"Toggle Cloud Groups")
		mnu11=GUI_Menu_Item(mnu_EZCloud,"Toggle LOD System")
		mnu14=GUI_Menu_Item(mnu_EZCloud,"Toggle Grid")
		mnu15=GUI_Menu_Item(mnu_EZCloud,"Set Background Color")
	
	mnu_Help=GUI_Menu(MainMenu,"Help")
		mnu25=GUI_Menu_Item(mnu_Help,"Help")
		mnu12=GUI_Menu_Item(mnu_Help,"About EZCloud")

	;GUI_Menu_ItmAct(mnu18,False)
	GUI_Menu_Refresh(mnu_File)
	GUI_Menu_Refresh(mnu_Edit)
	GUI_Menu_Refresh(mnu_EZCloud)
	GUI_Menu_Refresh(mnu_Help)
End Function

Function LoadIcon(ICN$,FileName$)
	GUI_GadICon(ICN$,LoadImage(PAK(FileName$)))
End Function

Function CreateIcon(ICN$,W,H)
	MYIMG = CreateImage(W,H)
	GUI_GadICon(ICN$,MYIMG)
	Return MYIMG
End Function


Function CreateAxis()
	SPRP = CreatePivot()
	Template = CreateCylinder(8,1,0)
	ScaleMesh(Template,.01,.2,.01)
	PositionMesh(Template,0,.2,0)
	Cone = CreateCone()
	ScaleMesh(Cone,.02,.02,.02)
	PositionMesh(Cone,0,.41,0)
	AddMesh(Cone,Template)
	FreeEntity(Cone)

	SPRX = CopyMesh(Template,SPRP)
	SPRY = CopyMesh(Template,SPRP)
	SPRZ = CopyMesh(Template,SPRP)
	
	EntityColor(SPRX,255,0,0)
	EntityColor(SPRY,0,255,0)
	EntityColor(SPRZ,0,0,255)
	
	RotateMesh(SPRX,0,0,-90)
	RotateMesh(SPRY,0,0,0)
	RotateMesh(SPRZ,-90,0,0)
	
	AxisArrow = CreateMesh()
	AS = CreateSurface(AxisArrow)
	AddVertex(AS,0,0,0)
	SC#=.2
	SC2#=SC#*.1
	;X
	AddVertex(AS,SC#,0,0)
	AddVertex(AS,SC#-SC2#,SC2#,0)
	AddVertex(AS,SC#-SC2#,-SC2#,0)
	AddVertex(AS,SC#-SC2#,0,SC2#)
	AddVertex(AS,SC#-SC2#,0,-SC2#)
	;Y
	AddVertex(AS,0,SC#,0)
	AddVertex(AS,0,SC#-SC2#,SC2#)
	AddVertex(AS,0,SC#-SC2#,-SC2#)
	AddVertex(AS,SC2#,SC#-SC2#,0)
	AddVertex(AS,-SC2#,SC#-SC2#,0)
	;Z
	AddVertex(AS,0,0,-SC#)
	AddVertex(AS,SC2#,0,-SC#+SC2#)
	AddVertex(AS,-SC2#,0,-SC#+SC2#)
	AddVertex(AS,0,-SC2#,-SC#+SC2#)
	AddVertex(AS,0,SC2#,-SC#+SC2#)
	
	
	
	EntityAlpha(SPRx,.0001)
	EntityAlpha(SPRy,.0001)
	EntityAlpha(SPRz,.0001)
	EntityParent(AxisArrow,SPRP)
	;EntityOrder(AxisArrow,-1000)
	HideEntity(SPRP)
	FreeEntity(Template)
End Function

Function ColorImage(IMG,CR,CG,CB)
	SetBuffer(ImageBuffer(IMG))
		Color(CR,CG,CB)
		Rect(0,0,ImageWidth(IMG),ImageHeight(IMG),1)
	SetBuffer(BackBuffer())
End Function

Function CreateGrid()
	CM = CreateMesh()
	CS = CreateSurface(CM)
	;Create 4 Verticies
	V0 = AddVertex(CS,-100,0, 100,  0,0)
	V1 = AddVertex(CS, 100,0, 100,  1,0)
	V2 = AddVertex(CS, 100,0,-100,  1,1)
	V3 = AddVertex(CS,-100,0,-100,  0,1)
	;Change Normals
	VertexNormal(CS,V0,0,1,0)
	VertexNormal(CS,V1,0,1,0)
	VertexNormal(CS,V2,0,1,0)
	VertexNormal(CS,V3,0,1,0)
	;Add 2 triangles
	AddTriangle(CS,V0,V1,V2)
	AddTriangle(CS,V2,V3,V0)
	GT=LoadTexture(PAK("Grid.png"),2)
	EntityTexture(CM,GT)
	EntityFX(CM,1+16)
	ScaleTexture(GT,.04,.04)
	Return(CM) 
End Function