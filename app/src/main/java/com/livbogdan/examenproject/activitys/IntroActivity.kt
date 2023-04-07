package com.livbogdan.examenproject.activitys

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.livbogdan.examenproject.R

class IntroActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_intro)

		//Private function
		header()
		signIn()
		signUp()
	}

	//Header with custom font
	private fun header(){
		val headerTextView: TextView = findViewById(R.id.tv_app_name_intro)
		val typeFace: Typeface = Typeface.createFromAsset(assets, "Haelous.ttf")
		headerTextView.typeface = typeFace
	}
	private fun signUp() {

		val btnSignUp: Button = findViewById(R.id.btn_sign_up_intro)
		btnSignUp.setOnClickListener {
			startActivity(Intent(this, SignUpActivity::class.java))
		}
	}
	private fun signIn() {

		val btnSignIn: Button = findViewById(R.id.btn_sign_in_intro)
		btnSignIn.setOnClickListener {
			startActivity(Intent(this, SignInActivity::class.java))
		}
	}

}