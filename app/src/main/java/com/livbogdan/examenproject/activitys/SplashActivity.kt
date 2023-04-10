package com.livbogdan.examenproject.activitys

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		//custom font
		val tvAppName: TextView = findViewById(R.id.tv_app_name)
		val typeFace: Typeface = Typeface.createFromAsset(assets, "Haelous.ttf")
		tvAppName.typeface = typeFace

		//Move to another activity by 2,5 second
		val handler = Handler(Looper.getMainLooper())
		var currentUserID = FirestoreClass().getCurrentUserId()

		val runnable = Runnable{

			//#region Auto Login If Account exist
			if (currentUserID.isNotEmpty()){
				startActivity(Intent(this, MainActivity::class.java))
			}else{
				startActivity(Intent(this, IntroActivity::class.java))
			}
			//#endregion

			/* Always starting IntroActivity
			startActivity(Intent(this, IntroActivity::class.java))
			 */
			finish()
		}
		handler.postDelayed(runnable,2500)


	}
}