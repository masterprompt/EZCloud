Global ExportVersion# = 1.0
;Save clf Format:
;----------------
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

Function ExportCloudFile(FileName$,Single=1)
	EZFile_Init()
	EZFile_StartChunk($000002);Header	
		EZFile_StartChunk($F00002);Version Info
			EZFile_WriteInt($FFFFFA)
			EZFile_WriteFloat(ExportVersion#)
		EZFile_EndChunk()
		If Single Then 
			ExportSingleCloud()
		Else 
			ExportMultipleClouds()
		EndIf
	EZFile_EndChunk()
	EZFile_Write(FileName$)
	EZFile_Kill()	
End Function

Function ExportSingleCloud()
	Local C.CL
		C.CL = First CL
		If C = Null Then Return 0
		EZFile_StartChunk($00A000);Cloud Chunk
		EZFile_StartChunk($00A001);Position Chunk
			EZFile_WriteFloat(0)
			EZFile_WriteFloat(0)
			EZFile_WriteFloat(0)
		EZFile_EndChunk()
		For C.CL = Each CL
			Num = Cloud_Get_ParticleCount(C\Cloud)
			For a = 1 To Num
				Ent = Cloud_Get_Particle(C\Cloud,a)
				EZFile_StartChunk($00A100);Particle Chunk
					EZFile_StartChunk($00A110);Particle Position Chunk
							TFormPoint(EntityX(Ent),EntityY(Ent),EntityZ(Ent),Ent,0)
							EZFile_WriteFloat(EntityX(Ent,1))
							EZFile_WriteFloat(EntityY(Ent,1))
							EZFile_WriteFloat(EntityZ(Ent,1))
					EZFile_EndChunk()
					EZFile_StartChunk($00A120);Particle Type Chunk
							EZFile_Writeint(Cloud_Get_ParticleType(C\Cloud, a))
					EZFile_EndChunk()
					
					EZFile_StartChunk($00A130);Particle color Chunk
							EZFile_Writeint(C\Colors[1])
							EZFile_Writeint(C\Colors[2])
							EZFile_Writeint(C\Colors[3])
							EZFile_Writeint(C\Colors[4])
					EZFile_EndChunk()
					EZFile_StartChunk($00A140);Particle lod Chunk
							EZFile_Writebyte(Cloud_Get_ParticleLOD(C\Cloud, a))
					EZFile_EndChunk()
					EZFile_StartChunk($00A150);Particle size Chunk
							EZFile_Writefloat(C\Size#)
					EZFile_EndChunk()
					EZFile_StartChunk($00A151);Particle Stretch Chunk
							EZFile_Writefloat(C\Stretch#)
					EZFile_EndChunk()					
					
					EZFile_StartChunk($00A160);Particle alpha Chunk
							EZFile_Writefloat(C\Alpha#)
					EZFile_EndChunk()
					EZFile_StartChunk($00A170);Particle formation Chunk
							EZFile_Writefloat(Cloud_Get_ParticleFormation#(C\Cloud, a))
					EZFile_EndChunk()																														
				EZFile_EndChunk()			
			Next
		Next
		EZFile_EndChunk()
End Function
Function ExportMultipleClouds()
	Local C.CL
		For C.CL = Each CL
			EZFile_StartChunk($00A000);Cloud Chunk
				EZFile_StartChunk($00A001);Position Chunk
					EZFile_WriteFloat(EntityX(C\CM,1))
					EZFile_WriteFloat(EntityY(C\CM,1))
					EZFile_WriteFloat(EntityZ(C\CM,1))
				EZFile_EndChunk()
				Num = Cloud_Get_ParticleCount(C\Cloud)
				For a = 1 To Num
					Ent = Cloud_Get_Particle(C\Cloud,a)
					EZFile_StartChunk($00A100);Particle Chunk
						EZFile_StartChunk($00A110);Particle Position Chunk
							EZFile_WriteFloat(EntityX(Ent,1)-EntityX(C\CM,1))
							EZFile_WriteFloat(EntityY(Ent,1)-EntityY(C\CM,1))
							EZFile_WriteFloat(EntityZ(Ent,1)-EntityZ(C\CM,1))
						EZFile_EndChunk()
						EZFile_StartChunk($00A120);Particle Type Chunk
								EZFile_Writeint(Cloud_Get_ParticleType(C\Cloud, a))
						EZFile_EndChunk()
						
						EZFile_StartChunk($00A130);Particle color Chunk
								EZFile_Writeint(C\Colors[1])
								EZFile_Writeint(C\Colors[2])
								EZFile_Writeint(C\Colors[3])
								EZFile_Writeint(C\Colors[4])
						EZFile_EndChunk()
						EZFile_StartChunk($00A140);Particle lod Chunk
								EZFile_Writebyte(Cloud_Get_ParticleLOD(C\Cloud, a))
						EZFile_EndChunk()
						EZFile_StartChunk($00A150);Particle size Chunk
								EZFile_Writefloat(C\Size#)
						EZFile_EndChunk()
						EZFile_StartChunk($00A151);Particle Stretch Chunk
								EZFile_Writefloat(C\Stretch#)
						EZFile_EndChunk()							
						EZFile_StartChunk($00A160);Particle alpha Chunk
								EZFile_Writefloat(C\Alpha#)
						EZFile_EndChunk()
						EZFile_StartChunk($00A170);Particle formation Chunk
								EZFile_Writefloat(Cloud_Get_ParticleFormation#(C\Cloud, a))
						EZFile_EndChunk()																														
					EZFile_EndChunk()			
				Next							
			EZFile_EndChunk()
		Next		
End Function