Const Timer_Max = 50

Type TimerList
	Field ID
	Field STime
	Field ETime
	Field Times[Timer_Max]
	Field Average
	Field Ref
	Field Recorded
End Type
;======================================Average Timer Functions
Function Timer_Create(Average=Timer_Max)
	Local TI.TimerList
	TI.TimerList= New TimerList
	TI\ID = Handle(TI.TimerList)
	If Average > Timer_Max Then Average = Timer_Max
	TI\Ref = Average
	Return TI\ID
End Function

Function Timer_StartTime(ID)
	Local TI.TimerList
	TI = Object.TimerList(ID)
	If TI <> Null Then
		TI\STime = MilliSecs()
	EndIf
End Function

Function Timer_EndTime(ID)
	Local TI.TimerList
	TI = Object.TimerList(ID)
	If TI <> Null Then
		TI\ETime = MilliSecs() - TI\STime
		TI\Average=0
		TI\Recorded = TI\Recorded + 1
		For X = 1 To TI\Ref
			TI\Times[x-1] = TI\Times[x]
			TI\Average = TI\Average + TI\Times[x]
		Next
			TI\Times[TI\Ref] = TI\ETime
			If TI\Recorded < TI\Ref Then
				TI\Average = TI\ETime
			Else
				If TI\Average = 0 Then TI\Average = 0 Else TI\Average = TI\Average/TI\Ref
			EndIf
	EndIf	
End Function

Function Timer_GetTime(ID)
	Local TI.TimerList
	TI = Object.TimerList(ID)
	If TI <> Null Then
		Return TI\Average
	EndIf
End Function
