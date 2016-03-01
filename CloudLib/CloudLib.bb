;===============================================Cloud Library====================================================
;============================================Written by Dark Half================================================
;==========================================master_prompt@hotmail.com============================================
;===================================================v1.0.0=======================================================
Global Cloud_Cam=0						;Stores our Camera to use with the clouds.
Global Cloud_Mesh=0						;Mesh to store our Clouds in
Global Cloud_Surface=0					;Stores the current surface we are working with
Global Cloud_Surface_Verticies=0			;Stores number of Verticies for the current surface we're working with
Global Cloud_Texture=0					;Holds our cloud texture
Global Cloud_SysActive=0					;State of the cloud system
Global Cloud_Dir$="CloudLib/"			;Holds our CloudLib Library Path
Global Cloud_MaxVerticies=50000			;Max number of verticies before we create a new surface
Global Cloud_SortCount=0					;Counter for our sorting Algorythm
Global Cloud_SurfaceCount=0				;Holds the number of surfaces we have
Global Cloud_ActiveParticles=0			;Counter of active particles we have drawing
Global Cloud_OwnedPArticles=0			;Total number of partcles that have cloud owners
Global Cloud_TotalParticles=0			;Counter of Total particles in system
Global Cloud_LODActive=0				;LOD System Flag
Global Cloud_LODDistance=500			;Distance for LOD
Global Cloud_PBSize=0
Global Cloud_TemplateDir$=""			;Directory to hold our template file
Global Cloud_TemplateFile$="cloudpack.ctp"
Global Cloud_Object						;Used to hold our main pivot for our clouds
Global Cloud_LO,CLoud_LP				;Used by the loader
   Dim Cloud_TA#(16,4)					;Holds our Array of Type offsets for the texture
   Dim Cloud_SArray1#(100000)			;Sorting Array 1
   Dim Cloud_SArray2(100000)				;Sorting Array 2
   Dim Cloud_Surfaces(1000)				;Holds our surfaces

Type Cloud									;-Holds each cloud refference
	Field ID								;ID of cloud
	Field P									;Pivot to control our cloud
	Field F#								;Formation Value
	Field Particles						;Number of particles in cloud
	Field CPIDs[1000]						;Hold our CloudParticle refferences
	Field UForm							;Flag for updating the formation
End Type

Type CloudParticle						;-Holds each partcles for the clouds
	Field ID								;ID of particle
	Field PBID								;PArticle Buffer ID
	Field P									;Pivot particle is attached to
	Field L									;LOD Flag
	Field T									;Type of particle
	Field S#								;Particle Scale
	Field ST#								;PArticle Stretch
	Field F#								;Formation Distance
	Field A#								;Particle Alpha
	Field Alpha#
	Field SX#,SY#
	Field CR1,CB1,CG1						;Color Assignments
	Field CR2,CB2,CG2						;Color Assignments
	Field CR3,CB3,CG3						;Color Assignments
	Field CR4,CB4,CG4						;Color Assignments
	Field UColor, UAlpha					;Color and alpha update flags
	Field UType								;Type update Flag
	Field USize								;Size update flag
End Type

Type ParticleBuffer						;-Particle repository
	Field ID								;ID of particle
	Field CM,CS							;Particle mesh and surface
	Field V[4]								;Vertices of particle
End Type

Type UnusedBuffer							;-Holds unused Particle Buffer Particles
	Field ID								;ID of the particleBuffer Particle
End Type

;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Initializes the EZCloud System
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Init(CAM)	
	If CAM=0 Then Cloud_GenError(2)
	Cloud_Cam 								=CAM
	If Cloud_Mesh =0 Then Cloud_Mesh =CreateMesh(Cloud_Cam)
	If Cloud_Surface =0 Then Cloud_Surface =CreateSurface(Cloud_Mesh)
	If Cloud_Texture =0 Then
		Cloud_LoadTemplate()
	EndIf
	If Cloud_Mesh = 0 Or Cloud_Texture = 0 Then
		Cloud_GenError(3)
	EndIf
	Cloud_Object=CreatePivot()
	Cloud_SurfaceCount=Cloud_SurfaceCount+1
	Cloud_Surfaces(Cloud_SurfaceCount)=Cloud_Surface
	Cloud_SysActive = 1
	Cloud_DefineTypes()
	EntityFX(Cloud_Mesh,32+2+1)
	EntityTexture(Cloud_Mesh,Cloud_Texture)
End Function


;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Creates a cloud Refference and returns it's handle
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_CreateCloud(X#=0,Y#=0,Z#=0)	
	Local C.Cloud
	If Cloud_SysActive Then
		C.Cloud 							=New Cloud
		C\ID 								=Handle(C)
		C\P									=CreatePivot(Cloud_Object)
		C\F#								=1
		C\Particles						=0
		PositionEntity(C\P,X#,Y#,Z#)
		NameEntity(C\P,C\ID)
		Return(C\P)
	EndIf
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Creates a cloud Particle Refference and returns it's index
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_CreateParticle(CloudHan,X#=0,Y#=0,Z#=0,T=4,F#=1,S#=8,A#=.7,L=0,ST#=1)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1) 
		Cloud_OwnedPArticles				=Cloud_OwnedPArticles+1
		C\Particles							=C\Particles+1
		CP.CloudParticle					=New CloudParticle
		CP\ID								=Handle(CP)
		CP\PBID								=0
		CP\P								=CreatePivot(C\P)
		C\CPIDs[C\Particles]				=CP\ID
		CP\T								=T
		CP\S#								=S#
		CP\ST#								=ST#
		CP\F#								=F#
		CP\A#								=A
		CP\Alpha#							=A#
		CP\L								=L
		CP\CR1								=255
		CP\CB1								=255
		CP\CG1								=255
		CP\CR2								=255
		CP\CB2								=255
		CP\CG2								=255
		CP\CR3								=255
		CP\CB3								=255
		CP\CG3								=255
		CP\CR4								=255
		CP\CB4								=255
		CP\CG4								=255						
		CP\UColor							=1
		CP\UAlpha							=1
		CP\UType							=1
		CP\USize							=1
		Cloud_TotalParticles				=Cloud_TotalParticles+1
		PositionEntity(CP\P,X#,Y#,Z#)
		NameEntity(CP\P,CP\ID)
		Return(C\Particles)
	Else
		Return 0
	EndIf
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Checks to see if a particle is available, if not, create one!
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_MakeParticle()	
	Local UB.UnusedBuffer, PB.ParticleBuffer,ID=0
	UB.UnusedBuffer							=First UnusedBuffer
	If UB = Null Then
		Cloud_PBSize=Cloud_PBSize+1
		PB.ParticleBuffer						=New ParticleBuffer
		PB\ID									=Handle(PB)
		ID										=PB\ID
		PB\CM									=Cloud_Mesh
		If Cloud_Surface_Verticies > Cloud_MaxVerticies Then
			Cloud_SurfaceCount						=Cloud_SurfaceCount+1
			Cloud_Surface 								=CreateSurface(Cloud_Mesh)
			Cloud_Surface_Verticies					=0
			Cloud_Surfaces(Cloud_SurfaceCount) 		=Cloud_Surface
						
		EndIf
		PB\CS									=Cloud_Surface
		PB\V[0] 								=AddVertex(PB\CS,-1, 1,0,  0,0)
		PB\V[1] 								=AddVertex(PB\CS, 1, 1,0,  0,0)
		PB\V[2] 								=AddVertex(PB\CS, 1,-1,0,  0,0)
		PB\V[3] 								=AddVertex(PB\CS,-1,-1,0,  0,0)		
		Cloud_Surface_Verticies				=Cloud_Surface_Verticies+4
	Else
		ID 										=UB\ID
		Delete(UB.UnusedBuffer)
	EndIf
	Return(ID)
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns an unused particle to the unused buffer
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_ReturnParticle(PBID)	
	Local UB.UnusedBuffer, PB.ParticleBuffer,ID=0
	If PBID <> 0 Then
		UB.UnusedBuffer						=New UnusedBuffer
		UB\ID									=PBID
	EndIf
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Updates the Cloud System
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Update()
	Local C.Cloud,CP.CloudParticle,PB.ParticleBuffer
	Local A,X#,Y#,Z#,X1#,X2#,Y1#,Y2#,Dist#,LOD=0
	Cloud_SortCount=0
	For A = 1 To Cloud_SurfaceCount
		ClearSurface(Cloud_Surfaces(A),0,1)
	Next
	For C.Cloud = Each Cloud
		If Cloud_LODActive Then
			CD# = EntityDistance#(Cloud_Cam,C\P)
			If CD > Cloud_LODDistance Then LOD=1 Else LOD=0
		EndIf
		For A = 1 To C\Particles
			CP = Object.CloudParticle(C\CPIDs[A])
			TFormPoint(EntityX(CP\P,1),EntityY(CP\P,1),EntityZ(CP\P,1),0,Cloud_Cam)
			X# = TFormedX():Y# = TFormedY():Z# = TFormedZ()
			If C\UForm Then CP\UAlpha = 1
			If Z < 1 Or (LOD <> CP\L And Cloud_LODActive) Then
				Cloud_ReturnParticle(CP\PBID)	
				CP\PBID=0
			Else
					If CP\PBID = 0 Then
						CP\PBID=Cloud_MakeParticle()
						CP\UType=1
						CP\UAlpha=1
						
					EndIf
					PB=Object.ParticleBuffer(CP\PBID)
					If CP\USize Then
						CP\SX# = CP\ST# * CP\S#
						CP\SY# = CP\S#
						CP\USize=0
					EndIf
					X1# = X#- CP\SX#
					X2# = X#+ CP\SX#
					Y1# = Y#- CP\SY#
					Y2# = Y#+ CP\SY#
					VertexCoords(PB\CS,PB\V[0],X1#,Y2#,Z#)
					VertexCoords(PB\CS,PB\V[1],X2#,Y2#,Z#)
					VertexCoords(PB\CS,PB\V[2],X2#,Y1#,Z#)
					VertexCoords(PB\CS,PB\V[3],X1#,Y1#,Z#)
					If CP\UAlpha Then
						CP\Alpha# = ((CP\F# * 2) * ( C\F# * ( 1-C\F# ) ) ) + ( CP\A#*(C\F#*C\F#))
					EndIf
					If CP\UColor Or CP\UAlpha Then
						VertexColor(PB\CS,PB\V[0],CP\CR1,CP\CG1,CP\CB1,CP\Alpha#)
						VertexColor(PB\CS,PB\V[1],CP\CR2,CP\CG2,CP\CB2,CP\Alpha#)
						VertexColor(PB\CS,PB\V[2],CP\CR3,CP\CG3,CP\CB3,CP\Alpha#)
						VertexColor(PB\CS,PB\V[3],CP\CR4,CP\CG4,CP\CB4,CP\Alpha#)
						CP\UColor=0
						CP\UAlpha=0
					EndIf					
					If CP\UType Then
						VertexTexCoords(PB\CS,PB\V[0],  Cloud_TA#(CP\T,1),Cloud_TA#(CP\T,3))
						VertexTexCoords(PB\CS,PB\V[1],  Cloud_TA#(CP\T,2),Cloud_TA#(CP\T,3))
						VertexTexCoords(PB\CS,PB\V[2],  Cloud_TA#(CP\T,2),Cloud_TA#(CP\T,4))
						VertexTexCoords(PB\CS,PB\V[3],  Cloud_TA#(CP\T,1),Cloud_TA#(CP\T,4))	
						CP\UType=0				
					EndIf
					Dist# = (X#*X#) + (Z#*Z#)
					Cloud_SArray1(Cloud_SortCount) = Dist#
					Cloud_SArray2(Cloud_SortCount) = CP\PBID
					Cloud_SortCount=Cloud_SortCount+1
			EndIf
		Next
		C\UForm = 0
	Next
	Cloud_Sort(0,Cloud_SortCount)
	Cloud_DrawParticles()
End Function

;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Re-draws all particles for correct ZOrder
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_DrawParticles()
	Local PB.ParticleBuffer,A
	For A = (Cloud_SortCount-1) To 0 Step -1
		PB=Object.ParticleBuffer(Cloud_SArray2(A))
		AddTriangle(PB\CS,PB\V[0],PB\V[1],PB\V[2])
		AddTriangle(PB\CS,PB\V[2],PB\V[3],PB\V[0])			
	Next
	Cloud_ActiveParticles = Cloud_SortCount
	Cloud_SortCount=0
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Kills a number of particles in a cloud
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Kill_Particles(CloudHan, Num)
	Local C.Cloud, CP.CloudParticle, A
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		If Num >= C\Particles Then Num = C\Particles
		For A = C\Particles To ((C\Particles-Num)+1) Step -1
			CP = Object.CloudParticle(C\CPIDs[A])
			Cloud_ReturnParticle(CP\PBID)
			FreeEntity(CP\P)
			Delete(CP.CloudParticle)
			Cloud_TotalParticles=Cloud_TotalParticles-1
			Cloud_OwnedPArticles= Cloud_OwnedPArticles-1
		Next
		C\Particles = C\Particles-Num
	EndIf	 
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Kills all particles in a cloud
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Kill_AllParticles(CloudHan)
	Local C.Cloud, CP.CloudParticle, A,Num
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		Num = C\Particles
		For A = 1 To C\Particles
			CP = Object.CloudParticle(C\CPIDs[A])
			Cloud_ReturnParticle(CP\PBID)
			FreeEntity(CP\P)
			Delete(CP.CloudParticle)
			Cloud_TotalParticles=Cloud_TotalParticles-1
			Cloud_OwnedPArticles=Cloud_OwnedPArticles-1
		Next
		C\Particles = C\Particles-Num
	EndIf	 
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Kills a Cloud
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Kill_Cloud(CloudHan)
	Local C.Cloud, CP.CloudParticle, A
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		For A =  1 To C\Particles
			CP = Object.CloudParticle(C\CPIDs[A])
			Cloud_ReturnParticle(CP\PBID)
			Cloud_TotalParticles=Cloud_TotalParticles-1
			FreeEntity(CP\P)
			Delete(CP.CloudParticle)
			Cloud_OwnedPArticles=Cloud_OwnedPArticles-1
		Next
		FreeEntity(C\P)
		C\Particles = 0
		Delete(C.Cloud)
	EndIf	 
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Kills all Clouds
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Kill_AllClouds()
	Local C.Cloud
	For C.Cloud = Each Cloud
		Cloud_Kill_Cloud(C\P)
	Next
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Counts Particles In A Cloud
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleCount(CloudHan)
	Local C.Cloud
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		Return(C\Particles)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Handle From A Cloud
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_Particle(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\P)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Type
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleType(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\T)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Scale
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleScale#(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\S#)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Formation Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleFormation#(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\F#)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Alpha Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleAlpha#(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\A#)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle Stretch Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleStretch#(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\ST#)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Returns a Particle LOD Flag
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Get_ParticleLOD(CloudHan, Index)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Return(CP\L)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Cloud Formation
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_CloudFormation(CloudHan,F#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		C\F#								=F#
		C\UForm								=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle LOD Flag
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleLOD(CloudHan, Index,L)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\L								=L
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Position
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticlePosition(CloudHan, Index,X#,Y#,Z#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		PositionEntity(CP\P,X#,Y#,Z#)
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Scale
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleScale(CloudHan, Index,S#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\S#								=S#
		CP\USize							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Type
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleType(CloudHan, Index,T)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\T								=T
		CP\UType							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Alpha Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleAlpha(CloudHan, Index,A#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\A#								=A#
		CP\UAlpha							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Alpha Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleStretch(CloudHan, Index,A#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\ST#								=A#
		CP\USize							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Formation Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleFormation(CloudHan, Index,F#)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		CP\F#								=F#
		CP\UAlpha							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sets a Particle Color Value
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_Set_ParticleColor(CloudHan, Index,CRNR,CR,CG,CB)
	Local C.Cloud, CP.CloudParticle
	If CloudHan <> 0 Then
		C									=Object.Cloud(EntityName(CloudHan))
		If C=Null Then Cloud_GenError(1)
		CP									=Object.CloudParticle(C\CPIDs[Index])
		Select CRNR
			Case 1
				CP\CR1=CR
				CP\CG1=CG
				CP\CB1=CB
			Case 2
				CP\CR2=CR
				CP\CG2=CG
				CP\CB2=CB
			Case 3
				CP\CR3=CR
				CP\CG3=CG
				CP\CB3=CB
			Case 4
				CP\CR4=CR
				CP\CG4=CG
				CP\CB4=CB												
		End Select
		CP\UColor							=1
	EndIf	
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Defines Regions for Texture Types of Clouds
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_DefineTypes()
	Local X,Y,D
	For y = 1 To 4
		For x = 1 To 4
			D = D + 1
			Cloud_TA#(D,1) = (Float(x) -1) * .25 
			Cloud_TA#(D,2) = (Float(x) ) * .25
			Cloud_TA#(D,3) = (Float(y) -1) * .25
			Cloud_TA#(D,4) = (Float(y) ) * .25
		Next
	Next
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Error Handler For Cloud System
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_GenError(EC)
	Select EC
		Case 1 RuntimeError("No Cloud Is Associated With That Handle!")
		Case 2 RuntimeError("Unable To set Cloud Cam To Null!")
		Case 3 RuntimeError("Unable To Initialize Cloud System!")
	End Select
End Function

;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Sorting Algorythm
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Global Sort_A#,Sort_B#
Function Cloud_SortSwap(A,B)
	Local T#,G
	T# = Cloud_SArray1(A)
	Cloud_SArray1(A)=Cloud_SArray1(B)
	Cloud_SArray1(B)=T#
	
	G = Cloud_SArray2(A) 
	Cloud_SArray2(A) = Cloud_SArray2(B)
	Cloud_SArray2(B) = G
End Function
Function Cloud_Sort(BegI,EndI)
	Local Piv,L,R
	If EndI > (BegI+1) Then
		Piv=Cloud_SArray1(BegI):L=BegI+1:R=EndI
		While L < R
			If Cloud_SArray1(L) <= Piv Then
				L=L+1;
			Else
				R=R-1
				Cloud_SortSwap(L,R)
			EndIf
		Wend
		L=L-1
		Cloud_SortSwap(L,BegI)
		Cloud_Sort(BegI,L)
		Cloud_Sort(R,EndI)
	EndIf
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Template Loader
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_LoadTemplate()
	Local f,K,B,fs,T,of$
	of$=SystemProperty("tempdir")+"tmp_img.png"
	FS = FileSize(Cloud_TemplateDir$+Cloud_TemplateFile$)
	F = ReadFile(Cloud_TemplateDir$+Cloud_TemplateFile$)
	B = CreateBank(FS)
	ReadBytes(B,F,0,FS)
	CloseFile(F)
	F=WriteFile(of$)
	WriteBytes(B,F,10,FS-10)
	CloseFile(F)
	FreeBank(B)
	Cloud_Texture =LoadTexture(of$,2+16+32)
	DeleteFile(of$)
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Color Converter
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function CColor(c,d)
	Return c Shr d And 255 Shl 0
End Function
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
;-Cloud Loader
;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
Function Cloud_LoadCloud(FileName$)
	Local CldFile=0,FType
	CldFile = ReadFile(FileName)
	If CldFile = 0 Then Return 0
	FType = ReadInt(CLDFile)
	If FType <> $000002 Then CloseFile(CldFile):Return 0
	Cloud_Loader(CLDFile)
	CloseFile(CldFile)
	Return 1
End Function

Function Cloud_Loader(LoadFile)
	Local ChunkSize=0,ChunkRead=8,ChunkType=0, ChunkData=0
	Local CLD,CLP,CLR=16,CLG=8,CLB=0
	Local F1#,F2#,F3#,C1,C2,C3,C4
	ChunkSize = ReadInt(LoadFile)
	While ChunkRead < ChunkSize And Eof(LoadFile)=0
		ChunkType = ReadInt(Loadfile)
		ChunkRead=ChunkRead+4
		CLD=Cloud_LO
		CLP=CLoud_LP
		Select ChunkType
			Case $000002 ChunkData=Cloud_Loader(LoadFile)
			Case $F00002;Version
				ReadInt(LoadFile)
				ReadInt(LoadFile):ReadFloat(LoadFile):ChunkData=8
			Case $00A000;Cloud
				Cloud_LO=Cloud_CreateCloud(0,0,0)
				ChunkData=Cloud_Loader(LoadFile)
				Case $00A001;Position
					ReadInt(LoadFile)
					F1#=ReadFloat(LoadFile)
					F2#=ReadFloat(LoadFile)
					F3#=ReadFloat(LoadFile)	
					PositionEntity(CLD,F1#,F2#,F3#)
					ChunkData=12
				Case $00A100;Particle
					CLoud_LP=Cloud_CreateParticle(CLD)
					ChunkData=Cloud_Loader(LoadFile)
					Case $00A110;Position
						ReadInt(LoadFile)
						F1#=ReadFloat(LoadFile)
						F2#=ReadFloat(LoadFile)
						F3#=ReadFloat(LoadFile)
						Cloud_Set_ParticlePosition(CLD, CLP,F1#,F2#,F3#)
						ChunkData=12				
					Case $00A120;Type
						ReadInt(LoadFile)
						C1=ReadInt(LoadFile)
						Cloud_Set_ParticleType(CLD, CLP,C1)	
						ChunkData=4
					Case $00A130;Color
						ReadInt(LoadFile)
						C1=ReadInt(LoadFile)
						C2=ReadInt(LoadFile)
						C3=ReadInt(LoadFile)
						C4=ReadInt(LoadFile)
						Cloud_Set_ParticleColor(CLD,CLP,1,CColor(C1,CLR),CColor(C1,CLG),CColor(C1,CLB))
						Cloud_Set_ParticleColor(CLD,CLP,2,CColor(C2,CLR),CColor(C2,CLG),CColor(C2,CLB))
						Cloud_Set_ParticleColor(CLD,CLP,3,CColor(C3,CLR),CColor(C3,CLG),CColor(C3,CLB))
						Cloud_Set_ParticleColor(CLD,CLP,4,CColor(C4,CLR),CColor(C4,CLG),CColor(C4,CLB))
						ChunkData=16
					Case $00A140;LOD
						ReadInt(LoadFile)
						C1=ReadByte(LoadFile)
						Cloud_Set_ParticleLOD(CLD,CLP,C1)
						ChunkDAta=1
					Case $00A150;Size
						ReadInt(LoadFile)
						F1#=ReadFloat(LoadFile)	
						Cloud_Set_ParticleScale(CLD,CLP,F1)
						ChunkData=4
					Case $00A151;Stretch
						ReadInt(LoadFile)
						F1#=ReadFloat(LoadFile)	
						Cloud_Set_ParticleStretch(CLD,CLP,F1)
						ChunkData=4						
					Case $00A160;Alpha
						ReadInt(LoadFile)
						F1#=ReadFloat(LoadFile)	
						Cloud_Set_ParticleAlpha(CLD,CLP,F1)
						ChunkData=4
					Case $00A170;Formation
						ReadInt(LoadFile)
						F1#=ReadFloat(LoadFile)
						Cloud_Set_ParticleFormation(CLD,CLP,F1)	
						ChunkData=4
			Default ChunkData=Cloud_SkipChunk(LoadFile)
		End Select
		ChunkRead=ChunkRead+ChunkData
	Wend
	Return(ChunkRead-4)
End Function
Function Cloud_SkipChunk(FileHandle)
	Local ChunkSize, CurPos
	ChunkSize = ReadInt(FileHandle)
	CurPos = FilePos(FileHandle)
	SeekFile(FileHAndle,CurPos+(ChunkSize-8))
	Return ChunkSize-4
End Function

;<-Begin Format
;KCSFile ($000002)
;		Version Info 	($F00002)
;			-Type	 	(Int) -clf Type $FFFFFA
;			-Version 	(Float)
;		CloudChunk 		($00A000)
;			-Position Chunk 	($00A001)
;				-3 x Float
;			Particle Chunk 		($00A100)
;				-Position 			($00A110)
;					-3 x Float
;				-Type				($00A120)
;					-1 x Int
;				-Color				($00A130)
;					-4 x Int
;				-LOD				($00A140)
;					-1 x Byte
;				-Size				($00A150)
;					-1 x Float
;				-Stretch			($00A151)
;					-1 x Float
;				-Alpha				($00A160)
;					-1 x Float
;				-Formation			($00A170)
;					-1 x Float
;<-End Format