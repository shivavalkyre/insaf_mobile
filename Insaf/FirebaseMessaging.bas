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
	Private fm As FirebaseMessaging
	Dim n As Notification
End Sub

Sub Service_Create
	fm.Initialize("fm")
End Sub

Public Sub SubscribeToTopics
	fm.SubscribeToTopic("general") 'you can subscribe to more topics
End Sub

Sub Service_Start (StartingIntent As Intent)
	If StartingIntent.IsInitialized Then fm.HandleIntent(StartingIntent)
	Sleep(0)
	Service.StopAutomaticForeground 'remove if not using B4A v8+.
End Sub

Sub fm_MessageArrived (Message As RemoteMessage)
	Log("Message arrived")
	Log($"Message data: ${Message.GetData}"$)
	general.notif_status=True
	n.Initialize2(n.IMPORTANCE_HIGH)
	n.Icon ="icon"
	n.SetInfo(Message.GetData.Get("title"), Message.GetData.Get("body"), Modul)
	n.Notify(1)
End Sub

Sub Service_Destroy

End Sub