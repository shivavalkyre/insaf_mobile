package com.insaf;

import android.webkit.*;
import android.webkit.WebChromeClient.*;
import android.net.*;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class chat extends Activity implements B4AActivity{
	public static chat mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.insaf", "com.insaf.chat");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (chat).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.insaf", "com.insaf.chat");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.insaf.chat", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (chat) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (chat) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return chat.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (chat) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (chat) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            chat mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (chat) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static String _urls = "";
public static anywheresoftware.b4a.objects.IntentWrapper _i = null;
public static String _uri = "";
public static anywheresoftware.b4a.phone.Phone.ContentChooser _cc = null;
public anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
public com.insaf.main _main = null;
public com.insaf.starter _starter = null;
public com.insaf.modul _modul = null;
public com.insaf.noconnection _noconnection = null;
public com.insaf.slidingpanels _slidingpanels = null;
public com.insaf.loadweb _loadweb = null;
public com.insaf.firebasemessaging _firebasemessaging = null;
public com.insaf.general _general = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4j.object.JavaObject _client = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 23;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 25;BA.debugLine="Activity.LoadLayout(\"LayoutChat\")";
mostCurrent._activity.LoadLayout("LayoutChat",mostCurrent.activityBA);
 //BA.debugLineNum = 27;BA.debugLine="Dim client As JavaObject";
_client = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 28;BA.debugLine="client.InitializeNewInstance(Application.PackageN";
_client.InitializeNewInstance(anywheresoftware.b4a.keywords.Common.Application.getPackageName()+".chat$MyChromeClient",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 29;BA.debugLine="Dim jo As JavaObject = WebView1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._webview1.getObject()));
 //BA.debugLineNum = 30;BA.debugLine="jo.RunMethod(\"setWebChromeClient\", Array(client))";
_jo.RunMethod("setWebChromeClient",new Object[]{(Object)(_client.getObject())});
 //BA.debugLineNum = 32;BA.debugLine="I = GetIntent";
_i = _getintent();
 //BA.debugLineNum = 34;BA.debugLine="If I.Action = I.ACTION_VIEW Then";
if ((_i.getAction()).equals(_i.ACTION_VIEW)) { 
 //BA.debugLineNum = 36;BA.debugLine="URI=I.GetData";
_uri = _i.GetData();
 //BA.debugLineNum = 37;BA.debugLine="Log(URI)";
anywheresoftware.b4a.keywords.Common.LogImpl("23145742",_uri,0);
 };
 //BA.debugLineNum = 41;BA.debugLine="If URI.Length>0 Then";
if (_uri.length()>0) { 
 //BA.debugLineNum = 42;BA.debugLine="Dim jo As JavaObject = WebView1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._webview1.getObject()));
 //BA.debugLineNum = 43;BA.debugLine="jo.RunMethod(\"setWebChromeClient\", Array(client)";
_jo.RunMethod("setWebChromeClient",new Object[]{(Object)(_client.getObject())});
 //BA.debugLineNum = 44;BA.debugLine="WebView1.LoadUrl(URI)";
mostCurrent._webview1.LoadUrl(_uri);
 //BA.debugLineNum = 45;BA.debugLine="WebView1.ZoomEnabled=False";
mostCurrent._webview1.setZoomEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 47;BA.debugLine="Dim jo As JavaObject = WebView1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._webview1.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="jo.RunMethod(\"setWebChromeClient\", Array(client)";
_jo.RunMethod("setWebChromeClient",new Object[]{(Object)(_client.getObject())});
 //BA.debugLineNum = 49;BA.debugLine="WebView1.LoadUrl(urls)";
mostCurrent._webview1.LoadUrl(_urls);
 //BA.debugLineNum = 50;BA.debugLine="WebView1.ZoomEnabled=False";
mostCurrent._webview1.setZoomEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.IntentWrapper  _getintent() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _sr = null;
 //BA.debugLineNum = 62;BA.debugLine="Sub GetIntent As Intent";
 //BA.debugLineNum = 64;BA.debugLine="Dim sR As Reflector";
_sr = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 65;BA.debugLine="sR.Target=sR.GetActivity";
_sr.Target = (Object)(_sr.GetActivity(processBA));
 //BA.debugLineNum = 66;BA.debugLine="Return sR.RunMethod(\"getIntent\")";
if (true) return (anywheresoftware.b4a.objects.IntentWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.IntentWrapper(), (android.content.Intent)(_sr.RunMethod("getIntent")));
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 20;BA.debugLine="Private WebView1 As WebView";
mostCurrent._webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim urls As String =\"http://chat.disnavpriok.id:3";
_urls = "http://chat.disnavpriok.id:3001";
 //BA.debugLineNum = 10;BA.debugLine="Dim I As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim URI As String";
_uri = "";
 //BA.debugLineNum = 13;BA.debugLine="Private cc As ContentChooser";
_cc = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static void  _showfile_chooser(Object _filepathcallback,Object _filechooserparams) throws Exception{
ResumableSub_ShowFile_Chooser rsub = new ResumableSub_ShowFile_Chooser(null,_filepathcallback,_filechooserparams);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowFile_Chooser extends BA.ResumableSub {
public ResumableSub_ShowFile_Chooser(com.insaf.chat parent,Object _filepathcallback,Object _filechooserparams) {
this.parent = parent;
this._filepathcallback = _filepathcallback;
this._filechooserparams = _filechooserparams;
}
com.insaf.chat parent;
Object _filepathcallback;
Object _filechooserparams;
boolean _success = false;
String _dir = "";
String _filename = "";
anywheresoftware.b4j.object.JavaObject _jo = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 72;BA.debugLine="cc.Initialize(\"CC\")";
parent._cc.Initialize("CC");
 //BA.debugLineNum = 73;BA.debugLine="cc.Show(\"*/*\", \"Choose File\")";
parent._cc.Show(processBA,"*/*","Choose File");
 //BA.debugLineNum = 74;BA.debugLine="Wait For CC_Result (Success As Boolean, Dir As St";
anywheresoftware.b4a.keywords.Common.WaitFor("cc_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_success = (Boolean) result[0];
_dir = (String) result[1];
_filename = (String) result[2];
;
 //BA.debugLineNum = 75;BA.debugLine="Dim jo As JavaObject = Me";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(chat.getObject()));
 //BA.debugLineNum = 76;BA.debugLine="If Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_success) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 77;BA.debugLine="Log(FileName)";
anywheresoftware.b4a.keywords.Common.LogImpl("23735558",_filename,0);
 //BA.debugLineNum = 78;BA.debugLine="File.Copy(Dir, FileName, Starter.Provider.Shared";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,parent.mostCurrent._starter._provider /*com.insaf.fileprovider*/ ._sharedfolder /*String*/ ,"TempFile");
 //BA.debugLineNum = 79;BA.debugLine="jo.RunMethod(\"SendResult\", Array(Starter.Provide";
_jo.RunMethod("SendResult",new Object[]{parent.mostCurrent._starter._provider /*com.insaf.fileprovider*/ ._getfileuri /*Object*/ ("TempFile"),_filepathcallback});
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 82;BA.debugLine="jo.RunMethod(\"SendResult\", Array(Null, FilePathC";
_jo.RunMethod("SendResult",new Object[]{anywheresoftware.b4a.keywords.Common.Null,_filepathcallback});
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cc_result(boolean _success,String _dir,String _filename) throws Exception{
}
public static void SendResult(Uri uri, ValueCallback<Uri[]> filePathCallback) {
	if (uri != null)
		filePathCallback.onReceiveValue(new Uri[] {uri});
	else
		filePathCallback.onReceiveValue(null);
		
}
public static class MyChromeClient extends WebChromeClient {
@Override
 public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
            FileChooserParams fileChooserParams) {
		processBA.raiseEventFromUI(this, "showfile_chooser", filePathCallback, fileChooserParams);
        return true;
    }
	}
}
