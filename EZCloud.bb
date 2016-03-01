Include "EZCloud_Definitions.bb"
CallDLL("dll/EZCD1", "SetIconLg")

DemoApp = 0
Release = 1
CMD$ = CommandLine$()



If Release Then
	APDir$ = SystemProperty("appdir")
	ChangeDir(SystemProperty("appdir"))
EndIf

;Initialize the Application
App_Init()

;Check for a file to load
If CMD$ <> "" And Instr(CMD$," /S ",1)=0 Then PreloadFile(Replace(CMD$,Chr(34),""))

;Loop the Application	
App_Loop()
End