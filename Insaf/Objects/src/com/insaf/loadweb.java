package com.insaf;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class loadweb extends  android.app.Service{
	public static class loadweb_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (loadweb) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, loadweb.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static loadweb mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return loadweb.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "com.insaf", "com.insaf.loadweb");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "com.insaf.loadweb", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (loadweb) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (loadweb) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (loadweb) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (false) {
            BA.LogInfo("** Service (loadweb) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (loadweb) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static uk.co.martinpearman.b4a.webkit.WebViewExtras _webviewextras1 = null;
public static String _urls = "";
public com.insaf.main _main = null;
public com.insaf.starter _starter = null;
public com.insaf.modul _modul = null;
public com.insaf.noconnection _noconnection = null;
public com.insaf.slidingpanels _slidingpanels = null;
public com.insaf.chat _chat = null;
public com.insaf.firebasemessaging _firebasemessaging = null;
public com.insaf.general _general = null;
public static String  _addwebview(anywheresoftware.b4a.objects.WebViewWrapper _webview1) throws Exception{
uk.co.martinpearman.b4a.webkit.DefaultWebViewClient _webviewclient1 = null;
 //BA.debugLineNum = 26;BA.debugLine="Public Sub AddWebview(Webview1 As WebView)";
 //BA.debugLineNum = 27;BA.debugLine="WebViewExtras1.Initialize(Webview1)";
_webviewextras1.Initialize((android.webkit.WebView)(_webview1.getObject()));
 //BA.debugLineNum = 28;BA.debugLine="Dim WebViewClient1 As DefaultWebViewClient";
_webviewclient1 = new uk.co.martinpearman.b4a.webkit.DefaultWebViewClient();
 //BA.debugLineNum = 29;BA.debugLine="WebViewClient1.Initialize(\"WebViewClient1\")";
_webviewclient1.Initialize(processBA,"WebViewClient1");
 //BA.debugLineNum = 31;BA.debugLine="WebViewExtras1.SetWebViewClient(WebViewClient1)";
_webviewextras1.SetWebViewClient((android.webkit.WebViewClient)(_webviewclient1.getObject()));
 //BA.debugLineNum = 32;BA.debugLine="WebViewExtras1.LoadUrl(urls)";
_webviewextras1.LoadUrl(_urls);
 //BA.debugLineNum = 33;BA.debugLine="WebViewExtras1.ZoomEnabled=False";
_webviewextras1.setZoomEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _gotohome() throws Exception{
 //BA.debugLineNum = 93;BA.debugLine="Public Sub GotoHome";
 //BA.debugLineNum = 94;BA.debugLine="WebViewExtras1.LoadUrl(urls)";
_webviewextras1.LoadUrl(_urls);
 //BA.debugLineNum = 95;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim WebViewExtras1 As WebViewExtras";
_webviewextras1 = new uk.co.martinpearman.b4a.webkit.WebViewExtras();
 //BA.debugLineNum = 11;BA.debugLine="Dim urls As String =\"https://insaf.disnavpriok.id";
_urls = "https://insaf.disnavpriok.id";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 19;BA.debugLine="Service.StopAutomaticForeground 'Call this when t";
mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _webviewclient1_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Sub WebViewClient1_PageFinished (Url As String)";
 //BA.debugLineNum = 83;BA.debugLine="Log(Url)";
anywheresoftware.b4a.keywords.Common.LogImpl("22883585",_url,0);
 //BA.debugLineNum = 84;BA.debugLine="If Url.Contains(\"dashboard\") Then";
if (_url.contains("dashboard")) { 
 //BA.debugLineNum = 86;BA.debugLine="CallSub2(Modul,\"SetVisibleButton1\",True)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._modul.getObject()),"SetVisibleButton1",(Object)(anywheresoftware.b4a.keywords.Common.True));
 }else {
 //BA.debugLineNum = 89;BA.debugLine="CallSub2(Modul,\"SetVisibleButton1\",False)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._modul.getObject()),"SetVisibleButton1",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return "";
}
public static String  _webviewclient1_receivedsslerror(uk.co.martinpearman.b4a.webkit.SslErrorHandler _sslerrorhandler1,uk.co.martinpearman.b4a.net.http.SslError _sslerror1) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub WebViewClient1_ReceivedSslError(SslErrorHandle";
 //BA.debugLineNum = 38;BA.debugLine="Log(\"WebViewClient1_ReceivedSslError: \"&SslError1";
anywheresoftware.b4a.keywords.Common.LogImpl("22818049","WebViewClient1_ReceivedSslError: "+_sslerror1.GetUrl(),0);
 //BA.debugLineNum = 41;BA.debugLine="Select SslError1.GetPrimaryError";
switch (BA.switchObjectToInt(_sslerror1.GetPrimaryError(),_sslerror1.SSL_DATE_INVALID,_sslerror1.SSL_EXPIRED,_sslerror1.SSL_IDMISMATCH,_sslerror1.SSL_INVALID,_sslerror1.SSL_MAX_ERROR,_sslerror1.SSL_NOTYETVALID,_sslerror1.SSL_UNTRUSTED)) {
case 0: {
 //BA.debugLineNum = 43;BA.debugLine="Log(\"SSL_DATE_INVALID\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818054","SSL_DATE_INVALID",0);
 break; }
case 1: {
 //BA.debugLineNum = 46;BA.debugLine="Log(\"SSL_EXPIRED\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818057","SSL_EXPIRED",0);
 break; }
case 2: {
 //BA.debugLineNum = 49;BA.debugLine="Log(\"SSL_IDMISMATCH\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818060","SSL_IDMISMATCH",0);
 break; }
case 3: {
 //BA.debugLineNum = 52;BA.debugLine="Log(\"SSL_INVALID\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818063","SSL_INVALID",0);
 break; }
case 4: {
 //BA.debugLineNum = 55;BA.debugLine="Log(\"SSL_MAX_ERROR\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818066","SSL_MAX_ERROR",0);
 break; }
case 5: {
 //BA.debugLineNum = 58;BA.debugLine="Log(\"SSL_NOTYETVALID\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818069","SSL_NOTYETVALID",0);
 break; }
case 6: {
 //BA.debugLineNum = 61;BA.debugLine="Log(\"SSL_UNTRUSTED\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818072","SSL_UNTRUSTED",0);
 break; }
}
;
 //BA.debugLineNum = 65;BA.debugLine="LogColor(\"SSL Error: \"& SslError1.GetUrl,Colors.R";
anywheresoftware.b4a.keywords.Common.LogImpl("22818076","SSL Error: "+_sslerror1.GetUrl(),anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 68;BA.debugLine="If SslError1.GetUrl = urls Then";
if ((_sslerror1.GetUrl()).equals(_urls)) { 
 //BA.debugLineNum = 69;BA.debugLine="Log(\"Proceed with the SSL Error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("22818080","Proceed with the SSL Error",0);
 //BA.debugLineNum = 72;BA.debugLine="SslErrorHandler1.Proceed";
_sslerrorhandler1.Proceed();
 }else {
 //BA.debugLineNum = 75;BA.debugLine="SslErrorHandler1.Proceed";
_sslerrorhandler1.Proceed();
 };
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
}
