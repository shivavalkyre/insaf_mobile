B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=11
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim urls As String ="http://chat.disnavpriok.id:3001"
	Dim I As Intent
	Dim URI As String

	Private cc As ContentChooser
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	'Dim WebViewExtras1 As WebViewExtras
	Private WebView1 As WebView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("LayoutChat")
	
	Dim client As JavaObject
	client.InitializeNewInstance(Application.PackageName & ".chat$MyChromeClient", Null)
	Dim jo As JavaObject = WebView1
	jo.RunMethod("setWebChromeClient", Array(client))
	
	I = GetIntent
	'Check if the app was started from a URI click
	If I.Action = I.ACTION_VIEW Then
	'Just log the URI
	URI=I.GetData
	Log(URI)
	End If
	
	'Activity.LoadLayout("LayoutChat")
	If URI.Length>0 Then
		Dim jo As JavaObject = WebView1
		jo.RunMethod("setWebChromeClient", Array(client))
		WebView1.LoadUrl(URI)
		WebView1.ZoomEnabled=False
	Else
		Dim jo As JavaObject = WebView1
		jo.RunMethod("setWebChromeClient", Array(client))
		WebView1.LoadUrl(urls)
		WebView1.ZoomEnabled=False
	End If
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub GetIntent As Intent

	Dim sR As Reflector
	sR.Target=sR.GetActivity
	Return sR.RunMethod("getIntent")

End Sub


Sub ShowFile_Chooser (FilePathCallback As Object, FileChooserParams As Object)
	cc.Initialize("CC")
	cc.Show("*/*", "Choose File")
	Wait For CC_Result (Success As Boolean, Dir As String, FileName As String)
	Dim jo As JavaObject = Me
	If Success Then
		Log(FileName)
		File.Copy(Dir, FileName, Starter.Provider.SharedFolder, "TempFile")
		jo.RunMethod("SendResult", Array(Starter.Provider.GetFileUri("TempFile"), FilePathCallback))
		
	Else
		jo.RunMethod("SendResult", Array(Null, FilePathCallback))
	End If
End Sub

#if Java
import android.webkit.*;
import android.webkit.WebChromeClient.*;
import android.net.*;
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
#End If
