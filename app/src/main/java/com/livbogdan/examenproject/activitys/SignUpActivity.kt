package com.livbogdan.examenproject.activitys


import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.User

@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {
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

	 //MAYBE NEED TO DELETE
	private fun signUpButton(){
		val btnSignUp: Button = findViewById(R.id.btn_sign_in)
		btnSignUp.setOnClickListener {
			registeredUser()
			//startActivity(Intent(this, SignUpActivity::class.java))
		}
	}


	private  fun registeredUser(){
		val etName: EditText = findViewById(R.id.et_name_sign_up)
		val etEmail: EditText = findViewById(R.id.et_email_sign_up)
		val etPassword: EditText = findViewById(R.id.et_pass_sign_up)

		val name:String = etName.text.toString().trim {it <= ' '}
		val email:String = etEmail.text.toString().trim {it <= ' '}
		val password:String = etPassword.text.toString().trim {it <= ' '}

		if (validForm(name, email, password)){
			showProgressDialog(resources.getString(R.string.please_wait))
			FirebaseAuth.getInstance()
				.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

					if (task.isSuccessful) {
						val firebaseUser: FirebaseUser = task.result!!.user!!
						val registeredEmail = firebaseUser.email!!
						val user = User(firebaseUser.uid, name, registeredEmail)

						FirestoreClass().registeredUser(this, user)

					} else {
						Toast.makeText(
							this,
							"Registration Failed", Toast.LENGTH_SHORT)
							.show()
					}
				}
		}
	}

	private fun validForm(name: String, email: String, password: String): Boolean {
		return when {
			TextUtils.isEmpty(name) -> {
				showErrorSnackBar("Please enter a name")
				false
			}
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

	fun userRegisterSuccess() {
		Toast.makeText(
			this," you have " +
					"successfully registered", Toast.LENGTH_LONG
		).show()

		hideProgressDialog()
		FirebaseAuth.getInstance().signOut()
		finish()
	}
}