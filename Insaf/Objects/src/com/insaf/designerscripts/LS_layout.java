package com.insaf.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[Layout/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="ImageView1.Top= 50%y-ImageView1.Height/2"[Layout/General script]
views.get("imageview1").vw.setTop((int)((50d / 100 * height)-(views.get("imageview1").vw.getHeight())/2d));
//BA.debugLineNum = 4;BA.debugLine="ImageView1.Left=50%x-ImageView1.Width/2"[Layout/General script]
views.get("imageview1").vw.setLeft((int)((50d / 100 * width)-(views.get("imageview1").vw.getWidth())/2d));

}
}