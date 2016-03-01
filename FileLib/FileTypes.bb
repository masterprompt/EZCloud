Global SaveVersion# = 1.0

Global EZC.CL
Global CPI=0

;Save EZC Format:
;----------------
;<-Begin Format
;KCSFile ($000001)
;		Version Info ($F00002)
;			-Type	 (Int) -EZCloud Type $FFAFFA
;			-Version (Float)
;		Builder Info ($000A00)
;			-Camera info ($000A10)
;						-R,G,B (3 x Int)
;						-CamX,CamY,CamZ (3 x float)
;			-Ojects Info ($000A20)
;						-ShowGroups (Byte)
;						-ShowGrid (Byte)
;			-Cloud System Info($000A30)
;						-LOD Active (Byte)
;						-LOD Distance (Int)
;		Cloud Group ($000B00)
;			-Position Info ($000B10)
;			-Scale Info ($000B20)
;			-Type Info ($000B30)
;			-Color Info ($000B40)
;			-LOD Info ($000B50)
;			-Size Info ($000B60)
;			-Stretch Info($000B61)
;			-Alpha Info ($000B70)
;			-Particle Info ($000B80)
;						-Position Info ($000B81)
;						-Type Map Info ($000B82)
;						-Formation Refference ($000B83)
;<-End Format
Function SaveEZCloudFile(FileName$)
	Local C.CL,PSave=0
	EZFile_Init()
	EZFile_StartChunk($000001,"Header");Header
		EZFile_StartChunk($F00002,"Version");Version  Info
			EZFile_WriteInt($FFAFFA)
			EZFile_WriteFloat(SaveVersion#)
		EZFile_EndChunk()
		EZFile_StartChunk($000A00,"Builder");Builder Info
			EZFile_StartChunk($000A10,"Camera");Camera Info
				EZFile_WriteInt(CAMR)
				EZFile_WriteInt(CAMG)
				EZFile_WriteInt(CAMB)
				EZFile_WriteFloat(CamRX#)
				EZFile_WriteFloat(CamRY#)
				EZFile_WriteFloat(CamZoom#)
			EZFile_EndChunk()
			EZFile_StartChunk($000A20,"Objects");Ojects Info
				EZFile_WriteByte(ShowClParts)
				EZFile_WriteByte(ShowGrid)
			EZFile_EndChunk()
			EZFile_StartChunk($000A30,"Cloud System");Cloud System Info
				EZFile_WriteByte(Cloud_LODActive)
				EZFile_WriteInt(Cloud_LODDistance)
			EZFile_EndChunk()
		EZFile_EndChunk()
		For C.CL = Each CL
			EZFile_StartChunk($000B00,"Cloud Groups");Cloud Group
				EZFile_StartChunk($000B10,"Group Position");Position Info
					EZFile_WriteFloat(EntityX(C\CM,1))
					EZFile_WriteFloat(EntityY(C\CM,1))
					EZFile_WriteFloat(EntityZ(C\CM,1))
				EZFile_EndChunk()
				EZFile_StartChunk($000B20,"Group Scale");Scale Info
					EZFile_WriteFloat(C\SX#)
					EZFile_WriteFloat(C\SY#)
					EZFile_WriteFloat(C\SZ#)
				EZFile_EndChunk()
				EZFile_StartChunk($000B30,"Group Type");Type Info
					EZFile_Writeint(C\TType)
				EZFile_EndChunk()			
				EZFile_StartChunk($000B40,"Group Color");Color Info
					EZFile_Writeint(C\Colors[1])
					EZFile_Writeint(C\Colors[2])
					EZFile_Writeint(C\Colors[3])
					EZFile_Writeint(C\Colors[4])
				EZFile_EndChunk()
				EZFile_StartChunk($000B50,"Group LOD");LOD Info
					EZFile_WriteByte(C\LOD)
				EZFile_EndChunk()	
				EZFile_StartChunk($000B60,"Group Size");Size Info
					EZFile_WriteFloat(C\Size)
				EZFile_EndChunk()	
				EZFile_StartChunk($000B61,"Group Stretch");Stretch Info
					EZFile_WriteFloat(C\Stretch)
				EZFile_EndChunk()									
				EZFile_StartChunk($000B70,"Group Alpha");Alpha Info
					EZFile_WriteFloat(C\Alpha)
				EZFile_EndChunk()
				Num = Cloud_Get_ParticleCount(C\Cloud)
				For a = 1 To Num
					Ent = Cloud_Get_Particle(C\Cloud,a)
					EZFile_StartChunk($000B80,"PArticles");Particle Info
						EZFile_StartChunk($000B81,"PArticle Position");Position Info
							EZFile_WriteFloat(EntityX(Ent))
							EZFile_WriteFloat(EntityY(Ent))
							EZFile_WriteFloat(EntityZ(Ent))
						EZFile_EndChunk()
						EZFile_StartChunk($000B82,"PArticle Type");Type Map Info
							EZFile_WriteInt(Cloud_Get_ParticleType(C\Cloud,a))
						EZFile_EndChunk()
						EZFile_StartChunk($000B83,"Particle Formation");Formation Refference 
							EZFile_WriteFloat(Cloud_Get_ParticleFormation#(C\Cloud,a))
						EZFile_EndChunk()
					EZFile_EndChunk()
				Next
			EZFile_EndChunk()
		Next
	EZFile_EndChunk()
	EZFile_Write(FileName$)
	EZFile_Kill()
End Function

Function OpenEZCFile(FileName$,Mergeflag)
	Local LoadFile,ChunkID
	PT=0
	LoadFile =ReadFile(FileNAme$)
	If LoadFile = 0 Then Return 0
	ChunkID =ReadInt(LoadFile)
	If ChunkID = $000001 Then
		PLoad=0
		If Mergeflag=0 Then DeleteAll()
			EZC_ReadTemplate(LoadFile)
		CloseFile(LoadFile)
		Return 1
	Else
		CloseFile(LoadFile)
		Return 0
	EndIf
End Function
Global PLoad

Function EZC_ReadTemplate(LoadFile)
	Local ChunkSize=9,ChunkRead=8,ChunkType,ChunkData=0
	DBugCNT=DBugCNT+1
	ChunkSize =ReadInt(LoadFile)
	While ChunkRead < ChunkSize And Eof(LoadFile)=0
		ChunkData=0
		ChunkType =ReadInt(LoadFile)
		ChunkRead=ChunkRead+4
		Select ChunkType
			Case $000001 ChunkData=EZC_ReadTemplate(LoadFile)
			Case $F00002 ChunkData=EZC_REadVersion(LoadFile);Version
			Case $000A00 ChunkData=EZC_ReadTemplate(LoadFile);Builder Info
				Case $000A10 ChunkData=EZC_ReadCameraInfo(LoadFile);Camera
				Case $000A20 ChunkData=EZC_ReadObjectInfo(LoadFile);Objects
				Case $000A30 ChunkData=EZC_ReadCloudSysInfo(LoadFile);Cloud System
			Case $000B00 ChunkData=EZC_ReadGroup(LoadFile):PT=PT+1;Group
				Case $000B10 ChunkData=EZC_ReadGroupPosition(LoadFile);-Position Info ($000B10)
				Case $000B20 ChunkData=EZC_ReadGroupScale(LoadFile);-Scale Info ($000B20)
				Case $000B30 ChunkData=EZC_ReadGroupType(LoadFile);-Type Info ($000B30)
				Case $000B40 ChunkData=EZC_ReadGroupColor(LoadFile);-Color Info ($000B40)
				Case $000B50 ChunkData=EZC_ReadGroupLOD(LoadFile);-LOD Info ($000B50)
				Case $000B60 ChunkData=EZC_ReadGroupSize(LoadFile);-Size Info ($000B60)
				Case $000B61 ChunkData=EZC_ReadGroupstretch(LoadFile);-Stretch Info ($000B61)
				Case $000B70 ChunkData=EZC_ReadGroupAlpha(LoadFile);-Alpha Info ($000B70)
				Case $000B80 ChunkData=EZC_ReadGroupPArticle(LoadFile);Particle
					Case $000B81 ChunkData=EZC_ReadGroupPArticlePosition(LoadFile);-Position Info ($000B81)
					Case $000B82 ChunkData=EZC_ReadGroupPArticleType(LoadFile);-Type Map Info ($000B82)
					Case $000B83 ChunkData=EZC_ReadGroupPArticleFormation(LoadFile);-Formation Refference ($000B83)				
			Default
				ChunkData=EZFile_SkipChunk(LoadFile)
		End Select
		ChunkRead = ChunkRead + ChunkData
	Wend
	Return(ChunkRead-4)
End Function



Function EZC_ReadGroupPArticleFormation(LoadFile)
	ReadInt(LoadFile)
	Cloud_Set_ParticleFormation(EZC\Cloud, CPI,ReadFloat(LoadFile))
	Return 8
End Function
Function EZC_ReadGroupPArticleType(LoadFile)
	ReadInt(LoadFile)
	Cloud_Set_ParticleType(EZC\Cloud, CPI,ReadInt(LoadFile))
	Return 8
End Function
Function EZC_ReadGroupPArticlePosition(LoadFile)
	ReadInt(LoadFile)
	X#=ReadFloat(LoadFile)
	Y#=ReadFloat(LoadFile)
	Z#=ReadFloat(LoadFile)
	Cloud_Set_ParticlePosition(EZC\Cloud, CPI,X#,Y#,Z#)
	Return 16
End Function
Function EZC_ReadGroupPArticle(LoadFile)
	EZC\num=EZC\num+1
	CPI = Cloud_CreateParticle(EZC\Cloud,0,0,0,1, 1,EZC\Size#,EZC\Alpha#,EZC\LOD,EZC\Stretch#)
	Cloud_Set_ParticleColor(EZC\Cloud, CPI,1,RColor(EZC\Colors[1],RRed),RColor(EZC\Colors[1],RGreen),RColor(EZC\Colors[1],RBlue))
	Cloud_Set_ParticleColor(EZC\Cloud, CPI,2,RColor(EZC\Colors[2],RRed),RColor(EZC\Colors[2],RGreen),RColor(EZC\Colors[2],RBlue))
	Cloud_Set_ParticleColor(EZC\Cloud, CPI,3,RColor(EZC\Colors[3],RRed),RColor(EZC\Colors[3],RGreen),RColor(EZC\Colors[3],RBlue))
	Cloud_Set_ParticleColor(EZC\Cloud, CPI,4,RColor(EZC\Colors[4],RRed),RColor(EZC\Colors[4],RGreen),RColor(EZC\Colors[4],RBlue))
	Return EZC_ReadTemplate(LoadFile)
End Function
Function EZC_ReadGroupAlpha(LoadFile)
	ReadInt(LoadFile)
	EZC\Alpha#=ReadFloat(LoadFile)
	Return 8
End Function
Function EZC_ReadGroupSize(LoadFile)
	ReadInt(LoadFile)
	EZC\Size#=ReadFloat(LoadFile)
	Return 8
End Function
Function EZC_ReadGroupStretch(LoadFile)
	ReadInt(LoadFile)
	EZC\Stretch#=ReadFloat(LoadFile)
	Return 8
End Function
Function EZC_ReadGroupLOD(LoadFile)
	ReadInt(LoadFile)
	EZC\LOD=ReadByte(LoadFile)
	Return 5
End Function
Function EZC_ReadGroupColor(LoadFile)
	ReadInt(LoadFile)
	EZC\Colors[1]=ReadInt(LoadFile)
	EZC\Colors[2]=ReadInt(LoadFile)
	EZC\Colors[3]=ReadInt(LoadFile)
	EZC\Colors[4]=ReadInt(LoadFile)
	Return 20
End Function
Function EZC_ReadGroupType(LoadFile)
	ReadInt(LoadFile)
	EZC\TType =ReadInt(LoadFile)
	Return 8
End Function
Function EZC_ReadGroupScale(LoadFile)
	ReadInt(LoadFile)
	X# =ReadFloat(LoadFile)
	Y# =ReadFloat(LoadFile)
	Z# =ReadFloat(LoadFile)
	EZC\SX#=X#:EZC\SY#=Y#:EZC\SZ#=Z#
	ScaleEntity(EZC\CM,X#,Y#,Z#)
	ScaleEntity(EZC\Cloud,X#,Y#,Z#)
	Return 16
End Function
Function EZC_ReadGroupPosition(LoadFile)
	ReadInt(LoadFile)
	X# =ReadFloat(LoadFile)
	Y# =ReadFloat(LoadFile)
	Z# =ReadFloat(LoadFile)
	PositionEntity(EZC\CM,X#,Y#,Z#)
	PositionEntity(EZC\Cloud,X#,Y#,Z#)
	Return 16
End Function
Function EZC_ReadGroup(LoadFile)
	EZC.CL = New CL
	EZC\CM = CreateCube()
	EntityAlpha(EZC\CM,PPickAlpha#)
	EZC\ID = Handle(EZC)
	NameEntity(EZC\CM,EZC\ID)
	EntityColor(EZC\CM,150,150,150)
	EZC\Cloud=Cloud_CreateCloud(EntityX(EZC\CM,1),EntityY(EZC\CM,1),EntityZ(EZC\CM,1))		
	Return EZC_ReadTemplate(LoadFile)
End Function

Function EZC_REadVersion(LoadFile)
	ReadInt(LoadFile)
	ReadInt(LoadFile):ReadFloat(LoadFile)
	Return 12
End Function
Function EZC_ReadCameraInfo(LoadFile)
				ReadInt(LoadFile)
				CAMR =ReadInt(LoadFile)
				CAMG =ReadInt(LoadFile)
				CAMB =ReadInt(LoadFile)	
				CamRX =ReadFloat(LoadFile)		
				CamRY =ReadFloat(LoadFile)
				CamZoom =ReadFloat(LoadFile)
				CameraClsColor(MainCam,CAMR,CAMG,CAMB)
				CameraClsColor(RenderCam,CamR,CamG,CamB)
				Return 28
End Function
Function EZC_ReadObjectInfo(LoadFile)
				ReadInt(LoadFile)
				SG=ReadByte(LoadFile)
				ToggleGroups(SG)
				SG =ReadByte(LoadFile)
				ToggleGridPlane(SG)	
				Return 6
End Function
Function EZC_ReadCloudSysInfo(LoadFile)
				ReadInt(LoadFile)
				Cloud_LODActive=ReadByte(LoadFile)
				ToggleLOD(Cloud_LODActive)
				Cloud_LODDistance=ReadInt(LoadFile)
				GUI_SetVal(Prop15,Cloud_LODDistance)
				Return 9
End Function