package com.livbogdan.examenproject.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.livbogdan.examenproject.activitys.MainActivity
import com.livbogdan.examenproject.activitys.MyProfileActivity
import com.livbogdan.examenproject.activitys.SignInActivity
import com.livbogdan.examenproject.activitys.SignUpActivity
import com.livbogdan.examenproject.models.User
import com.livbogdan.examenproject.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registeredUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }.addOnFailureListener { e-> Log.e(activity.javaClass.simpleName,
                "Error Writing document",e)
            }
    }

    fun getCurrentUserId(): String {

        //#region Auto Login
        // If you want remove auto login. Don't forget to fix Handler in SplashActivity
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
        //#endregion

        /* Get user from Database (Dont need if you has auto login)
        //return FirebaseAuth.getInstance().currentUser!!.uid
        */

    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when(activity){
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataUI(loggedInUser)
                    }
                }

            }.addOnFailureListener {
                    e->
                when(activity){
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("signInUser", "Error writing document", e)
            }
    }
}