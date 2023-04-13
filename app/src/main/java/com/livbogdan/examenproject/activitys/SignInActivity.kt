package com.livbogdan.examenproject.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.models.User

class SignInActivity : BaseActivity() {

	private lateinit var auth: FirebaseAuth
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)

		auth = FirebaseAuth.getInstance()

		setupActionBar()
		signInButton()

	}


	private fun setupActionBar() {
		val toolbar = findViewById<Toolbar>(R.id.tb_sign_in)
		setSupportActionBar(toolbar)

		val actionBar = supportActionBar
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true)
			actionBar.setHomeAsUpIndicator(R.drawable.back_icon)
		}

		toolbar.setNavigationOnClickListener {
			onBackPressedDispatcher.onBackPressed()
		}

	}

	private fun signInButton(){
		val btnSignIn: Button = findViewById(R.id.btn_sign_in)
		btnSignIn.setOnClickListener {
			signInRegisteredUser()
		}
	}

	private fun signInRegisteredUser(){
		val etEmail: EditText = findViewById(R.id.et_email_sign_in)
		val etPassword: EditText = findViewById(R.id.et_pass_sign_in)

		val email:String = etEmail.text.toString().trim {it <= ' '}
		val password:String = etPassword.text.toString().trim {it <= ' '}

		if (validForm(email, password)){
			showProgressDialog(resources.getString(R.string.please_wait))
			auth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this) { task ->
					hideProgressDialog()
					if (task.isSuccessful) {
						// Sign in success, update UI with the signed-in user's information
						Log.d("Sign in", "createUserWithEmail:success")
						val user = auth.currentUser
						startActivity(Intent(this, MainActivity::class.java))
					} else {
						// If sign in fails, display a message to the user.
						Log.w("Sign In", "createUserWithEmail:failure", task.exception)
						Toast.makeText(baseContext, "Authentication failed.",
							Toast.LENGTH_SHORT).show()

					}
				}

		}
	}

	private fun validForm(email: String, password: String): Boolean {
		return when {
			TextUtils.isEmpty(email) -> {
				showErrorSnackBar("Please enter an E-mail")
				false
			}
			TextUtils.isEmpty(password) -> {
				showErrorSnackBar("Please enter password")
				false
			}
			else ->
			{
				true
			}
		}
	}

	fun signInSuccess(user: User) {
		hideProgressDialog()
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}