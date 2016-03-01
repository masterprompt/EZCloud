;GUI SYSTEM CONSTANTS

;WINDOW STATUS
Const win_NULL=0
Const win_OPEN=1
Const win_MIN=2
Const win_HIDE=3

;GADGET STATUS
Const gad_HIDE=0
Const gad_SHOW=1
Const gad_LOCK=2

;WINDOW MODE
Const Wmode_NONE=0
Const Wmode_SLIDER=1
Const Wmode_DRAG=2
Const Wmode_CHANGE=3
Const Wmode_RESIZE=4
Const Wmode_MIN=5
Const Wmode_MAX=6
Const Wmode_BACK=7
Const Wmode_MENU=8
Const Wmode_SELECTOR=9
Const Wmode_POPUP=10
Const Wmode_INPUT=11
Const Wmode_BLOCK=12
Const Wmode_BARCLICK=13

;INPUT MODE
Const inp_APPEND=0
Const inp_INSERT=1

;WINDOW FLAGS
Const flg_MENU=1
Const flg_QUIT=2
Const flg_MIN=4
Const flg_BACK=8
Const flg_DRAG=16
Const flg_SCALE=32
Const flg_BORDER=64
Const flg_GRAD=128
Const flg_DEFAULT=%00111111

Const mod_NORM=0
Const mod_MODAL=1
Const mod_SYSTEM=2

;MOUSE CLICK EVENTS
Const ms_NONE=0
Const ms_LMBHIT=1
Const ms_LMBHLD=2
Const ms_RMBHIT=3
Const ms_RMBHLD=4
Const ms_LMBREL=5
Const ms_RMBREL=6
Const ms_DBLCLK=7
Const ms_WAITRELEASE=8

;CURVED OR STRAIGHT GADGET BOXES
Const gad_STYLE=1

;GADGET TYPES
Const gad_BUTTON=0
Const gad_SWITCH=1
Const gad_CYCLE=2
Const gad_IMGBUT=3
Const gad_IMGRAD=4
Const gad_RADIO=5
Const gad_TICK=6
Const gad_PROP=7
Const gad_SLIDER=8
Const gad_INTEGER=9
Const gad_FLOAT=10
Const gad_TXTLIST=11
Const gad_TREELIST=12
Const gad_COMBO=13
Const gad_SELECTOR=14
Const gad_IMGBOX=15
Const gad_IMGDSP=16
Const gad_FRAME=17
Const gad_PANEL=18
Const gad_TOOLBAR=19
Const gad_MENUBUT=20
Const gad_TAB=21
Const gad_LABEL=22
Const gad_TEXTAREA=23
Const gad_TEXTINP=24
Const gad_3D=25
Const gad_IMGAREA=26

;GUI TYPE DEFINITIONS

Type FPS
	Field TIME
End Type

Type TMP_TABGAD
	Field GAD.GAD
End Type

Type WORD
	Field A$
End Type

Type CHAR
	Field ID
	Field A$
	Field W,H,IMG
	Field X,Y
End Type

Type AREA
	Field X,Y,W,H
End Type

Type PANEL
	Field PAR.PANEL
	Field X,Y,W,H
	Field ACT
End Type

Type DSPLST
	Field ITM.LISTITM
End Type

Type GADLST
	Field PAR.GAD
	Field GAD.GAD
	Field NXT.GADLST
	Field VIS
	Field ID
End Type

Type ACTGROUP
	Field GROUP
End Type

Type ACTGAD
	Field GAD.GAD
	Field GRP.ACTGROUP
End Type

Type LISTGROUP
	Field OBJ
	Field LIST.LIST
	Field MAX
	Field CUR
End Type

Type LISTITM
	Field ID,OBJ
	Field ID_OFFSET
	Field LIST.LIST
	Field GAD.GAD
	Field SUBGAD.GAD
	Field TYP
	Field TXT$
	Field ICON.ICON
	Field FLAG
	Field STATUS,ACTIVE
	Field X,Y,W,H,VIS,XP,YP,VAL,DATUM$
	Field INTV,INTMAX,INTMIN
	Field FLTV#,FLTMIN#,FLTMAX#
	Field PAD[3]
	Field FPAD#[3]
	Field PARENT.LISTITM
	Field OPEN,SELECTED
	Field ON
	Field GRP
	Field ITEMGRP.LISTGROUP
	Field TCOL,BCOL
	Field TIMER
End Type

Type LIST
	Field ID,OBJ
	Field TYP,CNT
	Field FLAG
	Field IMG
	Field GAD.GAD
	Field CUR_ITM.LISTITM
	Field FIRST_ITM.LISTITM
	Field TREE
	Field LISTGRP.LISTGROUP
End Type

Type ICON
	Field ID,OBJ
	Field W,H
	Field NAME$
	Field IMG[4]
End Type

Type SKIN
	Field ID,OBJ
	Field NAME$
	Field IMG[0]
	Field FLAG
End Type

Type TXTCOL
	Field INP.TXTAREA
	Field NXT.TXTCOL
	Field PAR.TXTCOL
	Field POS_1,POS_2
	Field COL
End Type

Type TXTWORD
	Field TXT$,TAB$
	Field AREA.TXTAREA
	Field BNK.TXTBNK
	Field NEWLINE
	Field X,Y,W,H
	Field POS1,POS2
End Type

Type TXTBNK
	Field ID,OBJ
	Field GAD.GAD
	Field AREA.TXTAREA
	Field INPUTPOS
	Field TXT$
	Field NXT.TXTBNK
	Field PAR.TXTBNK
	Field X,Y,W,H
	Field POS_START,POS_END
End Type

Type TXTAREA
	Field ID,OBJ
	Field GAD.GAD
	Field VIS_W,VIS_H
	Field TXT$
	Field IMG_W,IMG_H
	Field IMG_X,IMG_Y,TXT_X,TXT_Y,TXT_W,TXT_H,START_Y
	Field VPORTW,VPORTH
	Field IMG
	Field TYP
	Field WRAP
	Field MAX_CHAR	
	Field PASSWORD
	Field CLS_COL
	Field TXT_COL
	Field ALPHA.TXTBNK
	Field ZETA.TXTBNK
	Field MODE,MINUS
	Field INV_START,INV_END
	Field INPUTPOS
	Field INPUTBNK.TXTBNK
	Field WORD_ALPHA.TXTWORD
	Field WORD_ZETA.TXTWORD
	Field CUR_X,CUR_Y
	Field BLOCK
	Field EDIT
	Field TABVAL
	Field LINES,STARTLINE
	Field COLBNK.TXTCOL
	Field MX,MY,FLAG
End Type

Type WIN
	Field ID,OBJ
	Field TYP,STATUS,OLD_STATUS
	Field X,Y,W,H,TAB,XOFF,YOFF,MODAL,MENUX,MENUY
	Field PAD[4]
	Field TABX,TABY,TABW,TABH,MINH
	Field IMG,IMG2,IMG_SCALE,IMG_SCALE1
	Field FLAG
	Field ICON.ICON
	Field WCOL[5]
	Field BCOL[5]
	Field TITLE$
	Field TITLE_COL
	Field MENU.MENU
	Field FIRST_GAD.GAD
	Field LAST_GAD.GAD
	Field gad_MIN.GAD
	Field gad_BACK.GAD
	Field gad_QUIT.GAD
	Field PANEL.PANEL
	Field TAB_PANEL.PANEL
	Field PAN_ACT
	Field SKIN.SKIN[30]
	Field GRID
End Type

Type GAD
	Field ID,OBJ
	Field TYP
	Field WIN.WIN
	Field X,Y,W,H,TAB,ON,ACTIVE,GRP,X2,Y2,VX,VY,VW,VH,TW,TH,ACT_GRP
	Field TXT$
	Field CAP$,HELP$
	Field FLAG
	Field STATUS
	Field LIST.LIST
	Field VAL,MIN,MAX
	Field FVAL#,FMIN#,FMAX#
	Field IMG
	Field ICON.ICON
	Field COL[4]
	Field TXT_COL
	Field LINK.GAD[3]
	Field PAD[11]
	Field FPAD#[5]
	Field BSTYLE
	Field PARENT.GAD
	Field GADLST.GADLST
	Field PANEL.PANEL
	Field MENU.MENU
	Field TOOL.GAD
	Field INP.TXTAREA
End Type

Type MENUORDER
	Field MENU.MENU
	Field X,Y
	Field AX,AY,AW,AH
	Field IMG
End Type

Type MENU
	Field ID,OBJ
	Field WIN.WIN
	Field PARENT.MENU
	Field PAR_GAD.GAD
	Field TXT$
	Field ACT
	Field BARGAD.GAD
	Field GAD.GAD
	Field IMG
	Field W,H
	Field X,Y,TCOL
	Field TYP
	Field COL[7]
End Type

Type MENUITEM
	Field ID,OBJ
	Field MENU.MENU
	Field TYP
	Field GRP
	Field TXT$
	Field ICON.ICON
	Field ACT
	Field ON
	Field NXT_MNU.MENU
	Field TCOL
End Type

Type EVENT
	Field WIN_OVER			;CURRENT WINDOW OVER
	Field WIN					;CURRENT ACTIVE WIN
	
	Field GAD					;CURRENT ACTIVE PARENT GAD
	Field GAD_OVER			;CURRENT GAD OVER
	Field GAD_ACT			;CURRENT ACTIVE SUB-GAD OR MAIN GAD
	Field OVER
	Field INPUTGAD
	
	Field WIN_DRAG,WIN_CHANGE,WIN_MIN,WIN_MAX,WIN_KILL,WIN_SCALE			;WIN EVENTS
	Field GAD_HIT,GAD_HOLD,GAD_RELEASE,FOCUS		
	Field GAD_LMBHIT,GAD_LMBHOLD,GAD_LMBRELEASE
	Field GAD_RMBHIT,GAD_RMBHOLD,GAD_RMBRELEASE	
	Field GAD_LOCK										
	Field GAD_SCROLL																						
	Field GAD_NEWTEXT
	Field GAD_NEWVAL
	Field GAD_NEWSTATE
	Field MX,MY
	
	Field MOUSE_MOVE,MOUSE_X,MOUSE_Y
	
	Field GROUP
	Field GROUP_CHANGE
	
	Field SELECTOR_HIT
	
	Field LIST
	Field LISTITEM
	Field ITM_SELECT
	Field ITM_RMBHIT
	Field ITM_DBLCLICK
	Field ITM_TOGGLE
	Field ITM_GRP_TOGGLE
	Field ITM_GRP
	
	Field MENU_OPEN
	Field MENU
	Field MENU_ITEM
	Field MENU_TOGGLE
	Field MENU_GRP_TOGGLE
	Field MENU_GRP
		
End Type

;EXTERNAL [USER] EVENTS
Global EVENT.EVENT=New EVENT

Global GUI_DEBUG=False
Global GUI_FPSTIMER,GUI_FPSOLDTIMER,GUI_FPSCOUNT

;INTERNAL PROCESSES & EVENTS
Global GUI_WINMODE
Global GUI_GADMODE
Global GUI_ACTIVEWIN.WIN
Global GUI_WINOVER.WIN
Global GUI_GADOVER.GAD
Global GUI_ANYGADOVER.GAD
Global GUI_ACTIVEGAD.GAD
Global GUI_INPUTGAD.GAD
Global GUI_MENUOVER.MENU
Global GUI_MENUHIT.MENUITEM
Global GUI_PREVGAD.GAD
Global GUI_GADHIT.GAD
Global GUI_LSTITMHIT.LISTITM
Global GUI_SELECTOR.GAD		;CURRENT SELECTOR GADGET
Global GUI_3DCAM
Global GUI_SCROLL_LOCK=False
Global GUI_HELPX,GUI_HELPY,GUI_HELPTIMER,GUI_HELP
Global GUI_INVCOL=$2299BB
Global GUI_BLOCKGAD.GAD
Global GUI_FOCUS

Global GUI_QMENU_ACTIVE=True
Global GUI_QMENU
Global GUI_QMENU_OPEN
Global GUI_POPUP

Global GUI_CLICKEVENT			;MOUSE EVENT
Global GUI_WIN_EVENT				;WIN EVENT
Global GUI_GAD_EVENT			;GAD EVENT
Global GUI_DBLCLK					;DOUBLE LMB CLICK?
Global GUI_LMBDWN					;QUICK MOUSE BUTTON TESTS
Global GUI_RMBDWN
Global GUI_CLKCNT=0				;DOUBLE CLICK COUNTER
Global GUI_BLOCK
Global GUI_BLOCKSCROLL
Global GUI_INVY

Global GUI_ITM=1					;CURRENT ITEM ID

Global GUI_GFXDIR$="DATA/GFX/"

;INTERNAL GFX, ICONS & FONTS
Global GUI_CLS_COLOR
Global GUI_GFXW=800
Global GUI_GFXH=600
Global GUI_GFXDEBUG;=True
Global GUI_MOUSE_DRAW=True
Global GUI_MOUSE
Global GUI_MOUSE_POINTER
Global GUI_MOUSE_HSLIDER
Global GUI_MOUSE_VSLIDER
Global GUI_MOUSE_WDRAG
Global GUI_MOUSE_WSCALE
Global GUI_IMG_SCALE
Global GUI_IMG_SCALE1
Global GUI_FONT
Global GUI_FONTBOLD
Global GUI_FONTIMG_ALPHA
Global GUI_FONTIMG_BETA
Global GUI_FONTIMG_INV
Global GUI_FONTIMG_INVBETA
Global GUI_FONTCOL
Global GUI_FONTMASK
Global GUI_VPX
Global GUI_VPY
Global GUI_VPW
Global GUI_VPH
Global GUI_OX
Global GUI_OY
Global GUI_INVSTART=-1
Global GUI_INVEND=-1

Dim GUI_CHAR.CHAR(256)


Global GUI_WINCOL					;WINDOW COLOR
Global GUI_WINHILITE				;WINDOW HILITE
Global GUI_WINLOLITE				;WINDOW LOLITE
Global GUI_WINBORDER			;WINDOW TITLEBAR COLOR
Global GUI_GADCOL					;GADGET COLOR
Global GUI_GADHILITE				;GADGET HILITE
Global GUI_GADLOLITE				;GADGET LOLITE
Global GUI_GADTXCOL				;GADGET TEXT COLOR
Global GUI_WINTXTCOL

;INPUT STUFF
Global GUI_INPUTBNK.TXTBNK
Global GUI_CURX=-1
Global GUI_CURY=-1
Global GUI_CURTIMER=-1
Global GUI_KEYDOWN=-1
Global GUI_GETKEY=0
Global GUI_OLDKEY=0
Global GUI_OLDKEYDOWN
Global GUI_FLASH=True
Global GUI_REPEAT=0
Global GUI_REP=True
Global GUI_REPTIMER
Global GUI_INPUTMODE
Global GUI_INPUTPOS
Global GUI_SHIFT
Global GUI_CTRL
Global GUI_ALT


Global GUI_SHADOW					;SHADOW CROSS-HATCH IMG

Global icn_HSLIDER
Global icn_VSLIDER
Global icn_DEFAULT.ICON
Global icn_TICKBOX.ICON
Global icn_CHKBOX.ICON
Global icn_LARROW.ICON
Global icn_RARROW.ICON
Global icn_UARROW.ICON
Global icn_DARROW.ICON
Global icn_INCARROW.ICON
Global icn_DECARROW.ICON
Global icn_LISTDIR.ICON
Global icn_CYCLE.ICON
Global icn_MIN.ICON
Global icn_BACK.ICON
Global icn_QUIT.ICON
Global icn_PANEL.ICON
