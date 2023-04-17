package com.livbogdan.examenproject.activitys

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.livbogdan.examenproject.R


open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    //This function shows a progress dialog with the given ´text´ message.
    fun showProgressDialog(text: String){

        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text

        mProgressDialog.show()
    }

    //This function dismisses the progress dialog.
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    //This function returns the UID (User ID) of the current user signed in using Firebase Authentication.
    fun getCurrentUserID(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    //This function handles the behavior of the back button being pressed twice to exit the app.
    //It shows a toast message instructing the user to press the back button again within 2 seconds, and sets a flag to true.
    //If the back button is pressed again within that time, the app exits. If the back button is not pressed again within that time, the flag is reset to false.
    fun doubleBackToExit(){

        val handler = Handler(Looper.getMainLooper())

        if (doubleBackToExitPressedOnce){
            onBackPressedDispatcher.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        handler.postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)

    }

    //This function displays a snackbar at the bottom of the screen with the given 'message' text, and sets the background color of the snackbar to red.
    fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,
            R.color.snackBarErrorColor))

        snackBar.show()
    }

}