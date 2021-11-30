package com.insaf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Path.FillType;
import android.graphics.Paint;
import android.graphics.Color;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;

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

public class modul extends Activity implements B4AActivity{
	public static modul mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.insaf", "com.insaf.modul");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (modul).");
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
		activityBA = new BA(this, layout, processBA, "com.insaf", "com.insaf.modul");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.insaf.modul", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (modul) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (modul) Resume **");
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
		return modul.class;
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
            BA.LogInfo("** Activity (modul) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (modul) Pause event (activity is not paused). **");
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
            modul mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (modul) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _tmranimation = null;
public static int _currentpanelbeforepaused = 0;
public static anywheresoftware.b4j.object.JavaObject _nativeme = null;
public com.insaf.slidingpanels._slidingdata _sd = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnleft = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnright = null;
public anywheresoftware.b4a.objects.PanelWrapper _indicator = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _activebitmap = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _inactivebitmap = null;
public static float _startx = 0f;
public static float _starty = 0f;
public static float _lastx = 0f;
public static long _lastmove = 0L;
public static int _img_width1 = 0;
public static int _img_height1 = 0;
public static int _img_width2 = 0;
public static int _img_height2 = 0;
public static int _img_width3 = 0;
public static int _img_height3 = 0;
public static String _label_height = "";
public static int _current_panel = 0;
public anywheresoftware.b4a.objects.PanelWrapper[] _panels = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _labels1 = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _labels2 = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _labels3 = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _labels4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _browser = null;
public anywheresoftware.b4a.objects.CSBuilder _cs = null;
public anywheresoftware.b4a.objects.CSBuilder _cs1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _button1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm2 = null;
public com.insaf.main _main = null;
public com.insaf.starter _starter = null;
public com.insaf.noconnection _noconnection = null;
public com.insaf.slidingpanels _slidingpanels = null;
public com.insaf.loadweb _loadweb = null;
public com.insaf.chat _chat = null;
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
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _img_onboard = null;
anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _cd = null;
int _borderwidth1 = 0;
int _bordercolor1 = 0;
 //BA.debugLineNum = 48;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 53;BA.debugLine="Activity.Title=\"\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 59;BA.debugLine="For i = 0 To panels.Length-1";
{
final int step2 = 1;
final int limit2 = (int) (mostCurrent._panels.length-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 60;BA.debugLine="panels(i).Initialize(\"panels\")";
mostCurrent._panels[_i].Initialize(mostCurrent.activityBA,"panels");
 //BA.debugLineNum = 62;BA.debugLine="panels(i).Color = Colors.RGB(253,183,21)";
mostCurrent._panels[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (253),(int) (183),(int) (21)));
 //BA.debugLineNum = 71;BA.debugLine="If i<=panels.Length Then";
if (_i<=mostCurrent._panels.length) { 
 //BA.debugLineNum = 74;BA.debugLine="If i =0 Then";
if (_i==0) { 
 //BA.debugLineNum = 75;BA.debugLine="Dim img_onboard As ImageView";
_img_onboard = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 76;BA.debugLine="img_onboard.Initialize(\"img_onboard\")";
_img_onboard.Initialize(mostCurrent.activityBA,"img_onboard");
 //BA.debugLineNum = 77;BA.debugLine="img_onboard.Bitmap=LoadBitmap(File.DirAssets,\"";
_img_onboard.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Group 2992.png").getObject()));
 //BA.debugLineNum = 78;BA.debugLine="img_onboard.Gravity= Gravity.FILL";
_img_onboard.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 79;BA.debugLine="panels(i).AddView(img_onboard,50%x-img_width1/";
mostCurrent._panels[_i].AddView((android.view.View)(_img_onboard.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-_img_width1/(double)2),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-_img_height1/(double)2),_img_width1,_img_height1);
 //BA.debugLineNum = 81;BA.debugLine="labels1(0).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (0)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 82;BA.debugLine="labels1(1).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (1)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 83;BA.debugLine="cs.Initialize.Typeface(Typeface.LoadFromAssets(";
mostCurrent._cs.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("Selamat datang di ")).Pop().Bold().Append(BA.ObjectToCharSequence("INSAF")).PopAll();
 //BA.debugLineNum = 84;BA.debugLine="labels1(0).Text = cs";
mostCurrent._labels1[(int) (0)].setText(BA.ObjectToCharSequence(mostCurrent._cs.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="labels1(0).TextColor=Colors.Black";
mostCurrent._labels1[(int) (0)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 87;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAssets";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Pop().Bold().Append(BA.ObjectToCharSequence("INFORMATION AND SAFETY")).PopAll();
 //BA.debugLineNum = 88;BA.debugLine="labels1(1).Text = cs1";
mostCurrent._labels1[(int) (1)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 89;BA.debugLine="labels1(1).TextColor=Colors.Black";
mostCurrent._labels1[(int) (1)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 92;BA.debugLine="labels4(0).Initialize(\"labels4\")";
mostCurrent._labels4[(int) (0)].Initialize(mostCurrent.activityBA,"labels4");
 //BA.debugLineNum = 95;BA.debugLine="labels4(0).Text = \"Lanjut\"";
mostCurrent._labels4[(int) (0)].setText(BA.ObjectToCharSequence("Lanjut"));
 //BA.debugLineNum = 96;BA.debugLine="labels4(0).Typeface= Typeface.LoadFromAssets(\"P";
mostCurrent._labels4[(int) (0)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf"));
 //BA.debugLineNum = 97;BA.debugLine="labels4(0).textColor=Colors.Black";
mostCurrent._labels4[(int) (0)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 98;BA.debugLine="labels4(0).Typeface= Typeface.DEFAULT_BOLD";
mostCurrent._labels4[(int) (0)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 99;BA.debugLine="labels4(0).Gravity= Bit.Or(Gravity.CENTER, Grav";
mostCurrent._labels4[(int) (0)].setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 101;BA.debugLine="panels(i).AddView(labels1(0),0dip,50%y,100%x,60";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (0)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 102;BA.debugLine="panels(i).AddView(labels1(1),0dip,labels1(0).T";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (1)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (mostCurrent._labels1[(int) (0)].getTop()+mostCurrent._labels1[(int) (0)].getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 103;BA.debugLine="panels(i).AddView(labels4(0),100%x-120dip,100%y";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels4[(int) (0)].getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-(double)(Double.parseDouble(mostCurrent._label_height))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 }else if(_i==1) { 
 //BA.debugLineNum = 107;BA.debugLine="Dim img_onboard As ImageView";
_img_onboard = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 108;BA.debugLine="img_onboard.Initialize(\"img_onboard\")";
_img_onboard.Initialize(mostCurrent.activityBA,"img_onboard");
 //BA.debugLineNum = 109;BA.debugLine="img_onboard.Bitmap=LoadBitmap(File.DirAssets,\"";
_img_onboard.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Group 2990.png").getObject()));
 //BA.debugLineNum = 110;BA.debugLine="img_onboard.Gravity= Gravity.FILL";
_img_onboard.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 111;BA.debugLine="panels(i).AddView(img_onboard,50%x-img_width2/";
mostCurrent._panels[_i].AddView((android.view.View)(_img_onboard.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-_img_width2/(double)2),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-_img_height2/(double)2),_img_width2,_img_height2);
 //BA.debugLineNum = 113;BA.debugLine="labels1(0).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (0)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 114;BA.debugLine="labels1(1).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (1)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 115;BA.debugLine="labels1(2).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (2)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 116;BA.debugLine="labels1(3).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (3)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 117;BA.debugLine="labels1(4).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (4)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 118;BA.debugLine="labels4(1).Initialize(\"labels4\")";
mostCurrent._labels4[(int) (1)].Initialize(mostCurrent.activityBA,"labels4");
 //BA.debugLineNum = 120;BA.debugLine="cs.Initialize.Typeface(Typeface.LoadFromAssets";
mostCurrent._cs.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("Aplikasi ")).Pop().Bold().Append(BA.ObjectToCharSequence("INSAF ")).Pop().Append(BA.ObjectToCharSequence("bertujuan untuk menyimpan")).PopAll();
 //BA.debugLineNum = 121;BA.debugLine="labels1(0).Text=cs";
mostCurrent._labels1[(int) (0)].setText(BA.ObjectToCharSequence(mostCurrent._cs.getObject()));
 //BA.debugLineNum = 122;BA.debugLine="labels1(0).textColor=Colors.Black";
mostCurrent._labels1[(int) (0)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 124;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("")).Pop().Bold().Append(BA.ObjectToCharSequence("Berita Telekomunikasi Pelayaran")).Pop().Append(BA.ObjectToCharSequence(" yang menjadi")).PopAll();
 //BA.debugLineNum = 125;BA.debugLine="labels1(1).Text =cs1";
mostCurrent._labels1[(int) (1)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 126;BA.debugLine="labels1(1).textColor=Colors.Black";
mostCurrent._labels1[(int) (1)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 128;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("salah satu fungsi dan tugas pokok")).PopAll();
 //BA.debugLineNum = 129;BA.debugLine="labels1(2).Text =cs1";
mostCurrent._labels1[(int) (2)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="labels1(2).textColor=Colors.Black";
mostCurrent._labels1[(int) (2)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 132;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("")).Pop().Bold().Append(BA.ObjectToCharSequence("Distrik Navigasi Kelas I Tanjung Priok")).PopAll();
 //BA.debugLineNum = 133;BA.debugLine="labels1(3).Text =cs1";
mostCurrent._labels1[(int) (3)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 134;BA.debugLine="labels1(3).textColor=Colors.Black";
mostCurrent._labels1[(int) (3)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 137;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("dalam bentuk digital")).PopAll();
 //BA.debugLineNum = 138;BA.debugLine="labels1(4).Text =cs1";
mostCurrent._labels1[(int) (4)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 139;BA.debugLine="labels1(4).textColor=Colors.Black";
mostCurrent._labels1[(int) (4)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 141;BA.debugLine="labels4(1).Text = \"Lanjut\"";
mostCurrent._labels4[(int) (1)].setText(BA.ObjectToCharSequence("Lanjut"));
 //BA.debugLineNum = 142;BA.debugLine="labels4(1).Typeface= Typeface.LoadFromAssets(\"";
mostCurrent._labels4[(int) (1)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf"));
 //BA.debugLineNum = 143;BA.debugLine="labels4(1).textColor=Colors.Black";
mostCurrent._labels4[(int) (1)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 144;BA.debugLine="labels4(1).Typeface= Typeface.DEFAULT_BOLD";
mostCurrent._labels4[(int) (1)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 145;BA.debugLine="labels4(1).Gravity= Bit.Or(Gravity.CENTER, Gra";
mostCurrent._labels4[(int) (1)].setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 149;BA.debugLine="panels(i).AddView(labels1(0),0dip,50%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (0)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 150;BA.debugLine="panels(i).AddView(labels1(1),0dip,54%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (1)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (54),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 151;BA.debugLine="panels(i).AddView(labels1(2),0dip,58%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (2)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (58),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 152;BA.debugLine="panels(i).AddView(labels1(3),0dip,62%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (3)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (62),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 153;BA.debugLine="panels(i).AddView(labels1(4),0dip,66%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (4)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (66),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 155;BA.debugLine="panels(i).AddView(labels4(1),100%x-120dip,100%";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels4[(int) (1)].getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-(double)(Double.parseDouble(mostCurrent._label_height))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 }else {
 //BA.debugLineNum = 158;BA.debugLine="Dim img_onboard As ImageView";
_img_onboard = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 159;BA.debugLine="img_onboard.Initialize(\"img_onboard\")";
_img_onboard.Initialize(mostCurrent.activityBA,"img_onboard");
 //BA.debugLineNum = 160;BA.debugLine="img_onboard.Bitmap=LoadBitmap(File.DirAssets,\"";
_img_onboard.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Group 2489.png").getObject()));
 //BA.debugLineNum = 161;BA.debugLine="img_onboard.Gravity= Gravity.FILL";
_img_onboard.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 162;BA.debugLine="panels(i).AddView(img_onboard,50%x-img_width3/";
mostCurrent._panels[_i].AddView((android.view.View)(_img_onboard.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-_img_width3/(double)2),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-_img_height3/(double)2),_img_width3,_img_height3);
 //BA.debugLineNum = 164;BA.debugLine="labels1(0).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (0)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 165;BA.debugLine="labels1(1).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (1)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 166;BA.debugLine="labels1(2).Initialize(\"labels1\")";
mostCurrent._labels1[(int) (2)].Initialize(mostCurrent.activityBA,"labels1");
 //BA.debugLineNum = 169;BA.debugLine="labels4(1).Initialize(\"labels4\")";
mostCurrent._labels4[(int) (1)].Initialize(mostCurrent.activityBA,"labels4");
 //BA.debugLineNum = 171;BA.debugLine="cs.Initialize.Typeface(Typeface.LoadFromAssets";
mostCurrent._cs.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("Aplikasi ")).Pop().Bold().Append(BA.ObjectToCharSequence("INSAF ")).Pop().Append(BA.ObjectToCharSequence("merupakan aplikasi internal")).PopAll();
 //BA.debugLineNum = 172;BA.debugLine="labels1(0).Text=cs";
mostCurrent._labels1[(int) (0)].setText(BA.ObjectToCharSequence(mostCurrent._cs.getObject()));
 //BA.debugLineNum = 173;BA.debugLine="labels1(0).textColor=Colors.Black";
mostCurrent._labels1[(int) (0)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 175;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("bidang ")).Pop().Bold().Append(BA.ObjectToCharSequence("Telekomunikasi Pelayaran")).PopAll();
 //BA.debugLineNum = 176;BA.debugLine="labels1(1).Text =cs1";
mostCurrent._labels1[(int) (1)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 177;BA.debugLine="labels1(1).textColor=Colors.Black";
mostCurrent._labels1[(int) (1)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 179;BA.debugLine="cs1.Initialize.Typeface(Typeface.LoadFromAsset";
mostCurrent._cs1.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence("")).Pop().Bold().Append(BA.ObjectToCharSequence("Distrik Navigasi Kelas I Tanjung Priok")).PopAll();
 //BA.debugLineNum = 180;BA.debugLine="labels1(2).Text =cs1";
mostCurrent._labels1[(int) (2)].setText(BA.ObjectToCharSequence(mostCurrent._cs1.getObject()));
 //BA.debugLineNum = 181;BA.debugLine="labels1(2).textColor=Colors.Black";
mostCurrent._labels1[(int) (2)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 184;BA.debugLine="labels4(1).Text = \"Mulai\"";
mostCurrent._labels4[(int) (1)].setText(BA.ObjectToCharSequence("Mulai"));
 //BA.debugLineNum = 185;BA.debugLine="labels4(1).Typeface= Typeface.LoadFromAssets(\"";
mostCurrent._labels4[(int) (1)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("Poppins-Regular.ttf"));
 //BA.debugLineNum = 186;BA.debugLine="labels4(1).textColor=Colors.Black";
mostCurrent._labels4[(int) (1)].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 187;BA.debugLine="labels4(1).Typeface= Typeface.DEFAULT_BOLD";
mostCurrent._labels4[(int) (1)].setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 188;BA.debugLine="labels4(1).Gravity= Bit.Or(Gravity.CENTER, Gra";
mostCurrent._labels4[(int) (1)].setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 192;BA.debugLine="panels(i).AddView(labels1(0),0dip,50%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (0)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 193;BA.debugLine="panels(i).AddView(labels1(1),labels1(1).Left,5";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (1)].getObject()),mostCurrent._labels1[(int) (1)].getLeft(),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (54),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 194;BA.debugLine="panels(i).AddView(labels1(2),0dip,58%y,100%x,6";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels1[(int) (2)].getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (58),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 197;BA.debugLine="panels(i).AddView(labels4(1),100%x-120dip,100%";
mostCurrent._panels[_i].AddView((android.view.View)(mostCurrent._labels4[(int) (1)].getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-(double)(Double.parseDouble(mostCurrent._label_height))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 };
 };
 //BA.debugLineNum = 202;BA.debugLine="Activity.AddView(panels(i), 100%x, 0, 100%x, 100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panels[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 203;BA.debugLine="Activity.AddMenuItem(\"Panel #\" & i, \"Menu\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Panel #"+BA.NumberToString(_i)),"Menu");
 }
};
 //BA.debugLineNum = 207;BA.debugLine="btnLeft.Initialize(\"Left\")";
mostCurrent._btnleft.Initialize(mostCurrent.activityBA,"Left");
 //BA.debugLineNum = 208;BA.debugLine="btnLeft.Text = \">\"";
mostCurrent._btnleft.setText(BA.ObjectToCharSequence(">"));
 //BA.debugLineNum = 209;BA.debugLine="Activity.AddView(btnLeft, 60%x, 100%y - 75dip, 10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._btnleft.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 210;BA.debugLine="btnRight.Initialize(\"Right\")";
mostCurrent._btnright.Initialize(mostCurrent.activityBA,"Right");
 //BA.debugLineNum = 211;BA.debugLine="btnRight.Text = \"<\"";
mostCurrent._btnright.setText(BA.ObjectToCharSequence("<"));
 //BA.debugLineNum = 212;BA.debugLine="Activity.AddView(btnRight, 10%x, 100%y - 75dip, 1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._btnright.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 213;BA.debugLine="btnLeft.Visible=False";
mostCurrent._btnleft.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 214;BA.debugLine="btnRight.Visible=False";
mostCurrent._btnright.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="ActiveBitmap.Initialize(File.DirAssets, \"indicato";
mostCurrent._activebitmap.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"indicator_active.png");
 //BA.debugLineNum = 218;BA.debugLine="InactiveBitmap.Initialize(File.DirAssets, \"indica";
mostCurrent._inactivebitmap.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"indicator_inactive.png");
 //BA.debugLineNum = 224;BA.debugLine="sd.Initialize";
mostCurrent._sd.Initialize();
 //BA.debugLineNum = 225;BA.debugLine="SlidingPanels.Initialize(sd, panels, True, 150)";
mostCurrent._slidingpanels._initialize /*String*/ (mostCurrent.activityBA,mostCurrent._sd,mostCurrent._panels,anywheresoftware.b4a.keywords.Common.True,(int) (150));
 //BA.debugLineNum = 226;BA.debugLine="sd.currentPanel = currentPanelBeforePaused - 1";
mostCurrent._sd.currentPanel /*int*/  = (int) (_currentpanelbeforepaused-1);
 //BA.debugLineNum = 228;BA.debugLine="Indicator = SlidingPanels.CreatePageIndicator(pan";
mostCurrent._indicator = mostCurrent._slidingpanels._createpageindicator /*anywheresoftware.b4a.objects.PanelWrapper*/ (mostCurrent.activityBA,mostCurrent._panels.length,mostCurrent._inactivebitmap,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (16)));
 //BA.debugLineNum = 229;BA.debugLine="Activity.AddView(Indicator, (100%x - 120dip) / 2,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._indicator.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)))/(double)2),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (16)));
 //BA.debugLineNum = 230;BA.debugLine="Indicator.BringToFront";
mostCurrent._indicator.BringToFront();
 //BA.debugLineNum = 231;BA.debugLine="ChangePanel(0)";
_changepanel((int) (0));
 //BA.debugLineNum = 234;BA.debugLine="Dim Webview1 As WebView";
_webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 235;BA.debugLine="Webview1.Initialize(\"Webview1\")";
_webview1.Initialize(mostCurrent.activityBA,"Webview1");
 //BA.debugLineNum = 237;BA.debugLine="Button1.Initialize(\"Button1\")";
mostCurrent._button1.Initialize(mostCurrent.activityBA,"Button1");
 //BA.debugLineNum = 239;BA.debugLine="browser.Initialize(\"browser\")";
mostCurrent._browser.Initialize(mostCurrent.activityBA,"browser");
 //BA.debugLineNum = 241;BA.debugLine="Activity.AddView(browser,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._browser.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 243;BA.debugLine="browser.Visible=False";
mostCurrent._browser.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 244;BA.debugLine="browser.AddView(Webview1,0,0,100%x,100%y)";
mostCurrent._browser.AddView((android.view.View)(_webview1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 245;BA.debugLine="browser.AddView(Button1,100%x-80dip,100%y-80dip,6";
mostCurrent._browser.AddView((android.view.View)(mostCurrent._button1.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 247;BA.debugLine="Dim cd As ColorDrawable";
_cd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 248;BA.debugLine="cd.Initialize(Colors.Rgb(252,211,77), 60dip)  'yo";
_cd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (252),(int) (211),(int) (77)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 250;BA.debugLine="Button1.Background =cd";
mostCurrent._button1.setBackground((android.graphics.drawable.Drawable)(_cd.getObject()));
 //BA.debugLineNum = 254;BA.debugLine="Button1.Elevation=10";
mostCurrent._button1.setElevation((float) (10));
 //BA.debugLineNum = 255;BA.debugLine="ImageView1.Initialize(\"ImageView1\")";
mostCurrent._imageview1.Initialize(mostCurrent.activityBA,"ImageView1");
 //BA.debugLineNum = 257;BA.debugLine="Button1.AddView(ImageView1,0,0,Button1.Width,Butt";
mostCurrent._button1.AddView((android.view.View)(mostCurrent._imageview1.getObject()),(int) (0),(int) (0),mostCurrent._button1.getWidth(),mostCurrent._button1.getHeight());
 //BA.debugLineNum = 259;BA.debugLine="nativeMe.InitializeContext";
_nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 260;BA.debugLine="Dim borderWidth1 As Int = 50";
_borderwidth1 = (int) (50);
 //BA.debugLineNum = 261;BA.debugLine="Dim borderColor1 As Int = Colors.White";
_bordercolor1 = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 262;BA.debugLine="ImageView1.Bitmap = Null";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 263;BA.debugLine="bm.Initialize(File.DirAssets,\"home.png\")";
mostCurrent._bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"home.png");
 //BA.debugLineNum = 264;BA.debugLine="bm1 = nativeMe.RunMethod(\"getRoundBitmap\",Array(b";
mostCurrent._bm1 = (anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(_nativeme.RunMethod("getRoundBitmap",new Object[]{(Object)(mostCurrent._bm.getObject()),(Object)(_bordercolor1),(Object)(_borderwidth1)})));
 //BA.debugLineNum = 265;BA.debugLine="ImageView1.Bitmap = bm1";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._bm1.getObject()));
 //BA.debugLineNum = 266;BA.debugLine="ImageView1.Gravity=Gravity.FILL";
mostCurrent._imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 269;BA.debugLine="CallSubDelayed2(LoadWeb,\"AddWebview\",Webview1)";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._loadweb.getObject()),"AddWebview",(Object)(_webview1));
 //BA.debugLineNum = 270;BA.debugLine="StartService(LoadWeb)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._loadweb.getObject()));
 //BA.debugLineNum = 271;BA.debugLine="CancelScheduledService(LoadWeb)";
anywheresoftware.b4a.keywords.Common.CancelScheduledService(processBA,(Object)(mostCurrent._loadweb.getObject()));
 //BA.debugLineNum = 273;BA.debugLine="If general.notif_status=True Then";
if (mostCurrent._general._notif_status /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 274;BA.debugLine="For i = 0 To panels.Length-1";
{
final int step143 = 1;
final int limit143 = (int) (mostCurrent._panels.length-1);
_i = (int) (0) ;
for (;_i <= limit143 ;_i = _i + step143 ) {
 //BA.debugLineNum = 275;BA.debugLine="panels(i).Visible=False";
mostCurrent._panels[_i].setVisible(anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 277;BA.debugLine="Indicator.Visible=False";
mostCurrent._indicator.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 278;BA.debugLine="browser.Visible=True";
mostCurrent._browser.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 279;BA.debugLine="browser.BringToFront";
mostCurrent._browser.BringToFront();
 //BA.debugLineNum = 280;BA.debugLine="general.notif_status=False";
mostCurrent._general._notif_status /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 281;BA.debugLine="FirebaseMessaging.n.Cancel(1)";
mostCurrent._firebasemessaging._n /*anywheresoftware.b4a.objects.NotificationWrapper*/ .Cancel((int) (1));
 };
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 403;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 404;BA.debugLine="If UserClosed = False Then";
if (_userclosed==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 405;BA.debugLine="currentPanelBeforePaused = sd.currentPanel";
_currentpanelbeforepaused = mostCurrent._sd.currentPanel /*int*/ ;
 }else {
 //BA.debugLineNum = 407;BA.debugLine="currentPanelBeforePaused = 0";
_currentpanelbeforepaused = (int) (0);
 };
 //BA.debugLineNum = 409;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 399;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 401;BA.debugLine="End Sub";
return "";
}
public static String  _animation1_animationend() throws Exception{
 //BA.debugLineNum = 333;BA.debugLine="Sub Animation1_AnimationEnd";
 //BA.debugLineNum = 335;BA.debugLine="SlidingPanels.AnimationEnd(sd)";
mostCurrent._slidingpanels._animationend /*String*/ (mostCurrent.activityBA,mostCurrent._sd);
 //BA.debugLineNum = 338;BA.debugLine="If sd.rotate Then";
if (mostCurrent._sd.rotate /*boolean*/ ) { 
 //BA.debugLineNum = 339;BA.debugLine="btnLeft.Enabled = True";
mostCurrent._btnleft.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 340;BA.debugLine="btnRight.Enabled = True";
mostCurrent._btnright.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 342;BA.debugLine="If sd.currentPanel = sd.panels.Length - 1 Then b";
if (mostCurrent._sd.currentPanel /*int*/ ==mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1) { 
mostCurrent._btnleft.setEnabled(anywheresoftware.b4a.keywords.Common.False);}
else {
mostCurrent._btnleft.setEnabled(anywheresoftware.b4a.keywords.Common.True);};
 //BA.debugLineNum = 343;BA.debugLine="If sd.currentPanel = 0 Then btnRight.Enabled = F";
if (mostCurrent._sd.currentPanel /*int*/ ==0) { 
mostCurrent._btnright.setEnabled(anywheresoftware.b4a.keywords.Common.False);}
else {
mostCurrent._btnright.setEnabled(anywheresoftware.b4a.keywords.Common.True);};
 };
 //BA.debugLineNum = 347;BA.debugLine="SlidingPanels.SetPageIndicator(Indicator, sd.curr";
mostCurrent._slidingpanels._setpageindicator /*String*/ (mostCurrent.activityBA,mostCurrent._indicator,mostCurrent._sd.currentPanel /*int*/ ,mostCurrent._activebitmap,mostCurrent._inactivebitmap);
 //BA.debugLineNum = 348;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 417;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 418;BA.debugLine="CallSub(LoadWeb,\"GotoHome\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._loadweb.getObject()),"GotoHome");
 //BA.debugLineNum = 419;BA.debugLine="End Sub";
return "";
}
public static String  _changepanel(int _page) throws Exception{
 //BA.debugLineNum = 323;BA.debugLine="Sub ChangePanel(page As Int)";
 //BA.debugLineNum = 325;BA.debugLine="btnLeft.Enabled = False";
mostCurrent._btnleft.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 326;BA.debugLine="btnRight.Enabled = False";
mostCurrent._btnright.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 329;BA.debugLine="sd.targetPanel = page";
mostCurrent._sd.targetPanel /*int*/  = _page;
 //BA.debugLineNum = 330;BA.debugLine="SlidingPanels.ChangePanel(sd)";
mostCurrent._slidingpanels._changepanel /*String*/ (mostCurrent.activityBA,mostCurrent._sd);
 //BA.debugLineNum = 331;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim sd As SlidingData 'The object that holds the";
mostCurrent._sd = new com.insaf.slidingpanels._slidingdata();
 //BA.debugLineNum = 16;BA.debugLine="Dim btnLeft, btnRight As Button";
mostCurrent._btnleft = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnright = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim Indicator As Panel";
mostCurrent._indicator = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim ActiveBitmap As Bitmap";
mostCurrent._activebitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim InactiveBitmap As Bitmap";
mostCurrent._inactivebitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim startX, startY, lastX As Float";
_startx = 0f;
_starty = 0f;
_lastx = 0f;
 //BA.debugLineNum = 23;BA.debugLine="Dim lastMove As Long";
_lastmove = 0L;
 //BA.debugLineNum = 24;BA.debugLine="Dim img_width1 As Int = 70dip";
_img_width1 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (70));
 //BA.debugLineNum = 25;BA.debugLine="Dim img_height1 As Int =100dip";
_img_height1 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 26;BA.debugLine="Dim img_width2 As Int = 150dip";
_img_width2 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (150));
 //BA.debugLineNum = 27;BA.debugLine="Dim img_height2 As Int =100dip";
_img_height2 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 28;BA.debugLine="Dim img_width3 As Int = 100dip";
_img_width3 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 29;BA.debugLine="Dim img_height3 As Int =100dip";
_img_height3 = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 31;BA.debugLine="Dim label_height=60dip";
mostCurrent._label_height = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 32;BA.debugLine="Dim current_panel As Int";
_current_panel = 0;
 //BA.debugLineNum = 33;BA.debugLine="Dim panels(3) As Panel";
mostCurrent._panels = new anywheresoftware.b4a.objects.PanelWrapper[(int) (3)];
{
int d0 = mostCurrent._panels.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._panels[i0] = new anywheresoftware.b4a.objects.PanelWrapper();
}
}
;
 //BA.debugLineNum = 35;BA.debugLine="Dim labels1(5) As Label";
mostCurrent._labels1 = new anywheresoftware.b4a.objects.LabelWrapper[(int) (5)];
{
int d0 = mostCurrent._labels1.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._labels1[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 36;BA.debugLine="Dim labels2(3) As Label";
mostCurrent._labels2 = new anywheresoftware.b4a.objects.LabelWrapper[(int) (3)];
{
int d0 = mostCurrent._labels2.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._labels2[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 37;BA.debugLine="Dim labels3(3) As Label";
mostCurrent._labels3 = new anywheresoftware.b4a.objects.LabelWrapper[(int) (3)];
{
int d0 = mostCurrent._labels3.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._labels3[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 38;BA.debugLine="Dim labels4(3) As Label";
mostCurrent._labels4 = new anywheresoftware.b4a.objects.LabelWrapper[(int) (3)];
{
int d0 = mostCurrent._labels4.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._labels4[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 39;BA.debugLine="Dim browser As Panel";
mostCurrent._browser = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim cs As CSBuilder";
mostCurrent._cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 41;BA.debugLine="Dim cs1 As CSBuilder";
mostCurrent._cs1 = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 43;BA.debugLine="Dim Button1 As Panel";
mostCurrent._button1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Dim bm, bm1, bm2 As Bitmap";
mostCurrent._bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._bm1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._bm2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _labels4_click() throws Exception{
int _i = 0;
 //BA.debugLineNum = 303;BA.debugLine="Sub labels4_Click";
 //BA.debugLineNum = 304;BA.debugLine="Log(currentPanelBeforePaused)";
anywheresoftware.b4a.keywords.Common.LogImpl("21179649",BA.NumberToString(_currentpanelbeforepaused),0);
 //BA.debugLineNum = 305;BA.debugLine="If current_panel=0 Then";
if (_current_panel==0) { 
 //BA.debugLineNum = 306;BA.debugLine="ChangePanel(1)";
_changepanel((int) (1));
 //BA.debugLineNum = 307;BA.debugLine="current_panel=1";
_current_panel = (int) (1);
 }else if(_current_panel==1) { 
 //BA.debugLineNum = 309;BA.debugLine="ChangePanel(2)";
_changepanel((int) (2));
 //BA.debugLineNum = 310;BA.debugLine="current_panel=2";
_current_panel = (int) (2);
 }else {
 //BA.debugLineNum = 312;BA.debugLine="Indicator.Visible=False";
mostCurrent._indicator.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 313;BA.debugLine="For i=0 To panels.Length-1";
{
final int step10 = 1;
final int limit10 = (int) (mostCurrent._panels.length-1);
_i = (int) (0) ;
for (;_i <= limit10 ;_i = _i + step10 ) {
 //BA.debugLineNum = 314;BA.debugLine="panels(i).Visible=False";
mostCurrent._panels[_i].setVisible(anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 316;BA.debugLine="browser.Visible=True";
mostCurrent._browser.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 317;BA.debugLine="SetStatusBarColor(Colors.Rgb(253,183,21))";
_setstatusbarcolor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (253),(int) (183),(int) (21)));
 };
 //BA.debugLineNum = 319;BA.debugLine="End Sub";
return "";
}
public static String  _left_click() throws Exception{
 //BA.debugLineNum = 295;BA.debugLine="Sub Left_Click";
 //BA.debugLineNum = 296;BA.debugLine="ChangePanel(SlidingPanels.PANEL_LEFT)";
_changepanel(mostCurrent._slidingpanels._panel_left /*int*/ );
 //BA.debugLineNum = 297;BA.debugLine="End Sub";
return "";
}
public static String  _menu_click() throws Exception{
String _menu = "";
 //BA.debugLineNum = 287;BA.debugLine="Sub Menu_Click";
 //BA.debugLineNum = 288;BA.debugLine="Dim menu As String";
_menu = "";
 //BA.debugLineNum = 289;BA.debugLine="menu = Sender";
_menu = BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA));
 //BA.debugLineNum = 290;BA.debugLine="btnLeft.Enabled = False";
mostCurrent._btnleft.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="btnRight.Enabled = False";
mostCurrent._btnright.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 292;BA.debugLine="ChangePanel(menu.SubString(\"Panel #\".Length))";
_changepanel((int)(Double.parseDouble(_menu.substring("Panel #".length()))));
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _panels_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 350;BA.debugLine="Sub Panels_Touch (Action As Int, X As Float, Y As";
 //BA.debugLineNum = 351;BA.debugLine="If Action = Activity.ACTION_MOVE And (DateTime.No";
if (_action==mostCurrent._activity.ACTION_MOVE && (anywheresoftware.b4a.keywords.Common.DateTime.getNow()-_lastmove<20 || anywheresoftware.b4a.keywords.Common.Abs(anywheresoftware.b4a.keywords.Common.Round(_x)-_lastx)<anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)))) { 
if (true) return "";};
 //BA.debugLineNum = 353;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP,mostCurrent._activity.ACTION_MOVE)) {
case 0: {
 //BA.debugLineNum = 355;BA.debugLine="startX = X";
_startx = _x;
 //BA.debugLineNum = 356;BA.debugLine="lastX = Round(X)";
_lastx = (float) (anywheresoftware.b4a.keywords.Common.Round(_x));
 //BA.debugLineNum = 357;BA.debugLine="startY = Y";
_starty = _y;
 break; }
case 1: {
 //BA.debugLineNum = 359;BA.debugLine="If Abs(y - startY) > 20%y Then ChangePanel(sd.c";
if (anywheresoftware.b4a.keywords.Common.Abs(_y-_starty)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA)) { 
_changepanel(mostCurrent._sd.currentPanel /*int*/ );};
 //BA.debugLineNum = 361;BA.debugLine="If ((sd.Panels(sd.currentPanel).Left + Round(X)";
if (((mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.Round(_x))-_startx>anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA) || _lastx-anywheresoftware.b4a.keywords.Common.Round(_x)>20) && mostCurrent._btnright.getEnabled()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 362;BA.debugLine="ChangePanel(SlidingPanels.PANEL_RIGHT)";
_changepanel(mostCurrent._slidingpanels._panel_right /*int*/ );
 }else if((_startx-(mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.Round(_x))>anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA) || anywheresoftware.b4a.keywords.Common.Round(_x)-_lastx>20) && mostCurrent._btnleft.getEnabled()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 364;BA.debugLine="ChangePanel(SlidingPanels.PANEL_LEFT)";
_changepanel(mostCurrent._slidingpanels._panel_left /*int*/ );
 }else {
 //BA.debugLineNum = 366;BA.debugLine="ChangePanel(sd.currentPanel)";
_changepanel(mostCurrent._sd.currentPanel /*int*/ );
 };
 //BA.debugLineNum = 368;BA.debugLine="Log(\"current panel:\" & sd.currentPanel)";
anywheresoftware.b4a.keywords.Common.LogImpl("21376274","current panel:"+BA.NumberToString(mostCurrent._sd.currentPanel /*int*/ ),0);
 //BA.debugLineNum = 369;BA.debugLine="current_panel= sd.currentPanel";
_current_panel = mostCurrent._sd.currentPanel /*int*/ ;
 //BA.debugLineNum = 370;BA.debugLine="If sd.currentPanel=0 Then";
if (mostCurrent._sd.currentPanel /*int*/ ==0) { 
 //BA.debugLineNum = 371;BA.debugLine="sd.rotate=False";
mostCurrent._sd.rotate /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 372;BA.debugLine="Indicator.Visible=True";
mostCurrent._indicator.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else if(mostCurrent._sd.currentPanel /*int*/ ==3) { 
 //BA.debugLineNum = 374;BA.debugLine="Indicator.Visible=False";
mostCurrent._indicator.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 376;BA.debugLine="Indicator.Visible=True";
mostCurrent._indicator.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 break; }
case 2: {
 //BA.debugLineNum = 379;BA.debugLine="lastMove = DateTime.Now";
_lastmove = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 380;BA.debugLine="If lastX <> Round(X) Then";
if (_lastx!=anywheresoftware.b4a.keywords.Common.Round(_x)) { 
 //BA.debugLineNum = 381;BA.debugLine="lastX = Round(X)";
_lastx = (float) (anywheresoftware.b4a.keywords.Common.Round(_x));
 //BA.debugLineNum = 382;BA.debugLine="sd.Panels(sd.currentPanel).Left = sd.Panels(sd";
mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].setLeft((int) (mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.Round(_x)-anywheresoftware.b4a.keywords.Common.Round(_startx)));
 //BA.debugLineNum = 383;BA.debugLine="If sd.rotate Then";
if (mostCurrent._sd.rotate /*boolean*/ ) { 
 //BA.debugLineNum = 384;BA.debugLine="sd.Panels((sd.currentPanel + sd.Panels.Length";
mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [(int) ((mostCurrent._sd.currentPanel /*int*/ +mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1)%mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length)].setLeft((int) (mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 385;BA.debugLine="sd.Panels((sd.currentPanel + 1) Mod sd.Panels";
mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [(int) ((mostCurrent._sd.currentPanel /*int*/ +1)%mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length)].setLeft((int) (mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 }else {
 //BA.debugLineNum = 388;BA.debugLine="If sd.currentPanel > 0 Then";
if (mostCurrent._sd.currentPanel /*int*/ >0) { 
 //BA.debugLineNum = 389;BA.debugLine="sd.Panels(sd.currentPanel - 1).Left = sd.Pan";
mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [(int) (mostCurrent._sd.currentPanel /*int*/ -1)].setLeft((int) (mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 };
 //BA.debugLineNum = 391;BA.debugLine="If sd.currentPanel < sd.Panels.Length - 1 The";
if (mostCurrent._sd.currentPanel /*int*/ <mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ .length-1) { 
 //BA.debugLineNum = 392;BA.debugLine="sd.Panels(sd.currentPanel + 1).Left = sd.Pan";
mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [(int) (mostCurrent._sd.currentPanel /*int*/ +1)].setLeft((int) (mostCurrent._sd.Panels /*anywheresoftware.b4a.objects.PanelWrapper[]*/ [mostCurrent._sd.currentPanel /*int*/ ].getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 };
 };
 };
 break; }
}
;
 //BA.debugLineNum = 397;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim tmrAnimation As Timer";
_tmranimation = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 9;BA.debugLine="Dim currentPanelBeforePaused As Int";
_currentpanelbeforepaused = 0;
 //BA.debugLineNum = 10;BA.debugLine="Dim nativeMe As JavaObject";
_nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _right_click() throws Exception{
 //BA.debugLineNum = 299;BA.debugLine="Sub Right_Click";
 //BA.debugLineNum = 300;BA.debugLine="ChangePanel(SlidingPanels.PANEL_RIGHT)";
_changepanel(mostCurrent._slidingpanels._panel_right /*int*/ );
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _setstatusbarcolor(int _clr) throws Exception{
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _window = null;
anywheresoftware.b4j.object.JavaObject _view = null;
 //BA.debugLineNum = 421;BA.debugLine="Sub SetStatusBarColor(clr As Int)";
 //BA.debugLineNum = 422;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 423;BA.debugLine="If p.SdkVersion > 20 Then";
if (_p.getSdkVersion()>20) { 
 //BA.debugLineNum = 426;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 427;BA.debugLine="jo.InitializeContext";
_jo.InitializeContext(processBA);
 //BA.debugLineNum = 428;BA.debugLine="Dim window As JavaObject = jo.RunMethodJO(\"getWi";
_window = new anywheresoftware.b4j.object.JavaObject();
_window = _jo.RunMethodJO("getWindow",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 429;BA.debugLine="window.RunMethod(\"addFlags\", Array (0x80000000))";
_window.RunMethod("addFlags",new Object[]{(Object)(0x80000000)});
 //BA.debugLineNum = 430;BA.debugLine="window.RunMethod(\"clearFlags\", Array (0x04000000";
_window.RunMethod("clearFlags",new Object[]{(Object)(0x04000000)});
 //BA.debugLineNum = 431;BA.debugLine="window.RunMethod(\"setStatusBarColor\", Array(clr)";
_window.RunMethod("setStatusBarColor",new Object[]{(Object)(_clr)});
 //BA.debugLineNum = 433;BA.debugLine="Dim view As JavaObject = window.RunMethodJO(\"get";
_view = new anywheresoftware.b4j.object.JavaObject();
_view = _window.RunMethodJO("getDecorView",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 435;BA.debugLine="view.RunMethod(\"setSystemUiVisibility\",Array(Bit";
_view.RunMethod("setSystemUiVisibility",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.Bit.Or((int) (0x00002000),(int)(BA.ObjectToNumber(_view.RunMethod("getSystemUiVisibility",(Object[])(anywheresoftware.b4a.keywords.Common.Null))))))});
 };
 //BA.debugLineNum = 438;BA.debugLine="End Sub";
return "";
}
public static String  _setvisiblebutton1(boolean _look) throws Exception{
 //BA.debugLineNum = 413;BA.debugLine="Sub SetVisibleButton1(look As Boolean)";
 //BA.debugLineNum = 414;BA.debugLine="Button1.Visible=look";
mostCurrent._button1.setVisible(_look);
 //BA.debugLineNum = 415;BA.debugLine="End Sub";
return "";
}




public static Bitmap getRoundBitmap(Bitmap scaleBitmapImage, int borderColor, int borderWidth) {
    int targetWidth = 1000;
    int targetHeight = 1000;
    int radius = Math.min((targetHeight - 5)/2, (targetWidth - 5)/2);  
   
    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
            Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(targetBitmap);
    Path path = new Path();
    path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
            Path.Direction.CCW);

    canvas.clipPath(path);
   
    Bitmap sourceBitmap = scaleBitmapImage;
   
    canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
            targetHeight), null);
           
           
    Paint p = new Paint();                                             
    p.setAntiAlias(true);                  
    //    canvas.drawBitmap(sourceBitmap, 4, 4, p);                                     
    p.setXfermode(null);                                               
    p.setStyle(Paint.Style.STROKE);                                          
    p.setColor(borderColor);                                           
    p.setStrokeWidth(borderWidth);                                               
    canvas.drawCircle((targetWidth / 2) , (targetHeight / 2) , radius, p);                             

    return targetBitmap;
}   
   
    public static Bitmap addSquareBorder(Bitmap bmp, int borderSize, int bordercolor) {
           
        Bitmap bmpWithBorder = Bitmap.createScaledBitmap(bmp, bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, false);           
           
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(bordercolor);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }   
   
   


}
