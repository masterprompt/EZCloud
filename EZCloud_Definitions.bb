;=================EZCloud Definitions File============================

;:::::::::::::::::Includes
Include "PAKLib/Bones.bb"
Include "XLNT/XLNT_Definitions.bb"
Include "GUILib/GUILib_Definitions.bb"
Include "ObjectLib/ObjectLib_Definitions.bb"
Include "cloudlib/CloudLib.bb"
Include "TimerLib/Timer_Definitions.bb"
Include "FileLib/FileLib.bb"
Include "FileLib/FileTypes.bb"
Include "FileLib/Export.bb"

;:::::::::::::::::Globals
Global Version$											="1.0.0"
Global AppTit$ 											=""
Global GW,GH
Global EndApp											=0
Global CloudTimer										=0
Global DemoApp 											=1
Global DemoSaveTXT$ 									="This is a Demo. Saving is disabled!"
Global CMD$												=0
Global APDIR$ 											=""
;:::::::::::::::::Functions
Function App_Init()
	ViewX=1:ViewY=57
	Local DeskW, DeskH
	AppTit$ = "EZ Cloud Builder v"+Version$
	AppTitle(AppTit$)
	
	outputdir$=SystemProperty("tempdir")
	Cloud_TemplateDir$="BIN/"
	PakInit "BIN/EZCloud.PAK", $DCECC47D, "TMP", $73B0438D	
	;Read config file
	MyFile = ReadFile(APDIR$+"bin/config.dtf")
		T_GW = ReadInt(MyFile)
		T_GH = ReadInt(MyFile)
		T_GD = ReadInt(MyFile)
		T_VW = ReadInt(MyFile)
		T_VH = ReadInt(MyFile)
	CloseFile(MyFile)

	If Instr(CMD$," /S ",1) Or Right(CMD$,2)="/S" Then 
		T_GW = 640
		T_GH = 480
		T_VW = T_GW-200
		T_VH = (T_GH-ViewY)-50		
	EndIf	
	If T_GW > GetSystemMetrics%(0) Or T_GH > GetSystemMetrics%(1) Then
		T_GW =GetSystemMetrics%(0) 
		T_GH =GetSystemMetrics%(1)
		T_VW = T_GW-200
		T_VH = (T_GH-ViewY)-50
	EndIf
	DSetW = T_GW
	DSetH = T_GH
	ViewW = T_VW
	ViewH = T_VH
	
	If GfxMode3DExists(DSetW,DSetH,16) And Windowed3D() Then
		Graphics3D(DSetW,DSetH,0,2)
		GW=GraphicsWidth()
		GH=GraphicsHeight()
		SetBuffer(BackBuffer())
		Splash = LoadImage(PAK("ezcloudlogo.png"))
		MidHandle(Splash)
		DrawImage(Splash,GW/2,GH/2)
		Flip
		GUI_MOUSE_DRAW=False
		CloudTimer = Timer_Create()	
		GUI_Init()	
		PakClean()
	Else
		RuntimeError("Windowed 3D Graphics is not supported!")
	EndIf
End Function

Function App_Loop()
	While Not EndApp 
		If GUI_WinMode = 0 And MouseDown(1)=0 Then NoViewI=0
		If GUI_WinMode<>0 Then NoViewI = 1
		If NoViewI = 0 Then
			CheckCloudUpdates()
			DoView()
			UpdateSelection()
			DoCamera()
		EndIf
			CheckGUIChanges()
			CheckGUI()
			Timer_StartTime(CloudTimer)
			Cloud_Update()
			Timer_EndTime(CloudTimer)
		
		GUI_Render3d(MainView)
		GUI()
		Flip(False)
	Wend
End Function