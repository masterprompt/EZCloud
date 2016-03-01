Global ObjectPivot=0
Global PPickAlpha#=1

Type CL
	Field ID
	Field CM,Cloud
	Field TType
	Field Selected
	Field SX#,SY#,SZ#
	Field Stretch#
	Field Num
	Field Size#
	Field Alpha#
	Field Update
	Field LOD
	Field Colors[4]
End Type

Type CopyCL
	Field X#,Y#,Z#
	Field TType
	Field SX#,SY#,SZ#
	Field Num
	Field Size#
	Field Alpha#
	Field LOD
	Field Stretch#
	Field Colors[4]
End Type

Const RALPHA = 24	;Return Alpha when using RColor
Const RRED = 16	;Return Red when using RColor
Const RGREEN = 8	;Return Green when using RColor
Const RBLUE = 0	;Return Blue when using RColor
Function IntColor(R,G,B,A=255)
	Return A Shl 24 Or R Shl 16 Or G Shl 8 Or B Shl 0
End Function
Function RColor(c,d)
	Return c Shr d And 255 Shl 0
End Function

Function SaveEZC(FileName$)
	SaveEZCloudFile(FileName$)
End Function
Function OpenEZC_v1_0_(FileName$,Mergeflag=0)
	OpenEZCFile(FileName$,Mergeflag)
End Function

Function ChangeGroupAlpha(Alpha#);Changes the cloud group alpha so we can pick them with cloud particles on
	Local C.CL
	PPickAlpha#=Alpha#
	For C.CL = Each CL
			
			EntityAlpha(C\CM,PPickAlpha#)
	Next
End Function

Function RegenSelected()
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\Update=1
			EndIf
	Next
End Function

Function SetNumParticles(Num)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				If Num  > C\Num Then;Add particles
					For a = 1 To (Num-C\Num)
						SX# = 1
						SY# = 1
						SZ# = 1
						CAX#=Rnd(-1,1)
						CAY#=Rnd(-1,1)
						CAZ#=Rnd(-1,1)
						MaxD# = Sqr((SX#*SX#) + (SY#*SY#) + (SZ#*SZ#))
						CurD#=1-(Sqr((CAX#*CAX#)+(CAY#*CAY#)+(CAZ#*CAZ#))/MaxD#)
						Stretch# = C\Stretch#
						Select C\TType
							Case 1 TType = Rand(5,10)
							Case 2 TType = Rand(1,4)
							Case 3 TType = Rand(11,16)
						End Select				
						CPI = Cloud_CreateParticle(C\Cloud,CAX#,CAY#,CAZ#,TType, CurD#,C\Size#,C\Alpha#,C\LOD,Stretch#)
						Cloud_Set_ParticleColor(C\Cloud, CPI,1,RColor(C\Colors[1],RRed),RColor(C\Colors[1],RGreen),RColor(C\Colors[1],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,2,RColor(C\Colors[2],RRed),RColor(C\Colors[2],RGreen),RColor(C\Colors[2],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,3,RColor(C\Colors[3],RRed),RColor(C\Colors[3],RGreen),RColor(C\Colors[3],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,4,RColor(C\Colors[4],RRed),RColor(C\Colors[4],RGreen),RColor(C\Colors[4],RBlue))

					Next				
				EndIf
				If Num < C\Num Then;Remove particles
					Num2 = C\Num - Num
					Cloud_Kill_Particles(C\Cloud,Num2)
				EndIf
				C\Num = Num
			EndIf
	Next
	CheckForOverThosand()
End Function
Function SetScale(Scale#)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\Size# = Scale#
				If C\Size# < .001 Then C\Size#=.001
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
					Cloud_Set_ParticleScale(C\Cloud,a,Scale#)
				Next
			EndIf
	Next
End Function

Function SetStretch(Stretch#)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\Stretch# = Stretch#
				If C\Stretch# < .001 Then C\Stretch#=.001
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
					Cloud_Set_ParticleStretch(C\Cloud,a,Stretch#)
				Next
			EndIf
	Next
End Function

Function SetLOD(LOD)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\LOD = LOD
				If C\Size# < .001 Then C\Size#=.001
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
					Cloud_Set_ParticleLOD(C\Cloud,a,C\LOD)
				Next
			EndIf
	Next
End Function

Function SetColor(MRGB,Corner)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\Colors[Corner]= MRGB
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
						Cloud_Set_ParticleColor(C\Cloud, a,Corner,RColor(C\Colors[Corner],RRed),RColor(C\Colors[Corner],RGreen),RColor(C\Colors[Corner],RBlue))
				Next
			EndIf
	Next
End Function

Function SetAlpha(Alpha#)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				
				C\Alpha#=Alpha#
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
					Cloud_Set_ParticleAlpha(C\Cloud,a,Alpha#)
				Next
			EndIf
	Next
End Function
Function SetType(TType)
	Local C.CL
	For C.CL = Each CL
			If C\Selected Then
				C\TType = TType
				;Get index of particles for cloud
				Num = Cloud_Get_ParticleCount(C\Cloud)
				;Change all particles to this type
				For a = 1 To Num
					Select C\TType
						Case 1 TType = Rand(5,10)
						Case 2 TType = Rand(1,4)
						Case 3 TType = Rand(11,16)
					End Select
					Cloud_Set_ParticleType(C\Cloud,a,TType)
				Next
			EndIf
	Next
End Function
Function FormateClouds(FVal#)
	Local C.CL
	For C.CL= Each CL
			Cloud_Set_CloudFormation(C\Cloud,FVal#)
	Next
End Function

Function MoveSelected(X#,Y#,Z#)
	Local C.CL
	For C.Cl = Each CL
		If C\Selected Then
			MoveEntity(C\CM,X#,Y#,Z#)
			;MoveEntity(SPRP,X#,Y#,Z#)
			MoveEntity(C\Cloud,X#,Y#,Z#)
		EndIf
	Next
End Function

Function ScaleSelected(X#,Y#,Z#)
	Local C.CL
	For C.Cl = Each CL
		If C\Selected Then
			C\SX# = C\SX + X#
			C\SY# = C\SY + Y#
			C\SZ# = C\SZ + Z#
			If C\SX#<0 Then C\SX#=0
			If C\SY#<0 Then C\SY#=0
			If C\SZ#<0 Then C\SZ#=0
			ScaleEntity(C\CM,C\SX#,C\SY#,C\SZ#)
			ScaleEntity(C\Cloud,C\SX#,C\SY#,C\SZ#)
		EndIf
	Next
End Function

Function CreateCloudGroup(X#,Y#,Z#,SXX#=-1,SXY#=-1,SXZ#=-1,Size#=10,Num=50,TType=1,Alpha#=.7,C1=$FFFFFF,C2=$FFFFFF,C3=$FFFFFF,C4=$FFFFFF,Update=1,Stretch#=1)
	Local C.CL
	C.CL = New CL
	;If ObjectPivot=0 Then ObjectPivot=CreatePivot()
	C\CM = CreateCube();ObjectPivot)
	EntityAlpha(C\CM,PPickAlpha#)
	C\ID = Handle(C)
	If SXX=-1 Then
	Scale# = CamZoom/5
	C\SX#=Scale#:C\SY#=Scale#:C\SZ#=Scale#
	Else
	C\SX#=SXX#:C\SY#=SXY#:C\SZ#=SXZ#
	EndIf
	C\Selected=0
	C\Alpha#=Alpha#
	C\Size#=Size#
	C\Num=Num
	C\TType=TType
	C\Update = Update
	C\Stretch=Stretch#
	
	PositionEntity(C\CM,X#,Y#,Z#)
	ScaleEntity(C\CM,C\SX#,C\SY#,C\SZ#)
	NameEntity(C\CM,C\ID)
	EntityColor(C\CM,150,150,150)
	C\Colors[1]=C1
	C\Colors[2]=C2
	C\Colors[3]=C3
	C\Colors[4]=C4
	C\Cloud=Cloud_CreateCloud(EntityX(C\CM,1),EntityY(C\CM,1),EntityZ(C\CM,1))
	ScaleEntity(C\Cloud,C\SX#,C\SY#,C\SZ#)
	PositionEntity(C\Cloud,X#,Y#,Z#)
	If update Then SelectCloud(C\CM)
	CheckForOverThosand()
	Return C\ID
End Function

Function DeleteSelected()
	Local C.CL
	For C.Cl = Each CL
		If C\Selected Then
			FreeEntity(C\CM)
			Cloud_Kill_Cloud(C\Cloud)
			Delete(C.CL)
		EndIf
	Next
	UpdateGUISelection()
	CheckForOverThosand()
End Function

Function DeleteAll()
	Local C.CL
	For C.Cl = Each CL
		C\Selected =1
	Next
	DeleteSelected()
	UpdateGUISelection()
	CheckForOverThosand()
End Function

Function CopySelected()
	Local C.CL,CY.CopyCL
	For CY.CopyCL = Each CopyCL
		Delete(CY.CopyCL)
	Next
	For C.CL = Each CL
		If C\Selected Then 
			CY.CopyCL = New CopyCL
			CY\X#			=EntityX(C\CM,1)
			CY\Y#			=EntityY(C\CM,1)
			CY\Z#			=EntityZ(C\CM,1)
			CY\TType		=C\TType
			CY\SX#			=C\SX
			CY\SY#			=C\SY
			CY\SZ#			=C\SZ
			CY\Num			=C\Num
			CY\Size#		=C\Size
			CY\Alpha#		=C\Alpha
			CY\LOD			=C\LOD
			CY\Colors[1] 	=C\Colors[1]
			CY\Colors[2] 	=C\Colors[2]
			CY\Colors[3] 	=C\Colors[3]
			CY\Colors[4] 	=C\Colors[4]
			CY\Stretch		=C\Stretch
		EndIf
	Next
End Function
Function PasteSelected()
	Local C.CL,CY.CopyCL
	DeSelectClouds()
	For CY.CopyCL = Each CopyCL
		C.CL = New CL
		C\CM = CreateCube()
		C\ID = Handle(C)
		C\SX#=CY\SX#:C\SY#=CY\SY#:C\SZ#=CY\SZ#
		C\Selected=0
		C\Alpha#=CY\Alpha#
		C\Size#=CY\Size#
		C\Num=CY\Num
		C\TType=CY\TType
		C\LOD=CY\LOD
		C\Stretch=CY\Stretch
			C\Colors[1] 	=CY\Colors[1]
			C\Colors[2] 	=CY\Colors[2]
			C\Colors[3] 	=CY\Colors[3]
			C\Colors[4] 	=CY\Colors[4]		
		
		PositionEntity(C\CM,CY\X#,CY\Y#,CY\Z#)
		ScaleEntity(C\CM,C\SX#,C\SY#,C\SZ#)
		NameEntity(C\CM,C\ID)
		EntityColor(C\CM,150,150,150)
		C\Cloud=Cloud_CreateCloud(EntityX(C\CM,1),EntityY(C\CM,1),EntityZ(C\CM,1))
		ScaleEntity(C\Cloud,C\SX#,C\SY#,C\SZ#)
		C\Update = 1
		SelectCloud(C\CM,1)	
		EntityAlpha(C\CM,PPickAlpha#)	
	Next
	UpdateGUISelection()
	CheckForOverThosand()
End Function

Function CheckCloudUpdates()
	Local C.CL
	For C.Cl = Each CL
		If C\Update Then
			Cloud_Kill_AllParticles(C\Cloud)
			For a = 1 To C\Num
				SX# = 1
				SY# = 1
				SZ# = 1
				CAX#=Rnd(-1,1)
				CAY#=Rnd(-1,1)
				CAZ#=Rnd(-1,1)
				MaxD# = Sqr((SX#*SX#) + (SY#*SY#) + (SZ#*SZ#))
				CurD#=1-(Sqr((CAX#*CAX#)+(CAY#*CAY#)+(CAZ#*CAZ#))/MaxD#)
				Stretch# = C\Stretch#
				Select C\TType
					Case 1 TType = Rand(5,10)
					Case 2 TType = Rand(1,4)
					Case 3 TType = Rand(11,16)
				End Select				
						CPI = Cloud_CreateParticle(C\Cloud,CAX#,CAY#,CAZ#,TType, CurD#,C\Size#,C\Alpha#,C\LOD,Stretch#)
						Cloud_Set_ParticleColor(C\Cloud, CPI,1,RColor(C\Colors[1],RRed),RColor(C\Colors[1],RGreen),RColor(C\Colors[1],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,2,RColor(C\Colors[2],RRed),RColor(C\Colors[2],RGreen),RColor(C\Colors[2],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,3,RColor(C\Colors[3],RRed),RColor(C\Colors[3],RGreen),RColor(C\Colors[3],RBlue))
						Cloud_Set_ParticleColor(C\Cloud, CPI,4,RColor(C\Colors[4],RRed),RColor(C\Colors[4],RGreen),RColor(C\Colors[4],RBlue))
			
			Next
			C\Update=0
			CheckForOverThosand()
		EndIf
	Next
	
End Function

Function SetObjectPicked(PickMode)
	Local C.CL
	For C.Cl = Each CL
		EntityPickMode(C\CM,PickMode)
	Next
End Function

Function SelectAll()
	Local C.CL
	For C.Cl = Each CL
		C\Selected = 1
		EntityColor(C\CM,255,150,150) 
	Next
	UpdateSelection()
	UpdateGUISelection()	
End Function

Function SelectCloud(CloudHan, override=0)
	Local C.CL
	If KeyDown(29)=0 And KeyDown(157)=0 And override=0 Then DeSelectClouds()
	If CloudHan <> 0 Then CM=EntityName(CloudHan) Else Return 0
	C=Object.CL(CM)
	If C = Null Then Return 0
	C\Selected = 1-C\Selected
	If C\Selected Then 
		EntityColor(C\CM,255,150,150) 
	Else 
		EntityColor(C\CM,150,150,150)
	EndIf
	UpdateSelection()
	UpdateGUISelection()
End Function

Function UpdateGUISelection()
	Local C.CL, Num=0
	For C.CL = Each CL
		If C\Selected Then
			Num=Num+1
			TType = C\TType
			Size# = Size#+C\Size#
			Numb = Numb +C\Num
			Alpha# = Alpha#+C\Alpha#
			LOD = LOD+C\LOD
			Stretch# = Stretch#+C\Stretch#
		EndIf
	Next	
	If Num = 0 Then
		GUI_SetActive(Prop1,False)
		GUI_SetActive(Prop2,False)
		GUI_SetActive(Prop3,False)
		GUI_SetActive(Prop4,False)
		GUI_SetActive(Prop5,False)
		GUI_SetActive(Prop16,False)
		GUI_SetActive(Prop6,False)
		GUI_SetActive(Prop7,False)
		GUI_SetActive(Prop13,False)
		GUI_SetActive(Prop14,False)
		GUI_SetOn(Prop13,0)
		GUI_SetOn(Prop14,0)
	EndIf
	GUI_SetOn(Prop1,0)
	GUI_SetOn(Prop2,0)
	GUI_SetOn(Prop3,0)
	If Num =1 Then
		GUI_SetActive(Prop4,True)
		GUI_SetActive(Prop5,True)
		GUI_SetActive(Prop16,True)
		GUI_SetActive(Prop6,True)
		GUI_SetActive(Prop13,True)
		GUI_SetActive(Prop14,True)
		If LOD Then 
			GUI_SetOn(Prop14,1)
		Else
			GUI_SetOn(Prop13,1)
		EndIf
		GUI_SetVal(Prop4,Numb)
		GUI_SetFloat(Prop5,Size#)
		GUI_SetVal(Prop6,Alpha#)
		GUI_SetVal(Prop16,Stretch#)
		Select TType
			Case 1 GUI_SetOn(Prop1,1)
			Case 2 GUI_SetOn(Prop2,1)
			Case 3 GUI_SetOn(Prop3,1)
		End Select
	Else
		GUI_SetActive(Prop4,False)
		GUI_SetActive(Prop5,False)
		GUI_SetActive(Prop16,False)
		GUI_SetActive(Prop6,False)
		GUI_SetActive(Prop13,False)	
		GUI_SetActive(Prop14,False)
	
	EndIf
	If Num > 0 Then
		GUI_SetActive(Prop4,True)
		GUI_SetActive(Prop5,True)
		GUI_SetActive(Prop16,True)
		GUI_SetActive(Prop6,True)
		GUI_SetActive(Prop13,True)
		GUI_SetActive(Prop14,True)
		GUI_SetVal(Prop4,Numb/num)
		GUI_SetFloat(Prop5,Size#/Float(num))
		GUI_SetVal(Prop6,Alpha#/Float(num))
		GUI_SetVal(Prop16,Stretch#/Float(num))	
	
		GUI_SetActive(Prop1,True)
		GUI_SetActive(Prop2,True)
		GUI_SetActive(Prop3,True)
		GUI_SetActive(Prop7,True)
		GUI_SetActive(Prop9,True)
		GUI_SetActive(Prop10,True)
		GUI_SetActive(Prop11,True)
		GUI_SetActive(Prop12,True)
			
	EndIf
	
	;GUI_Refresh(MainWin)
	
	
End Function

Function UpdateSelection()
	Local C.CL, Num, CX#,CY#,CZ#
	For C.CL = Each CL
		If C\Selected Then
			Num=Num+1
			CX=CX+EntityX(C\CM,1)
			CY=CY+EntityY(C\CM,1)
			CZ=CZ+EntityZ(C\CM,1)
		EndIf
	Next
	If Num > 0 Then
		CX = CX/Num
		CY = CY/Num
		CZ = CZ/Num
		ShowArrow=1
		PositionEntity(SPRP,CX,CY,CZ)
	Else
		ShowArrow=0
	EndIf
End Function

Function DeSelectClouds()
	Local C.CL
	For C.Cl = Each CL
		C\Selected=0
		EntityColor(C\CM,150,150,150)
	Next
	PositionEntity(SPRP,0,0,0)
End Function

Function AddMeshCustom(SMesh,DMesh,BCR,BCG,BCB)
	;Add a surface to existing mesh
	CS = CreateSurface(DMesh)
	SCS = GetSurface(SMesh,1)
	BS = CreateBrush()
	BrushColor(BS,BCR,BCG,BCB)
	PaintSurface(CS,BS)
	;Add all verticies
	Verts = CountVertices(SCS)-1
	For A=0 To Verts
		AddVertex(CS,VertexX(SCS,A),VertexY(SCS,A),VertexZ(SCS,A))	
		VertexNormal(CS,A,VertexNX(SCS,A),VertexNY(SCS,A),VertexNZ(SCS,A))
	Next
	Tris = CountTriangles(SCS)-1
	For A = 0 To Tris
		AddTriangle(CS,TriangleVertex(SCS,A,0),TriangleVertex(SCS,A,1),TriangleVertex(SCS,A,2))
	Next
End Function