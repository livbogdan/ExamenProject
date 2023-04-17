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


	//sets up the action bar and back button to return to the previous activity.
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

	// The sign-in button to call the signInRegisteredUser() method when clicked.
	private fun signInButton(){
		val btnSignIn: Button = findViewById(R.id.btn_sign_in)
		btnSignIn.setOnClickListener {
			signInRegisteredUser()
		}
	}

	// Is used to handle the result of the authentication.
	// If the authentication is successful, it starts the MainActivity.
	// If it fails, it shows a toast message to the user.
	private fun signInRegisteredUser(){
		val etEmail: EditText = findViewById(R.id.et_email_sign_in)
		val etPassword: EditText = findViewById(R.id.et_pass_sign_in)

		val email:String = etEmail.text.toString().trim {it <= ' '}
		val password:String = etPassword.text.toString().trim {it <= ' '}

		if (validForm(email, password)){
			showProgressDialog(resources.getString(R.string.please_wait))
			//The FirebaseAuth object to authenticate the user.
			auth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this) { task ->
					hideProgressDialog()
					if (task.isSuccessful) {
						Log.d("Sign in", "createUserWithEmail:success")
						val user = auth.currentUser
						startActivity(Intent(this, MainActivity::class.java))
					} else {
						Log.w("Sign In", "createUserWithEmail:failure", task.exception)
						Toast.makeText(baseContext, "Authentication failed.",
							Toast.LENGTH_SHORT).show()

					}
				}

		}
	}

	// Validates the user's input and returns true if the input is valid, and false otherwise.
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

	// Method is not used in the code, but it's left there as an empty method.
	// It is possible that it was intended to be called when the sign-in is successful,
	// But it is not being used in this version of the code.
	fun signInSuccess(user: User) {
		hideProgressDialog()
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}