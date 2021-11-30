package com.insaf;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class general {
private static general mostCurrent = new general();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static boolean _notif_status = false;
public com.insaf.main _main = null;
public com.insaf.starter _starter = null;
public com.insaf.modul _modul = null;
public com.insaf.noconnection _noconnection = null;
public com.insaf.slidingpanels _slidingpanels = null;
public com.insaf.loadweb _loadweb = null;
public com.insaf.chat _chat = null;
public com.insaf.firebasemessaging _firebasemessaging = null;
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Dim notif_status as Boolean";
_notif_status = false;
 //BA.debugLineNum = 7;BA.debugLine="End Sub";
return "";
}
}
