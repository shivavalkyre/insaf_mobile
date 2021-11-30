B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=11
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

'Activity module
Sub Process_Globals
	Dim tmrAnimation As Timer
	Dim currentPanelBeforePaused As Int
	Dim nativeMe As JavaObject
End Sub

Sub Globals
	Dim sd As SlidingData 'The object that holds the data for SlidingPanels

	Dim btnLeft, btnRight As Button
	Dim Indicator As Panel
	'Dim  As ImageView
	Dim ActiveBitmap As Bitmap
	Dim InactiveBitmap As Bitmap

	Dim startX, startY, lastX As Float
	Dim lastMove As Long
	Dim img_width1 As Int = 70dip
	Dim img_height1 As Int =100dip
	Dim img_width2 As Int = 150dip
	Dim img_height2 As Int =100dip
	Dim img_width3 As Int = 100dip
	Dim img_height3 As Int =100dip
	
	Dim label_height=60dip
	Dim current_panel As Int
	Dim panels(3) As Panel
	
	Dim labels1(5) As Label
	Dim labels2(3) As Label
	Dim labels3(3) As Label
	Dim labels4(3) As Label
	Dim browser As Panel
	Dim cs As CSBuilder
	Dim cs1 As CSBuilder
	'Dim Button1 As Button
	Dim Button1 As Panel
	Dim ImageView1 As ImageView
	Dim bm, bm1, bm2 As Bitmap
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Create the panels.
	'In this example we are just creating 6 "dummy" panels with different colors.
	'It is possible to load a layout for each panel but this is slow and initializing the GUI
	'will take some time. It is really better to manually create the complete by code here.
	Activity.Title=""
	
		
	
	
	
	For i = 0 To panels.Length-1
		panels(i).Initialize("panels")
		'If i<=panels.Length - 1 Then
			panels(i).Color = Colors.RGB(253,183,21)
		'End If
		'Dim lbl As Label
		'lbl.Initialize("")
		'lbl.Text = "I'm Panel: " & i
		'lbl.TextSize = 20
		'lbl.TextColor = Colors.White
		'panels(i).AddView(lbl, 20%x, 40%y, 60%x, 30dip)
		
		If i<=panels.Length Then
		
		
			If i =0 Then
				Dim img_onboard As ImageView
				img_onboard.Initialize("img_onboard")
				img_onboard.Bitmap=LoadBitmap(File.DirAssets,"Group 2992.png")
				img_onboard.Gravity= Gravity.FILL
				panels(i).AddView(img_onboard,50%x-img_width1/2,40%y-img_height1/2,img_width1,img_height1)
				
			labels1(0).Initialize("labels1")
			labels1(1).Initialize("labels1")
			cs.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("Selamat datang di ").Pop.Bold.Append("INSAF").PopAll
			labels1(0).Text = cs
			labels1(0).TextColor=Colors.Black
			
			cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Pop.Bold.Append("INFORMATION AND SAFETY").PopAll
			labels1(1).Text = cs1
			labels1(1).TextColor=Colors.Black
			
			
			labels4(0).Initialize("labels4")
			
			
			labels4(0).Text = "Lanjut"
			labels4(0).Typeface= Typeface.LoadFromAssets("Poppins-Regular.ttf")
			labels4(0).textColor=Colors.Black
			labels4(0).Typeface= Typeface.DEFAULT_BOLD
			labels4(0).Gravity= Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
			
			panels(i).AddView(labels1(0),0dip,50%y,100%x,60dip)
				panels(i).AddView(labels1(1),0dip,labels1(0).Top+labels1(0).Height-35dip,100%x,60dip)
			panels(i).AddView(labels4(0),100%x-120dip,100%y-label_height,120dip,60dip)
			
			Else If i=1 Then
				
				Dim img_onboard As ImageView
				img_onboard.Initialize("img_onboard")
				img_onboard.Bitmap=LoadBitmap(File.DirAssets,"Group 2990.png")
				img_onboard.Gravity= Gravity.FILL
				panels(i).AddView(img_onboard,50%x-img_width2/2,40%y-img_height2/2,img_width2,img_height2)
				
				labels1(0).Initialize("labels1")
				labels1(1).Initialize("labels1")
				labels1(2).Initialize("labels1")
				labels1(3).Initialize("labels1")
				labels1(4).Initialize("labels1")
				labels4(1).Initialize("labels4")
				
				cs.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("Aplikasi ").Pop.Bold.Append("INSAF ").Pop.Append("bertujuan untuk menyimpan").PopAll
				labels1(0).Text=cs
				labels1(0).textColor=Colors.Black
			
				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("").Pop.Bold.Append("Berita Telekomunikasi Pelayaran").Pop.Append(" yang menjadi").PopAll
				labels1(1).Text =cs1
				labels1(1).textColor=Colors.Black
				
				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("salah satu fungsi dan tugas pokok").PopAll
				labels1(2).Text =cs1
				labels1(2).textColor=Colors.Black
			
				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("").Pop.Bold.Append("Distrik Navigasi Kelas I Tanjung Priok").PopAll
				labels1(3).Text =cs1
				labels1(3).textColor=Colors.Black
				
				
				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("dalam bentuk digital").PopAll
				labels1(4).Text =cs1
				labels1(4).textColor=Colors.Black
			
				labels4(1).Text = "Lanjut"
				labels4(1).Typeface= Typeface.LoadFromAssets("Poppins-Regular.ttf")
				labels4(1).textColor=Colors.Black
				labels4(1).Typeface= Typeface.DEFAULT_BOLD
				labels4(1).Gravity= Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
				
				
			
				panels(i).AddView(labels1(0),0dip,50%y,100%x,60dip)
				panels(i).AddView(labels1(1),0dip,54%y,100%x,60dip)
				panels(i).AddView(labels1(2),0dip,58%y,100%x,60dip)
				panels(i).AddView(labels1(3),0dip,62%y,100%x,60dip)
				panels(i).AddView(labels1(4),0dip,66%y,100%x,60dip)
				
				panels(i).AddView(labels4(1),100%x-120dip,100%y-label_height,120dip,60dip)
			Else
				
				Dim img_onboard As ImageView
				img_onboard.Initialize("img_onboard")
				img_onboard.Bitmap=LoadBitmap(File.DirAssets,"Group 2489.png")
				img_onboard.Gravity= Gravity.FILL
				panels(i).AddView(img_onboard,50%x-img_width3/2,40%y-img_height3/2,img_width3,img_height3)
				
				labels1(0).Initialize("labels1")
				labels1(1).Initialize("labels1")
				labels1(2).Initialize("labels1")
				'labels2(1).Initialize("labels2")
				'labels3(1).Initialize("labels3")
				labels4(1).Initialize("labels4")
			
				cs.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("Aplikasi ").Pop.Bold.Append("INSAF ").Pop.Append("merupakan aplikasi internal").PopAll
				labels1(0).Text=cs
				labels1(0).textColor=Colors.Black

				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("bidang ").Pop.Bold.Append("Telekomunikasi Pelayaran").popall
				labels1(1).Text =cs1
				labels1(1).textColor=Colors.Black
				
				cs1.Initialize.Typeface(Typeface.LoadFromAssets("Poppins-Regular.ttf")).Alignment("ALIGN_CENTER").Append("").Pop.Bold.Append("Distrik Navigasi Kelas I Tanjung Priok").popall
				labels1(2).Text =cs1
				labels1(2).textColor=Colors.Black
			
			
				labels4(1).Text = "Mulai"
				labels4(1).Typeface= Typeface.LoadFromAssets("Poppins-Regular.ttf")
				labels4(1).textColor=Colors.Black
				labels4(1).Typeface= Typeface.DEFAULT_BOLD
				labels4(1).Gravity= Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
				
				
			
				panels(i).AddView(labels1(0),0dip,50%y,100%x,60dip)
				panels(i).AddView(labels1(1),labels1(1).Left,54%y,100%x,60dip)
				panels(i).AddView(labels1(2),0dip,58%y,100%x,60dip)
				'panels(i).AddView(labels2(1),labels1(1).Left+labels1(1).Width-24dip,labels1(1).Top-2dip,20%x,60dip)
				'panels(i).AddView(labels3(1),0,55%y,100%x,60dip)
				panels(i).AddView(labels4(1),100%x-120dip,100%y-label_height,120dip,60dip)
		End If
		End If
		
		
		Activity.AddView(panels(i), 100%x, 0, 100%x, 100%y) 'add the panel to the layout
		Activity.AddMenuItem("Panel #" & i, "Menu")
	Next

	'add the Left and Right button
	btnLeft.Initialize("Left")
	btnLeft.Text = ">"
	Activity.AddView(btnLeft, 60%x, 100%y - 75dip, 100dip, 50dip)
	btnRight.Initialize("Right")
	btnRight.Text = "<"
	Activity.AddView(btnRight, 10%x, 100%y - 75dip, 100dip, 50dip)
	btnLeft.Visible=False
	btnRight.Visible=False

	'Load Bitmaps for indicator
	ActiveBitmap.Initialize(File.DirAssets, "indicator_active.png")
	InactiveBitmap.Initialize(File.DirAssets, "indicator_inactive.png")
	
	'*****************************
	'Initialize the SlidingData object and set the array of panels.
	'Then we call SlidingPanels.Initialize to prepare the animation objects.
	'The last call to ChangePanel brings the first panel.
	sd.Initialize
	SlidingPanels.Initialize(sd, panels, True, 150)
	sd.currentPanel = currentPanelBeforePaused - 1
	
	Indicator = SlidingPanels.CreatePageIndicator(panels.Length, InactiveBitmap, 120dip, 16dip)
	Activity.AddView(Indicator, (100%x - 120dip) / 2, 0, 120dip, 16dip)
	Indicator.BringToFront
	ChangePanel(0)
	
	
	Dim Webview1 As WebView
	Webview1.Initialize("Webview1")
	
	Button1.Initialize("Button1")

	browser.Initialize("browser")
	
	Activity.AddView(browser,0,0,100%x,100%y)
	
	browser.Visible=False
	browser.AddView(Webview1,0,0,100%x,100%y)
	browser.AddView(Button1,100%x-80dip,100%y-80dip,60dip,60dip)
	
	Dim cd As ColorDrawable
	cd.Initialize(Colors.Rgb(252,211,77), 60dip)  'you can also use Initialize2 with more parameters
	
	Button1.Background =cd
	'Button1.Typeface=Typeface.FONTAWESOME
	'Button1.Text= Chr(0xF015)
	'Button1.TextSize=18
	Button1.Elevation=10
	ImageView1.Initialize("ImageView1")
	
	Button1.AddView(ImageView1,0,0,Button1.Width,Button1.Height)
	
	nativeMe.InitializeContext
	Dim borderWidth1 As Int = 50
	Dim borderColor1 As Int = Colors.White
	ImageView1.Bitmap = Null
	bm.Initialize(File.DirAssets,"home.png")
	bm1 = nativeMe.RunMethod("getRoundBitmap",Array(bm,borderColor1, borderWidth1))
	ImageView1.Bitmap = bm1
	ImageView1.Gravity=Gravity.FILL
	
	
	CallSubDelayed2(LoadWeb,"AddWebview",Webview1)
	StartService(LoadWeb)
	CancelScheduledService(LoadWeb)
	
	If general.notif_status=True Then
		For i = 0 To panels.Length-1
			panels(i).Visible=False
		Next
		Indicator.Visible=False
		browser.Visible=True
		browser.BringToFront
		general.notif_status=False
		FirebaseMessaging.n.Cancel(1)
	End If
		
	
End Sub

Sub Menu_Click
	Dim menu As String
	menu = Sender
	btnLeft.Enabled = False
	btnRight.Enabled = False
	ChangePanel(menu.SubString("Panel #".Length))
End Sub

Sub Left_Click
	ChangePanel(SlidingPanels.PANEL_LEFT)
End Sub

Sub Right_Click
	ChangePanel(SlidingPanels.PANEL_RIGHT)
End Sub

Sub labels4_Click
	Log(currentPanelBeforePaused)
If current_panel=0 Then
	ChangePanel(1)
	current_panel=1
Else If current_panel=1 Then
		ChangePanel(2)
		current_panel=2
Else
	Indicator.Visible=False
	For i=0 To panels.Length-1
		panels(i).Visible=False
	Next
	browser.Visible=True
		SetStatusBarColor(Colors.Rgb(253,183,21))
End If
End Sub

'**** These two subs are required for handling the sliding ***

Sub ChangePanel(page As Int)
	'disable the buttons during the animation
	btnLeft.Enabled = False
	btnRight.Enabled = False
	
	'Call SlidingPanels.ChangePanel to actually change the panels.
	sd.targetPanel = page
	SlidingPanels.ChangePanel(sd)
End Sub

Sub Animation1_AnimationEnd
	'This event is raised when the animation finishes. You should call SlidingPanels.AnimationEnd from this sub.
	SlidingPanels.AnimationEnd(sd)

	'Enable the Left and Right buttons (unless there are no more panels).
	If sd.rotate Then
		btnLeft.Enabled = True
		btnRight.Enabled = True
	Else
		If sd.currentPanel = sd.panels.Length - 1 Then btnLeft.Enabled = False Else btnLeft.Enabled = True
		If sd.currentPanel = 0 Then btnRight.Enabled = False Else btnRight.Enabled = True
	End If

	'set the indicator to the correct page.
	SlidingPanels.SetPageIndicator(Indicator, sd.currentPanel, ActiveBitmap, InactiveBitmap)
End Sub
	
Sub Panels_Touch (Action As Int, X As Float, Y As Float)
	If Action = Activity.ACTION_MOVE And (DateTime.Now - lastMove < 20 Or Abs(Round(X) - lastX) < 1dip) Then Return
	
	Select Action
		Case Activity.ACTION_DOWN
			startX = X
			lastX = Round(X)
			startY = Y
		Case Activity.ACTION_UP
			If Abs(y - startY) > 20%y Then ChangePanel(sd.currentPanel)

			If ((sd.Panels(sd.currentPanel).Left + Round(X)) - startX > 30%x Or lastX - Round(X) > 20) And btnRight.Enabled = True Then
				ChangePanel(SlidingPanels.PANEL_RIGHT)
			Else If (startX - (sd.Panels(sd.currentPanel).Left + Round(X)) > 30%x Or Round(X) - lastX > 20) And btnLeft.Enabled = True Then
				ChangePanel(SlidingPanels.PANEL_LEFT)
			Else
				ChangePanel(sd.currentPanel)
			End If
			Log("current panel:" & sd.currentPanel)
			current_panel= sd.currentPanel
			If sd.currentPanel=0 Then
				sd.rotate=False
				Indicator.Visible=True
			Else If sd.currentPanel=3 Then
				Indicator.Visible=False
			Else
				Indicator.Visible=True
			End If
		Case Activity.ACTION_MOVE
			lastMove = DateTime.Now
			If lastX <> Round(X) Then
				lastX = Round(X)
				sd.Panels(sd.currentPanel).Left = sd.Panels(sd.currentPanel).Left + Round(X) - Round(startX)
				If sd.rotate Then
					sd.Panels((sd.currentPanel + sd.Panels.Length - 1) Mod sd.Panels.Length).Left = sd.Panels(sd.currentPanel).Left - 100%x
					sd.Panels((sd.currentPanel + 1) Mod sd.Panels.Length).Left = sd.Panels(sd.currentPanel).Left + 100%x
					'Log("current panel:" & sd.currentPanel)
				Else
					If sd.currentPanel > 0 Then
						sd.Panels(sd.currentPanel - 1).Left = sd.Panels(sd.currentPanel).Left - 100%x
					End If
					If sd.currentPanel < sd.Panels.Length - 1 Then
						sd.Panels(sd.currentPanel + 1).Left = sd.Panels(sd.currentPanel).Left + 100%x
					End If
				End If
			End If
	End Select
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed = False Then
		currentPanelBeforePaused = sd.currentPanel
	Else
		currentPanelBeforePaused = 0
	End If
End Sub



Sub SetVisibleButton1(look As Boolean)
	Button1.Visible=look
End Sub

Sub Button1_Click
	CallSub(LoadWeb,"GotoHome")
End Sub

Sub SetStatusBarColor(clr As Int)
	Dim p As Phone
	If p.SdkVersion > 20 Then

		'Background color
		Dim jo As JavaObject
		jo.InitializeContext
		Dim window As JavaObject = jo.RunMethodJO("getWindow", Null)
		window.RunMethod("addFlags", Array (0x80000000))
		window.RunMethod("clearFlags", Array (0x04000000))
		window.RunMethod("setStatusBarColor", Array(clr))
 
		Dim view As JavaObject = window.RunMethodJO("getDecorView",Null)

		view.RunMethod("setSystemUiVisibility",Array(Bit.Or(0x00002000,view.RunMethod("getSystemUiVisibility",Null)))) 'Light style with black icons and text
		'view.RunMethod("setSystemUiVisibility",Array(0)) 'Dark style with White icons and text
	End If
End Sub

#If Java

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
   
   


#End If
