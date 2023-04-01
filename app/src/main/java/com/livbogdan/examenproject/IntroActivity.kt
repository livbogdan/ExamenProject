package com.livbogdan.examenproject

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_intro)

		val headerTextView: TextView = findViewById(R.id.tv_app_name_intro)
		val typeFace: Typeface = Typeface.createFromAsset(assets, "Haelous.ttf")
		headerTextView.typeface = typeFace

	}
}