package com.livbogdan.examenproject.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.livbogdan.examenproject.activitys.*
import com.livbogdan.examenproject.models.Board
import com.livbogdan.examenproject.models.User
import com.livbogdan.examenproject.utils.Constants

// This class contains functions for interacting with the Firebase Firestore database.
class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    // This function is used to register a new user to the Firestore database.
    // It takes an activity object and user information as parameters.
    // It adds the user information to the Firestore database under the USERS collection.
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

    fun createBoard(activity: CreateBoardActivity, board: Board) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,
                    "Board Created successfully.")
                Toast.makeText(activity,
                "Board created successfully.", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                "Error Writing document",e)
            }
    }

    // This function is used to get the current user ID.
    // It returns the current user ID as a string.
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

    // This function is used to update the user profile data in the Firestore database.
    // It takes an activity object and a hashmap of user information as parameters.
    fun updateUserProfileData(activity: MyProfileActivity,
                              userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data Updated")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener {
                e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "error while creating",
                    e
                )

                Toast.makeText(activity, "Profile updated error!", Toast.LENGTH_SHORT).show()
            }
    }

    // This function is used to load the user data from the Firestore database.
    // It takes an activity object as a parameter.
    // It gets the user information from the USERS collection in the Firestore database and returns it to the activity.
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