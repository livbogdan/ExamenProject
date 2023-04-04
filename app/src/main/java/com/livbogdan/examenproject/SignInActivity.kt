package com.livbogdan.examenproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar

class SignInActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)

		setupActionBar()
		signInButton()
	}

	@Suppress("DEPRECATION")
	private fun setupActionBar() {
		val toolbar = findViewById<Toolbar>(R.id.tb_sign_in)
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

	private fun signInButton(){
		val btnSignIn: Button = findViewById(R.id.btn_sign_in)
		btnSignIn.setOnClickListener {
			startActivity(Intent(this, SignInActivity::class.java))
		}
	}
}