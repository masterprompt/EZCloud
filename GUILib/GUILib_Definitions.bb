;=================GUI Library Definitions File============================

;:::::::::::::::::Includes
Include "GUILIB/GUILIB_Setup.bb"
Include "GUILIB/GUILIB_Core.bb"
Include "GUILIB/Dialog_Color.bb"

;:::::::::::::::::Globals
Global MainWin, MainView, MainCam
Global ViewX,ViewY,ViewW,ViewH
Global CamPiv1,CamPiv2
Global GridPlane

Global CamRX#,CamRY#,CamZoom#
Global ViewAction=0
Global GrabPlane=0
Global SelectAction=1,SelectActionSub=0
Global MX#,MY#,MZ#
Global AxisArrow=0
Global SPRP,SPRX,SPRY,SPRZ
Global ShowArrow=0
Global AxisSelected = 0
Global ShowGrid=1
Global SaveFileName$=""

Global mnu1,mnu2,mnu3,mnu4,mnu5,mnu6,mnu7,mnu8,mnu9,mnu10,mnu11,mnu12,mnu13,mnu14,mnu15,mnu16,mnu17,mnu18
Global mnu20,mnu21,mnu22,mnu23,mnu24,mnu25
Global btn1,btn2,btn3,btn4,btn5,btn6,btn7
Global Prop1,Prop2,Prop3,Prop4,Prop5,Prop6,Prop7,Prop8,Prop9,Prop10,Prop11,Prop12,Prop13,Prop14,Prop15,Prop16
Global LBL1,LBL2,LBL3,LBL4,LBL5,LBL6,LBL7
Global CCI1,CCI2,CCI3,CCI4
Global VecX#,VecY#,VDist#
Global ShowClParts=0
Global CAMR,CAMG,CAMB
Global NoViewInteractions
Global ConfigWin
Global ConFigWinOpen= 0
Global CNFG_L1,CNFG_L2,CNFG_L3,CNFG_L4,CNFG_L5,CNFG_L6,CNFG_L7,CNFG_L8
Global CNFG_List
Global GFXSelected
Global OLD_GFXSelected
Global OLD_GFXVW,OLD_GFXVH
Global GFXMs
Global GFXVW,GFXVH
Global GFXModes
Global RenderCam
;:::::::::::::::::Functions