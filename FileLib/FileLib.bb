Global FChunk_Bank					;Bank to hold our data
Global FChunk_S						;Current Offset
Global FChunk_Size					;Current Size
Type FChunk
	Field ID						;ID of Chunk
	Field S							;Start of chunk (bank offset)
	Field Size						;Size of chunk (byte size + Size int + ID int)
	Field Parent					;Handle of parent chunk
End Type

Function EZFile_Init()
	If FChunk_Bank <> 0 Then EZFile_Kill()
	FChunk_Bank = CreateBank(0)
	FChunk_S=0
	FChunk_Size=0
End Function
Function EZFile_Write(FileName$)
	Local MyFile
	MyFile = WriteFile(FileName)
	WriteBytes(FChunk_Bank,MyFile,0,FChunk_Size)
	CloseFile(MyFile)
End Function
Function EZFile_Kill()
	EZFile_EndAllChunks()
	If FChunk_Bank <> 0 Then
		FreeBank(FChunk_Bank)
		FChunk_Bank=0
	EndIf
	FChunk_S=0
	FChunk_Size=0	
	CloseFile(FChunk_Debug)
End Function
Function EZFile_StartChunk(ID,DBTXT$="")
	Local F.FChunk, Parent=0
	F.FChunk = Last FChunk
	If F = Null Then 
		Parent=0 
	Else 
		Parent= Handle(F)
		EZFile_ResizeChunks(8)
	EndIf
	F.FChunk = New FChunk
	F\ID = ID
	F\S = FChunk_S
	F\Size = 8
	FChunk_Size = FChunk_Size + 8
	FChunk_S= FChunk_S+8
	ResizeBank(FChunk_Bank,FChunk_Size)
	F\Parent = Parent
End Function
Function EZFile_EndChunk()
	Local F.FChunk
	F.FChunk = Last FChunk
	If F <> Null Then
		PokeInt(FChunk_Bank,F\S,F\ID)
		PokeInt(FChunk_Bank,F\S+4,F\Size)
		Delete(F.FChunk)
	EndIf
End Function
Function EZFile_EndAllChunks()
	Local F.FChunk
	For F.FChunk = Each FChunk
		PokeInt(FChunk_Bank,F\S,F\ID)
		PokeInt(FChunk_Bank,F\S+4,F\Size)
		Delete(F.FChunk)	
	Next
End Function
Function EZFile_ResizeChunks(SM)
	Local F.FChunk
	F.FChunk = Last FChunk
	If F<> Null Then
		F\Size=F\Size+SM
		If F\Parent <> 0 Then EZFile_ResizeChunkParent(F\Parent,SM)
	EndIf
End Function
Function EZFile_ResizeChunkParent(ID,SM)
	Local F.FChunk
	F = Object.FChunk(ID)
		F\Size=F\Size+SM
		If F\Parent <> 0 Then EZFile_ResizeChunkParent(F\Parent,SM)
End Function
Function EZFile_WriteByte(EByte)
	Local EZOFF = 1
	FChunk_Size=FChunk_Size+EZOFF
	ResizeBank(FChunk_Bank,FChunk_Size)
	PokeByte(FChunk_Bank,FChunk_S,EByte)
	FChunk_S=FChunk_S+EZOFF
	EZFile_ResizeChunks(EZOFF)
End Function
Function EZFile_Writeshort(EByte)
	Local EZOFF = 2
	FChunk_Size=FChunk_Size+EZOFF
	ResizeBank(FChunk_Bank,FChunk_Size)	
	PokeShort(FChunk_Bank,FChunk_S,EByte)
	FChunk_S=FChunk_S+EZOFF
	EZFile_ResizeChunks(EZOFF)
End Function
Function EZFile_Writeint(EByte)
	Local EZOFF = 4
	FChunk_Size=FChunk_Size+EZOFF
	ResizeBank(FChunk_Bank,FChunk_Size)	
	PokeInt(FChunk_Bank,FChunk_S,EByte)
	FChunk_S=FChunk_S+EZOFF
	EZFile_ResizeChunks(EZOFF)
End Function
Function EZFile_WriteFloat(EByte#)
	Local EZOFF = 4
	FChunk_Size=FChunk_Size+EZOFF
	ResizeBank(FChunk_Bank,FChunk_Size)	
	PokeFloat(FChunk_Bank,FChunk_S,EByte)
	FChunk_S=FChunk_S+EZOFF
	EZFile_ResizeChunks(EZOFF)
End Function
Function EZFile_WriteString(EByte$)
	Local EZOFF = Len(EByte$)+4
	FChunk_Size=FChunk_Size+EZOFF
	ResizeBank(FChunk_Bank,FChunk_Size)	
	PokeInt(FChunk_Bank,FChunk_S,EZOFF-4)
	FChunk_S=FChunk_S+4	
	For A = 1 To (EZOff-4)
		PokeByte(FChunk_Bank,FChunk_S,Asc(Mid(EByte$,A,1)))
		FChunk_S=FChunk_S+1
	Next
	EZFile_ResizeChunks(EZOFF)
End Function
Function EZFile_SkipChunk(FileHandle)
	Local ChunkSize, CurPos
	ChunkSize =ReadInt(FileHandle)
	CurPos = FilePos(FileHandle)
	SeekFile(FileHAndle,CurPos+(ChunkSize-8))
	Return ChunkSize-4
End Function