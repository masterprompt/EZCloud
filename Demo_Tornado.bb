Include "CloudLib/CloudLib.bb"
Const RALPHA = 24	;Return Alpha when using RColor
Const RRED = 16	;Return Red when using RColor
Const RGREEN = 8	;Return Green when using RColor
Const RBLUE = 0	;Return Blue when using RColor

Global SArrayCound
   Dim SArray1#(100000)
   Dim SArray2(100000)
	Dim CLDS(10000)

Graphics3D 800,600,32,2
SetBuffer BackBuffer()

CamPiv = CreatePivot()
Camera = CreateCamera(CamPiv)
MoveEntity CamPiv,0,0,-100
Cloud_TemplateDir$="bin/"
Cloud_Init(Camera)

LoadClouds("samples/Tornado1.EZC")
Sort(0,SArrayCound)
SArrayCound=SArrayCound-1
For A = 0 To SArrayCound
	CLDS(A) = SArray2(SArrayCound-A)
Next

	RotX# = 0
	RotY# = 0
	MoveMouse(GraphicsWidth()/2,GraphicsHeight()/2)
While Not KeyDown(1)
	B#=0
	For A = 0 To SArrayCound
		
		C# = Float(A+1)/Float(SArrayCound)
		B#=ASin(C#)*.5
		TurnEntity CLDS(A),0,B#,0
	Next	
	MX = MouseX()-(GraphicsWidth()/2)
	MY = MouseY() - (GraphicsHeight()/2)
	MoveMouse(GraphicsWidth()/2,GraphicsHeight()/2)
	RotY# = RotY# - MX
	RotX# = RotX# + MY
	If RotX# > 89 Then RotX# = 89
	If RotX < -89 Then RotX = -89
	MV# = 5
	If KeyDown(203) Then MoveEntity CamPiv,-MV#,0,0
	If KeyDown(205) Then MoveEntity CamPiv,MV#,0,0
	If KeyDown(200) Then MoveEntity CamPiv,0,0,MV#
	If KeyDown(208) Then MoveEntity CamPiv,0,0,-MV#
	RotateEntity Camera,RotX,0,0
	RotateEntity CamPiv,0,RotY,0


	Cloud_Update()
	RenderWorld()
	Flip()
	
Wend
End




Function LoadClouds(FileName$)
	MyFile = ReadFile(FileName$)
		ReadLine(MyFile)
		ReadFloat(MyFile)
		CAMR = ReadInt(MyFile)
		CAMG = ReadInt(MyFile)
		CAMB = ReadInt(MyFile)
		ReadByte(MyFile)
		ReadInt(MyFile)
		SG=ReadByte(MyFile)
		SG = ReadByte(MyFile)
		CamRX = ReadFloat(MyFile)		
		CamRY = ReadFloat(MyFile)
		CamZoom = ReadFloat(MyFile)	
		Num = ReadInt(MyFile)
		SArrayCound=0
		For a = 1 To Num
			
			X# = ReadFloat(MyFile)
			Y# = ReadFloat(MyFile)
			Z# = ReadFloat(MyFile)
			SX# = ReadFloat(MyFile)
			SY# = ReadFloat(MyFile)
			SZ# = ReadFloat(MyFile)
			TType = ReadInt(MyFile)
			LOD = ReadByte(MyFile)
			PCount = ReadInt(MyFile)
			Size# = ReadFloat(MyFile)
			
			Alpha# = ReadFloat(MyFile)
			C1 = ReadInt(MyFile)
			C2 = ReadInt(MyFile)
			C3 = ReadInt(MyFile)
			C4 = ReadInt(MyFile)
			SArray2(SArrayCound) = Cloud_CreateCloud(X#,Y#,Z#)
			SArray1#(SArrayCound) = Y#
			ScaleEntity(SArray2(SArrayCound),SX#,SY#,SZ#)
			For B = 1 To PCount
				X# = ReadFloat(MyFile)
				Y# = ReadFloat(MyFile)
				Z# = ReadFloat(MyFile)
				TType = ReadInt(MyFile)
				LOD = LOD
				Form# = ReadFloat(Myfile)
				CPI = Cloud_CreateParticle(SArray2(SArrayCound),X#,Y#,Z#,TType, Form#,Size#,Alpha#,LOD)
				Cloud_Set_ParticleColor(SArray2(SArrayCound), CPI,1,RColor(C1,RRed),RColor(C1,RGreen),RColor(C1,RBlue))
				Cloud_Set_ParticleColor(SArray2(SArrayCound), CPI,2,RColor(C2,RRed),RColor(C2,RGreen),RColor(C2,RBlue))
				Cloud_Set_ParticleColor(SArray2(SArrayCound), CPI,3,RColor(C3,RRed),RColor(C3,RGreen),RColor(C3,RBlue))
				Cloud_Set_ParticleColor(SArray2(SArrayCound), CPI,4,RColor(C4,RRed),RColor(C4,RGreen),RColor(C4,RBlue))
			Next
			SArrayCound=SArrayCound+1
		Next
		CloseFile(MyFile)		
End Function

Function RColor%(c%,d%)
	Return c Shr d And 255 Shl 0
End Function
Function SortSwap(A,B)
	Local T#,G
	T# = SArray1(A)
	SArray1(A)=SArray1(B)
	SArray1(B)=T#
	
	G = SArray2(A) 
	SArray2(A) = SArray2(B)
	SArray2(B) = G
End Function
Function Sort(BegI,EndI)
	Local Piv,L,R
	If EndI > (BegI+1) Then
		Piv=SArray1(BegI):L=BegI+1:R=EndI
		While L < R
			If SArray1(L) <= Piv Then
				L=L+1;
			Else
				R=R-1
				SortSwap(L,R)
			EndIf
		Wend
		L=L-1
		SortSwap(L,BegI)
		Sort(BegI,L)
		Sort(R,EndI)
	EndIf
End Function