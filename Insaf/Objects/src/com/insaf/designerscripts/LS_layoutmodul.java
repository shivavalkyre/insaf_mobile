package com.insaf.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layoutmodul{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("webview1").vw.setLeft((int)(0d));
//BA.debugLineNum = 4;BA.debugLine="WebView1.Top=0"[LayoutModul/General script]
views.get("webview1").vw.setTop((int)(0d));
//BA.debugLineNum = 6;BA.debugLine="LiquidProgress1.Left=50%x-LiquidProgress1.Width/2"[LayoutModul/General script]
views.get("liquidprogress1").vw.setLeft((int)((50d / 100 * width)-(views.get("liquidprogress1").vw.getWidth())/2d));
//BA.debugLineNum = 7;BA.debugLine="LiquidProgress1.Top=50%y-LiquidProgress1.Height/2"[LayoutModul/General script]
views.get("liquidprogress1").vw.setTop((int)((50d / 100 * height)-(views.get("liquidprogress1").vw.getHeight())/2d));
//BA.debugLineNum = 9;BA.debugLine="Button1.Top=100%y-Button1.Height-20dip"[LayoutModul/General script]
views.get("button1").vw.setTop((int)((100d / 100 * height)-(views.get("button1").vw.getHeight())-(20d * scale)));
//BA.debugLineNum = 10;BA.debugLine="Button1.Left=100%x-Button1.Width-15dip"[LayoutModul/General script]
views.get("button1").vw.setLeft((int)((100d / 100 * width)-(views.get("button1").vw.getWidth())-(15d * scale)));

}
}