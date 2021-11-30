package com.insaf;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class slidingpanels {
private static slidingpanels mostCurrent = new slidingpanels();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static int _panel_left = 0;
public static int _panel_right = 0;
public com.insaf.main _main = null;
public com.insaf.starter _starter = null;
public com.insaf.modul _modul = null;
public com.insaf.noconnection _noconnection = null;
public com.insaf.loadweb _loadweb = null;
public com.insaf.chat _chat = null;
public com.insaf.firebasemessaging _firebasemessaging = null;
public com.insaf.general _general = null;
public static class _slidingdata{
public boolean IsInitialized;
public int currentPanel;
public int targetPanel;
public boolean rotate;
public int duration;
public anywheresoftware.b4a.objects.PanelWrapper[] Panels;
public anywheresoftware.b4a.objects.AnimationWrapper[] slideAnimations;
public void Initialize() {
IsInitialized = true;
currentPanel = 0;
targetPanel = 0;
rotate = false;
duration = 0;
Panels = new anywheresoftware.b4a.objects.PanelWrapper[0];
{
int d0 = Panels.length;
for (int i0 = 0;i0 < d0;i0++) {
Panels[i0] = new anywheresoftware.b4a.objects.PanelWrapper();
}
}
;
slideAnimations = new anywheresoftware.b4a.objects.AnimationWrapper[0];
{
int d0 = slideAnimations.length;
for (int i0 = 0;i0 < d0;i0++) {
slideAnimations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _animationend(anywheresoftware.b4a.BA _ba,com.insaf.slidingpanels._slidingdata _sd) throws Exception{
int _i = 0;
 //BA.debugLineNum = 123;BA.debugLine="Sub AnimationEnd (sd As SlidingData)";
 //BA.debugLineNum = 124;BA.debugLine="sd.panels(sd.currentPanel).Left = 0 'Set the posi";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].setLeft((int) (0));
 //BA.debugLineNum = 125;BA.debugLine="For i = 0 To sd.panels.Length - 1";
{
final int step2 = 1;
final int limit2 = (int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 126;BA.debugLine="If i <> sd.currentPanel Then";
if (_i!=_sd.currentPanel /*int*/ ) { 
 //BA.debugLineNum = 127;BA.debugLine="sd.Panels(i).Left = 100%x 'Move all other panel";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_i].setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba));
 };
 }
};
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _changepanel(anywheresoftware.b4a.BA _ba,com.insaf.slidingpanels._slidingdata _sd) throws Exception{
int _relativeduration = 0;
int _secondaryindex = 0;
boolean _slideleft = false;
int _tox = 0;
String _leftpanel = "";
 //BA.debugLineNum = 31;BA.debugLine="Sub ChangePanel(sd As SlidingData)";
 //BA.debugLineNum = 32;BA.debugLine="Dim relativeDuration As Int";
_relativeduration = 0;
 //BA.debugLineNum = 33;BA.debugLine="Dim secondaryIndex As Int";
_secondaryindex = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim slideleft As Boolean : slideleft = False";
_slideleft = false;
 //BA.debugLineNum = 34;BA.debugLine="Dim slideleft As Boolean : slideleft = False";
_slideleft = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 37;BA.debugLine="If sd.targetPanel = PANEL_LEFT Then";
if (_sd.targetPanel /*int*/ ==_panel_left) { 
 //BA.debugLineNum = 38;BA.debugLine="sd.targetPanel = sd.currentPanel  + 1";
_sd.targetPanel /*int*/  = (int) (_sd.currentPanel /*int*/ +1);
 };
 //BA.debugLineNum = 42;BA.debugLine="If sd.targetPanel = PANEL_RIGHT Then";
if (_sd.targetPanel /*int*/ ==_panel_right) { 
 //BA.debugLineNum = 43;BA.debugLine="sd.targetPanel = sd.currentPanel - 1";
_sd.targetPanel /*int*/  = (int) (_sd.currentPanel /*int*/ -1);
 };
 //BA.debugLineNum = 47;BA.debugLine="If sd.currentPanel < sd.targetPanel Then slidelef";
if (_sd.currentPanel /*int*/ <_sd.targetPanel /*int*/ ) { 
_slideleft = anywheresoftware.b4a.keywords.Common.True;};
 //BA.debugLineNum = 50;BA.debugLine="If sd.targetPanel > sd.Panels.Length - 1 Then";
if (_sd.targetPanel /*int*/ >_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1) { 
 //BA.debugLineNum = 51;BA.debugLine="If sd.rotate Then";
if (_sd.rotate /*boolean*/ ) { 
 //BA.debugLineNum = 52;BA.debugLine="sd.targetPanel = 0";
_sd.targetPanel /*int*/  = (int) (0);
 }else {
 //BA.debugLineNum = 54;BA.debugLine="sd.targetPanel = sd.currentPanel";
_sd.targetPanel /*int*/  = _sd.currentPanel /*int*/ ;
 };
 };
 //BA.debugLineNum = 58;BA.debugLine="If sd.targetPanel < 0 Then";
if (_sd.targetPanel /*int*/ <0) { 
 //BA.debugLineNum = 59;BA.debugLine="If sd.rotate Then";
if (_sd.rotate /*boolean*/ ) { 
 //BA.debugLineNum = 60;BA.debugLine="sd.targetPanel = sd.Panels.Length - 1";
_sd.targetPanel /*int*/  = (int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1);
 }else {
 //BA.debugLineNum = 62;BA.debugLine="sd.targetPanel = sd.currentPanel";
_sd.targetPanel /*int*/  = _sd.currentPanel /*int*/ ;
 };
 };
 //BA.debugLineNum = 66;BA.debugLine="If sd.currentPanel = sd.targetPanel And sd.curren";
if (_sd.currentPanel /*int*/ ==_sd.targetPanel /*int*/  && _sd.currentPanel /*int*/ !=-1) { 
 //BA.debugLineNum = 67;BA.debugLine="relativeDuration = sd.duration / 100%x * Abs(sd.";
_relativeduration = (int) (_sd.duration /*int*/ /(double)anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)*anywheresoftware.b4a.keywords.Common.Abs(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()));
 //BA.debugLineNum = 70;BA.debugLine="If sd.Panels(sd.currentPanel).Left >= 0 Then";
if (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()>=0) { 
 //BA.debugLineNum = 71;BA.debugLine="secondaryIndex = sd.currentPanel - 1";
_secondaryindex = (int) (_sd.currentPanel /*int*/ -1);
 //BA.debugLineNum = 72;BA.debugLine="If secondaryIndex < 0 And sd.rotate Then second";
if (_secondaryindex<0 && _sd.rotate /*boolean*/ ) { 
_secondaryindex = (int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1);};
 }else {
 //BA.debugLineNum = 74;BA.debugLine="secondaryIndex = sd.currentPanel + 1";
_secondaryindex = (int) (_sd.currentPanel /*int*/ +1);
 //BA.debugLineNum = 75;BA.debugLine="If secondaryIndex > sd.Panels.Length - 1 Then";
if (_secondaryindex>_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1) { 
 //BA.debugLineNum = 76;BA.debugLine="If sd.rotate Then secondaryIndex = 0 Else seco";
if (_sd.rotate /*boolean*/ ) { 
_secondaryindex = (int) (0);}
else {
_secondaryindex = (int) (-1);};
 };
 };
 //BA.debugLineNum = 80;BA.debugLine="Dim toX As Int";
_tox = 0;
 //BA.debugLineNum = 82;BA.debugLine="toX = -sd.Panels(sd.currentPanel).Left";
_tox = (int) (-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft());
 //BA.debugLineNum = 85;BA.debugLine="sd.slideAnimations(1).InitializeTranslate(\"anima";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].InitializeTranslate(_ba,"animation1",(float) (0),(float) (0),(float) (_tox),(float) (0));
 //BA.debugLineNum = 86;BA.debugLine="sd.slideAnimations(1).Duration = relativeDuratio";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 87;BA.debugLine="sd.slideAnimations(1).Start(sd.panels(sd.current";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getObject()));
 //BA.debugLineNum = 90;BA.debugLine="If secondaryIndex <> -1 Then";
if (_secondaryindex!=-1) { 
 //BA.debugLineNum = 91;BA.debugLine="sd.slideAnimations(0).InitializeTranslate( \"ani";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].InitializeTranslate(_ba,"animation0",(float) (0),(float) (0),(float) (_tox),(float) (0));
 //BA.debugLineNum = 92;BA.debugLine="sd.slideAnimations(0).Duration = relativeDurati";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 93;BA.debugLine="sd.slideAnimations(0).Start(sd.panels(secondary";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_secondaryindex].getObject()));
 };
 }else {
 //BA.debugLineNum = 96;BA.debugLine="If slideleft Or sd.currentPanel = -1 Then";
if (_slideleft || _sd.currentPanel /*int*/ ==-1) { 
 //BA.debugLineNum = 97;BA.debugLine="relativeDuration = sd.duration / 100%x * Abs(sd";
_relativeduration = (int) (_sd.duration /*int*/ /(double)anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)*anywheresoftware.b4a.keywords.Common.Abs(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].getLeft()));
 //BA.debugLineNum = 98;BA.debugLine="If sd.currentPanel <> -1 Then";
if (_sd.currentPanel /*int*/ !=-1) { 
 //BA.debugLineNum = 99;BA.debugLine="sd.Panels(sd.targetPanel).Left = sd.Panels(sd.";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].setLeft((int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)));
 //BA.debugLineNum = 100;BA.debugLine="sd.slideAnimations(0).InitializeTranslate(\"ani";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].InitializeTranslate(_ba,"animation0",(float) (0),(float) (0),(float) (-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()),(float) (0));
 //BA.debugLineNum = 101;BA.debugLine="sd.slideAnimations(0).Duration = relativeDurat";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 102;BA.debugLine="sd.slideAnimations(0).Start(sd.panels(sd.curre";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getObject()));
 };
 //BA.debugLineNum = 104;BA.debugLine="sd.slideAnimations(1).InitializeTranslate( \"ani";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].InitializeTranslate(_ba,"animation1",(float) (0),(float) (0),(float) (-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].getLeft()),(float) (0));
 //BA.debugLineNum = 105;BA.debugLine="sd.slideAnimations(1).Duration = relativeDurati";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 106;BA.debugLine="sd.slideAnimations(1).Start(sd.panels(sd.target";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].getObject()));
 //BA.debugLineNum = 107;BA.debugLine="sd.currentPanel = sd.targetPanel";
_sd.currentPanel /*int*/  = _sd.targetPanel /*int*/ ;
 }else {
 //BA.debugLineNum = 109;BA.debugLine="sd.Panels(sd.targetPanel).Left = sd.Panels(sd.c";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].setLeft((int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)));
 //BA.debugLineNum = 110;BA.debugLine="relativeDuration = sd.duration / 100%x * Abs(10";
_relativeduration = (int) (_sd.duration /*int*/ /(double)anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)*anywheresoftware.b4a.keywords.Common.Abs(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()));
 //BA.debugLineNum = 111;BA.debugLine="leftPanel = (sd.currentPanel + sd.Panels.Length";
_leftpanel = BA.NumberToString((_sd.currentPanel /*int*/ +_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1)%_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length);
 //BA.debugLineNum = 112;BA.debugLine="sd.slideAnimations(0).InitializeTranslate(\"anim";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].InitializeTranslate(_ba,"animation0",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()),(float) (0));
 //BA.debugLineNum = 113;BA.debugLine="sd.slideAnimations(1).InitializeTranslate(\"anim";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].InitializeTranslate(_ba,"animation1",(float) (0),(float) (0),(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getLeft()),(float) (0));
 //BA.debugLineNum = 114;BA.debugLine="sd.slideAnimations(0).Duration = relativeDurati";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 115;BA.debugLine="sd.slideAnimations(1).Duration = relativeDurati";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].setDuration((long) (_relativeduration));
 //BA.debugLineNum = 116;BA.debugLine="sd.slideAnimations(0).Start(sd.panels(sd.curren";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (0)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.currentPanel /*int*/ ].getObject()));
 //BA.debugLineNum = 117;BA.debugLine="sd.slideAnimations(1).Start(sd.panels(sd.target";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/ [(int) (1)].Start((android.view.View)(_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_sd.targetPanel /*int*/ ].getObject()));
 //BA.debugLineNum = 118;BA.debugLine="sd.currentPanel = sd.targetPanel";
_sd.currentPanel /*int*/  = _sd.targetPanel /*int*/ ;
 };
 };
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.PanelWrapper  _createpageindicator(anywheresoftware.b4a.BA _ba,int _numberofpages,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _inactivebitmap,int _width,int _height) throws Exception{
int _imagesize = 0;
int _maxwidth = 0;
int _gap = 0;
int _top = 0;
anywheresoftware.b4a.objects.PanelWrapper _basepanel = null;
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
 //BA.debugLineNum = 132;BA.debugLine="Sub CreatePageIndicator(numberOfPages As Int, inac";
 //BA.debugLineNum = 133;BA.debugLine="Dim imageSize, maxWidth, Gap, Top As Int";
_imagesize = 0;
_maxwidth = 0;
_gap = 0;
_top = 0;
 //BA.debugLineNum = 135;BA.debugLine="maxWidth = Width / numberOfPages";
_maxwidth = (int) (_width/(double)_numberofpages);
 //BA.debugLineNum = 136;BA.debugLine="If maxWidth < Height Then";
if (_maxwidth<_height) { 
 //BA.debugLineNum = 137;BA.debugLine="imageSize = maxWidth";
_imagesize = _maxwidth;
 //BA.debugLineNum = 138;BA.debugLine="Top = (Height - maxWidth) / 2";
_top = (int) ((_height-_maxwidth)/(double)2);
 //BA.debugLineNum = 139;BA.debugLine="Gap = 0";
_gap = (int) (0);
 }else {
 //BA.debugLineNum = 141;BA.debugLine="imageSize = Height";
_imagesize = _height;
 //BA.debugLineNum = 142;BA.debugLine="Top = 0";
_top = (int) (0);
 //BA.debugLineNum = 143;BA.debugLine="Gap = (Width - numberOfPages * imageSize) / (num";
_gap = (int) ((_width-_numberofpages*_imagesize)/(double)(_numberofpages-1));
 };
 //BA.debugLineNum = 146;BA.debugLine="Dim basePanel As Panel";
_basepanel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 148;BA.debugLine="basePanel.Initialize(\"\")";
_basepanel.Initialize(_ba,"");
 //BA.debugLineNum = 149;BA.debugLine="For i = 0 To numberOfPages - 1";
{
final int step14 = 1;
final int limit14 = (int) (_numberofpages-1);
_i = (int) (0) ;
for (;_i <= limit14 ;_i = _i + step14 ) {
 //BA.debugLineNum = 150;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 152;BA.debugLine="iv.Initialize(\"\")";
_iv.Initialize(_ba,"");
 //BA.debugLineNum = 153;BA.debugLine="iv.Gravity=Gravity.FILL";
_iv.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 154;BA.debugLine="iv.Bitmap = inactiveBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_inactivebitmap.getObject()));
 //BA.debugLineNum = 155;BA.debugLine="basePanel.AddView(iv, i * (imageSize + Gap), Top";
_basepanel.AddView((android.view.View)(_iv.getObject()),(int) (_i*(_imagesize+_gap)),_top,_imagesize,_imagesize);
 }
};
 //BA.debugLineNum = 158;BA.debugLine="Return basePanel";
if (true) return _basepanel;
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return null;
}
public static String  _initialize(anywheresoftware.b4a.BA _ba,com.insaf.slidingpanels._slidingdata _sd,anywheresoftware.b4a.objects.PanelWrapper[] _panels,boolean _rotate,int _slidingduration) throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _a = null;
int _i = 0;
 //BA.debugLineNum = 14;BA.debugLine="Sub Initialize (sd As SlidingData, panels() As Pan";
 //BA.debugLineNum = 15;BA.debugLine="sd.duration = SlidingDuration";
_sd.duration /*int*/  = _slidingduration;
 //BA.debugLineNum = 16;BA.debugLine="sd.rotate = Rotate";
_sd.rotate /*boolean*/  = _rotate;
 //BA.debugLineNum = 17;BA.debugLine="sd.targetPanel = -1";
_sd.targetPanel /*int*/  = (int) (-1);
 //BA.debugLineNum = 18;BA.debugLine="sd.currentPanel = -1";
_sd.currentPanel /*int*/  = (int) (-1);
 //BA.debugLineNum = 19;BA.debugLine="sd.Panels = panels";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/  = _panels;
 //BA.debugLineNum = 20;BA.debugLine="Modul.tmrAnimation.Initialize(\"tmrAnimation\", 2)";
mostCurrent._modul._tmranimation /*anywheresoftware.b4a.objects.Timer*/ .Initialize((_ba.processBA == null ? _ba : _ba.processBA),"tmrAnimation",(long) (2));
 //BA.debugLineNum = 21;BA.debugLine="Dim a(2) As Animation";
_a = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (2)];
{
int d0 = _a.length;
for (int i0 = 0;i0 < d0;i0++) {
_a[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 22;BA.debugLine="sd.slideAnimations = a";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/  = _a;
 //BA.debugLineNum = 23;BA.debugLine="Dim a(2) As Animation";
_a = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (2)];
{
int d0 = _a.length;
for (int i0 = 0;i0 < d0;i0++) {
_a[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 24;BA.debugLine="sd.slideAnimations = a";
_sd.slideAnimations /*anywheresoftware.b4a.objects.AnimationWrapper[]*/  = _a;
 //BA.debugLineNum = 26;BA.debugLine="For i = 0 To sd.Panels.Length - 1";
{
final int step11 = 1;
final int limit11 = (int) (_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1);
_i = (int) (0) ;
for (;_i <= limit11 ;_i = _i + step11 ) {
 //BA.debugLineNum = 27;BA.debugLine="sd.Panels(i).Left = 100%x 'Move the panels right";
_sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [_i].setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba));
 }
};
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Dim PANEL_LEFT As Int : PANEL_LEFT = -2";
_panel_left = 0;
 //BA.debugLineNum = 3;BA.debugLine="Dim PANEL_LEFT As Int : PANEL_LEFT = -2";
_panel_left = (int) (-2);
 //BA.debugLineNum = 4;BA.debugLine="Dim PANEL_RIGHT As Int : PANEL_RIGHT = -3";
_panel_right = 0;
 //BA.debugLineNum = 4;BA.debugLine="Dim PANEL_RIGHT As Int : PANEL_RIGHT = -3";
_panel_right = (int) (-3);
 //BA.debugLineNum = 6;BA.debugLine="Type SlidingData (currentPanel As Int, _ 					  t";
;
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setpageindicator(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.PanelWrapper _pagepanel,int _page,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _activebitmap,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _inactivebitmap) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
int _i = 0;
 //BA.debugLineNum = 161;BA.debugLine="Sub SetPageIndicator(pagePanel As Panel, page As I";
 //BA.debugLineNum = 162;BA.debugLine="Dim v As View";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 163;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 165;BA.debugLine="For i = 0 To pagePanel.NumberOfViews - 1";
{
final int step3 = 1;
final int limit3 = (int) (_pagepanel.getNumberOfViews()-1);
_i = (int) (0) ;
for (;_i <= limit3 ;_i = _i + step3 ) {
 //BA.debugLineNum = 166;BA.debugLine="v = pagePanel.GetView(i)";
_v = _pagepanel.GetView(_i);
 //BA.debugLineNum = 167;BA.debugLine="If v Is ImageView Then";
if (_v.getObjectOrNull() instanceof android.widget.ImageView) { 
 //BA.debugLineNum = 168;BA.debugLine="iv = v";
_iv = (anywheresoftware.b4a.objects.ImageViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ImageViewWrapper(), (android.widget.ImageView)(_v.getObject()));
 //BA.debugLineNum = 169;BA.debugLine="If i = page Then";
if (_i==_page) { 
 //BA.debugLineNum = 170;BA.debugLine="iv.Bitmap = activeBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_activebitmap.getObject()));
 }else {
 //BA.debugLineNum = 172;BA.debugLine="iv.Bitmap = inactiveBitmap";
_iv.setBitmap((android.graphics.Bitmap)(_inactivebitmap.getObject()));
 };
 };
 }
};
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
}
