Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=1.70
@EndOfDesignText@
'Code module
Sub Process_Globals
	Dim PANEL_LEFT As Int : PANEL_LEFT = -2
	Dim PANEL_RIGHT As Int : PANEL_RIGHT = -3

	Type SlidingData (currentPanel As Int, _
					  targetPanel As Int, _
					  rotate As Boolean, _
					  duration As Int, _
					  Panels() As Panel, _
					  slideAnimations() As Animation)
End Sub

Sub Initialize (sd As SlidingData, panels() As Panel, Rotate As Boolean, SlidingDuration As Int)
	sd.duration = SlidingDuration
	sd.rotate = Rotate
	sd.targetPanel = -1
	sd.currentPanel = -1
	sd.Panels = panels
	Modul.tmrAnimation.Initialize("tmrAnimation", 2)
	Dim a(2) As Animation
	sd.slideAnimations = a
	Dim a(2) As Animation
	sd.slideAnimations = a
	'Initialize the animation objects. We need two objects for each direction as both the current panel and the new panel are animated.
	For i = 0 To sd.Panels.Length - 1
		sd.Panels(i).Left = 100%x 'Move the panels right of the screen
	Next
End Sub

Sub ChangePanel(sd As SlidingData)
	Dim relativeDuration As Int
	Dim secondaryIndex As Int
	Dim slideleft As Boolean : slideleft = False
	
	'Just move panels to the left?
	If sd.targetPanel = PANEL_LEFT Then
		sd.targetPanel = sd.currentPanel  + 1
	End If
	
	'Just move panels to the right?
	If sd.targetPanel = PANEL_RIGHT Then
		sd.targetPanel = sd.currentPanel - 1
	End If
	
	'What direction we need to slide?
	If sd.currentPanel < sd.targetPanel Then slideleft = True
	
	'Do we need to rotate?
	If sd.targetPanel > sd.Panels.Length - 1 Then
		If sd.rotate Then
			sd.targetPanel = 0
		Else
			sd.targetPanel = sd.currentPanel
		End If
	End If
	
	If sd.targetPanel < 0 Then
		If sd.rotate Then
			sd.targetPanel = sd.Panels.Length - 1
		Else
			sd.targetPanel = sd.currentPanel
		End If
	End If
	
	If sd.currentPanel = sd.targetPanel And sd.currentPanel <> -1 Then
		relativeDuration = sd.duration / 100%x * Abs(sd.Panels(sd.currentPanel).Left)
		
		'Current page and target page are the same. We just have to snap the panels back to their old position
		If sd.Panels(sd.currentPanel).Left >= 0 Then
			secondaryIndex = sd.currentPanel - 1
			If secondaryIndex < 0 And sd.rotate Then secondaryIndex = sd.Panels.Length - 1
		Else
			secondaryIndex = sd.currentPanel + 1
			If secondaryIndex > sd.Panels.Length - 1 Then
				If sd.rotate Then secondaryIndex = 0 Else secondaryIndex = -1
			End If
		End If

		Dim toX As Int

		toX = -sd.Panels(sd.currentPanel).Left
		
		'Animate current panel to original position
		sd.slideAnimations(1).InitializeTranslate("animation1", 0, 0, toX , 0)
		sd.slideAnimations(1).Duration = relativeDuration
		sd.slideAnimations(1).Start(sd.panels(sd.currentPanel)) 'Animate current panel and move it to old position
		
		'Animate secondary panel outside of screen
		If secondaryIndex <> -1 Then
			sd.slideAnimations(0).InitializeTranslate( "animation0", 0, 0, toX, 0)
			sd.slideAnimations(0).Duration = relativeDuration
			sd.slideAnimations(0).Start(sd.panels(secondaryIndex)) 'Animate secondary panel
		End If
	Else
		If slideleft Or sd.currentPanel = -1 Then
			relativeDuration = sd.duration / 100%x * Abs(sd.Panels(sd.targetPanel).Left)
			If sd.currentPanel <> -1 Then
				sd.Panels(sd.targetPanel).Left = sd.Panels(sd.currentPanel).Left + 100%x
				sd.slideAnimations(0).InitializeTranslate("animation0", 0, 0, -100%x - sd.Panels(sd.currentPanel).Left, 0)
				sd.slideAnimations(0).Duration = relativeDuration
				sd.slideAnimations(0).Start(sd.panels(sd.currentPanel)) 'Animate current panel and move it out
			End If
			sd.slideAnimations(1).InitializeTranslate( "animation1", 0, 0, - sd.Panels(sd.targetPanel).Left, 0)
			sd.slideAnimations(1).Duration = relativeDuration
			sd.slideAnimations(1).Start(sd.panels(sd.targetPanel)) 'Animate new panel
			sd.currentPanel = sd.targetPanel
		Else
			sd.Panels(sd.targetPanel).Left = sd.Panels(sd.currentPanel).Left - 100%x
			relativeDuration = sd.duration / 100%x * Abs(100%x - sd.Panels(sd.currentPanel).Left)
			leftPanel = (sd.currentPanel + sd.Panels.Length - 1) Mod sd.Panels.Length
			sd.slideAnimations(0).InitializeTranslate("animation0", 0, 0, 100%x - sd.Panels(sd.currentPanel).Left, 0)
			sd.slideAnimations(1).InitializeTranslate("animation1", 0, 0, 100%x - sd.Panels(sd.currentPanel).Left, 0)
			sd.slideAnimations(0).Duration = relativeDuration
			sd.slideAnimations(1).Duration = relativeDuration
			sd.slideAnimations(0).Start(sd.panels(sd.currentPanel))
			sd.slideAnimations(1).Start(sd.panels(sd.targetPanel))
			sd.currentPanel = sd.targetPanel
		End If
	End If
End Sub

Sub AnimationEnd (sd As SlidingData)
	sd.panels(sd.currentPanel).Left = 0 'Set the position of the new panel
	For i = 0 To sd.panels.Length - 1
		If i <> sd.currentPanel Then
			sd.Panels(i).Left = 100%x 'Move all other panels right of the screen.
		End If
	Next
End Sub

Sub CreatePageIndicator(numberOfPages As Int, inactiveBitmap As Bitmap, Width As Int, Height As Int) As Panel
	Dim imageSize, maxWidth, Gap, Top As Int
	
	maxWidth = Width / numberOfPages
	If maxWidth < Height Then
		imageSize = maxWidth
		Top = (Height - maxWidth) / 2
		Gap = 0
	Else
		imageSize = Height
		Top = 0
		Gap = (Width - numberOfPages * imageSize) / (numberOfPages - 1)
	End If
	
	Dim basePanel As Panel
	
	basePanel.Initialize("")
	For i = 0 To numberOfPages - 1
		Dim iv As ImageView
		
		iv.Initialize("")
		iv.Gravity=Gravity.FILL
		iv.Bitmap = inactiveBitmap
		basePanel.AddView(iv, i * (imageSize + Gap), Top, imageSize, imageSize)
	Next
	
	Return basePanel
End Sub

Sub SetPageIndicator(pagePanel As Panel, page As Int, activeBitmap As Bitmap, inactiveBitmap As Bitmap)
	Dim v As View
	Dim iv As ImageView

	For i = 0 To pagePanel.NumberOfViews - 1
		v = pagePanel.GetView(i)
		If v Is ImageView Then
			iv = v
			If i = page Then
				iv.Bitmap = activeBitmap
			Else
				iv.Bitmap = inactiveBitmap
			End If
		End If
	Next
End Sub