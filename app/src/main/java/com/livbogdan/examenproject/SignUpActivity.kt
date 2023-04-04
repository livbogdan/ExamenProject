package com.livbogdan.examenproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)
		setupActionBar()
		signUpButton()
	}

	private fun setupActionBar() {
		val toolbar = findViewById<Toolbar>(R.id.tb_sign_up)
		setSupportActionBar(toolbar)

		val actionBar = supportActionBar
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true)
			actionBar.setHomeAsUpIndicator(R.drawable.back_icon)
		}

		toolbar.setNavigationOnClickListener {
			onBackPressed()
		}
	}

	private fun signUpButton(){
		val btnSignUp: Button = findViewById(R.id.btn_sign_in)
		btnSignUp.setOnClickListener {
			startActivity(Intent(this, SignUpActivity::class.java))
		}
	}

}