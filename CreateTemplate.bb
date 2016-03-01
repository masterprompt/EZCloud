;LoadTexture(Cloud_Dir$+"Data/Cloudtemplate.png",2+16+32)
OutFile$ = "cloudpack.ctp"
FileKey = 10
MyFile$ = "CloudLib/Data/Cloudtemplate.png"
MyFileSize = FileSize(MyFile$)
MyBank = CreateBank(MyFileSize)
FileIn = ReadFile(MyFile$)
ReadBytes(MyBank,fileIn,0,MyFileSize)
CloseFile(FileIn)
fileOut = WriteFile(OutFile$)
For A = 1 To FileKey
	WriteByte(fileOut,Rnd(0,255))
Next
WriteBytes(MyBank,FileOut,0,MyFileSize)
CloseFile(FileOut)
