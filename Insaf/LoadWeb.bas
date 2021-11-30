B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=11
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim WebViewExtras1 As WebViewExtras
	'Dim WebViewSetting1 As WebViewSettings
	Dim urls As String ="https://insaf.disnavpriok.id"
End Sub

Sub Service_Create

End Sub

Sub Service_Start (StartingIntent As Intent)
	Service.StopAutomaticForeground 'Call this when the background task completes (if there is one)
End Sub

Sub Service_Destroy

End Sub

Public Sub AddWebview(Webview1 As WebView)
	WebViewExtras1.Initialize(Webview1)
	Dim WebViewClient1 As DefaultWebViewClient
	WebViewClient1.Initialize("WebViewClient1")
    
	WebViewExtras1.SetWebViewClient(WebViewClient1)
	WebViewExtras1.LoadUrl(urls)
	WebViewExtras1.ZoomEnabled=False
End Sub


Sub WebViewClient1_ReceivedSslError(SslErrorHandler1 As SslErrorHandler, SslError1 As SslError)
	Log("WebViewClient1_ReceivedSslError: "&SslError1.GetUrl)
    
	'    you might want to take action depending on the type of the SSL error:
	Select SslError1.GetPrimaryError
		Case SslError1.SSL_DATE_INVALID
			Log("SSL_DATE_INVALID")
            
		Case SslError1.SSL_EXPIRED
			Log("SSL_EXPIRED")
            
		Case SslError1.SSL_IDMISMATCH
			Log("SSL_IDMISMATCH")
            
		Case SslError1.SSL_INVALID
			Log("SSL_INVALID")
            
		Case SslError1.SSL_MAX_ERROR
			Log("SSL_MAX_ERROR")
            
		Case SslError1.SSL_NOTYETVALID
			Log("SSL_NOTYETVALID")
            
		Case SslError1.SSL_UNTRUSTED
			Log("SSL_UNTRUSTED")
            
	End Select

	LogColor("SSL Error: "& SslError1.GetUrl,Colors.Red)

	'    you might want to compare the url that raised the error with a known trusted URL and Proceed or Cancel with the page loading:
	If SslError1.GetUrl = urls Then
		Log("Proceed with the SSL Error")
		'btnCancel.SetVisibleAnimated(600,False)
		'btnApprove.SetVisibleAnimated(900,False)
		SslErrorHandler1.Proceed
	Else
		'SslErrorHandler1.Cancel
		SslErrorHandler1.Proceed
	End If
    
End Sub



Sub WebViewClient1_PageFinished (Url As String)
	Log(Url)
	If Url.Contains("dashboard") Then
		'Button1.Visible=True
		CallSub2(Modul,"SetVisibleButton1",True)
	Else
		'Button1.Visible=False
		CallSub2(Modul,"SetVisibleButton1",False)
	End If
End Sub

Public Sub GotoHome
	WebViewExtras1.LoadUrl(urls)
End Sub