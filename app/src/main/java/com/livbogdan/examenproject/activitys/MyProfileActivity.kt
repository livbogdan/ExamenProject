package com.livbogdan.examenproject.activitys

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.User
import com.livbogdan.examenproject.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class MyProfileActivity : BaseActivity() {


    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User             // Object that will store the user details.
    private var mProfileImgURL: String = ""             // That will store the URL of the user's profile image.

    // Is a permission launcher to request the READ_EXTERNAL_STORAGE permission.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showImageChooser()
            } else {
                Toast.makeText(
                    this,
                    "You denied the permission for storage",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // Is an activity launcher to pick an image from the gallery.
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                mSelectedImageFileUri = result.data?.data

                val profileUserImage: CircleImageView = findViewById(R.id.iv_my_profile_user_image)
                try {
                    Glide
                        .with(this)
                        .load(mSelectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(profileUserImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirestoreClass().loadUserData(this)

        val profileUserImage: CircleImageView = findViewById(R.id.iv_my_profile_user_image)
        profileUserImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        val btnUpdate: Button = findViewById(R.id.btn_update)
        btnUpdate.setOnClickListener {
            if (mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
            }
        }
    }

    private fun showImageChooser() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickImageLauncher.launch(galleryIntent)
    }

    // That will be called when the user grants or denies permission for storage.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            //Show Image chooser
            showImageChooser()
        } else {
            Toast.makeText(
                this,
                "You denied the permission for storage",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Set up the toolbar of the activity.
    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.tb_my_profile_activity)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            title = resources.getString(R.string.my_profile)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Set the user details in the UI.
    fun setUserDataUI(user: User){
        val navUserImage: CircleImageView = findViewById(R.id.iv_my_profile_user_image)
        val etName: EditText = findViewById(R.id.et_name_my_profile)
        val etEmail: EditText = findViewById(R.id.et_email_my_profile)
        val etMobile: EditText = findViewById(R.id.et_mobile_my_profile)

        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navUserImage)

        etName.setText(user.name)
        etEmail.setText(user.email)
        if (user.mobile != 0L){
            etMobile.setText(user.mobile.toString())
        }
    }

    // Function that will update the user profile data in Firestore.
    private fun updateUserProfileData() {
        val userHashmap = HashMap<String, Any>()
        var anyChangesMade = false

        val etName: EditText = findViewById(R.id.et_name_my_profile)
        val etMobile: EditText = findViewById(R.id.et_mobile_my_profile)

        if (mProfileImgURL.isNotEmpty() && mProfileImgURL != mUserDetails.image) {
            userHashmap[Constants.IMAGE] = mProfileImgURL
            anyChangesMade = true
        }

        if (etName.text.toString() != mUserDetails.name) {
            userHashmap[Constants.NAME] = etName.text.toString()
            anyChangesMade = true
        }

        if (etMobile.text.toString() != mUserDetails.mobile.toString()) {
            userHashmap[Constants.MOBILE] = etMobile.text.toString().toLong()
            anyChangesMade = true
        }

        if (anyChangesMade) {
            FirestoreClass().updateUserProfileData(this, userHashmap)
        }
    }

    // Upload the user's profile image to Firebase Storage.
    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectedImageFileUri != null){
            val sRef: StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_Image" + System.currentTimeMillis()
                            + "." + getFileExtension(mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot ->
                Log.e(
                    "Firebase img url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.e("Downloadable img url", uri.toString())
                    mProfileImgURL = uri.toString()
                    updateUserProfileData()
                }
            }.addOnFailureListener {
                exception ->
                Toast.makeText(
                    this,
                    exception.message,
                    Toast.LENGTH_LONG
                ).show()
                hideProgressDialog()
            }
        }
    }

    private fun getFileExtension(uri: Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}