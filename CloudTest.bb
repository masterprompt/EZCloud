Include "CloudLib/CloudLib.bb"
Include "timerlib/Timer_Definitions.bb"
Graphics3D(640,480,32,2)
SetBuffer(BackBuffer())

CamPiv = CreatePivot()
Camera = CreateCamera(CamPiv)
CameraRange camera,1,1000
Light  = CreateLight(1)


;CameraClsColor(Camera,113,184,255)

Cloud_Init(Camera)
Cloud_LODActive=0				;LOD System Flag
Cloud_LODDistance=400			;Distance for LOD
D# = 10.5

Cloud_LoadCloud("test2.clf")



	;Cloud_Kill_Particles(MyCloud, 50)


	CldTime = Timer_Create()
	RotX# = 0
	RotY# = 0
	MoveMouse(GraphicsWidth()/2,GraphicsHeight()/2)
While Not KeyDown(1)
	f#=f+.5
	If f > 180 Then f=0
	;Cloud_Set_CloudFormation(MyCloud,Sin(F))
	;Cloud_ChangeFormation(MyCloud,Sin(f))
	;ScaleEntity MyCloud,.2,.2,.2

	MX = MouseX()-(GraphicsWidth()/2)
	MY = MouseY() - (GraphicsHeight()/2)
	MoveMouse(GraphicsWidth()/2,GraphicsHeight()/2)
	RotY# = RotY# - MX
	RotX# = RotX# + MY
	If RotX# > 89 Then RotX# = 89
	If RotX < -89 Then RotX = -89
	
	;TurnEntity MyCloud,0,2,0
	
	Timer_StartTime(CldTime)
	Cloud_Update()
	Timer_EndTime(CldTime)
	;TurnEntity lightpivot,0,1,0
	If KeyDown(203) Then MoveEntity CamPiv,-1,0,0
	If KeyDown(205) Then MoveEntity CamPiv,1,0,0
	If KeyDown(200) Then MoveEntity CamPiv,0,0,1
	If KeyDown(208) Then MoveEntity CamPiv,0,0,-1
	RotateEntity Camera,RotX,0,0
	RotateEntity CamPiv,0,RotY,0
	If KeyDown(2) Then Stop
	RenderWorld
	If KeyDown(2) Then Stop
	
	NewFPS = MilliSecs() - OldFPS
	If NewFPS = 0 Then FPS = 1000 Else FPS = 1000/NewFPS
	OldFPS = MilliSecs()
	Text 0,0,"FPS:"+FPS
	Text 0,10,"CTime:"+Timer_GetTime(CldTime)+" ms"
	Text 0,20,"PArticles:"+Cloud_PBSize
	
	Flip False
Wend
End