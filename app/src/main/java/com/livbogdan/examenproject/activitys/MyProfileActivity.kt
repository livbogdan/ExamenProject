package com.livbogdan.examenproject.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.User
import de.hdodenhof.circleimageview.CircleImageView

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirestoreClass().loadUserData(this)
    }

    private fun setupActionBar(){
        val toolbar: Toolbar = findViewById(R.id.tb_my_profile_activity)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_icon)
            actionBar.title =resources.getString(R.string.my_profile)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setUserDataUI(user: User){
        val navUserImage: CircleImageView = findViewById(R.id.iv_my_profile_user_image)
        val etName: EditText = findViewById(R.id.et_name_my_profile)
        val etEmail: EditText = findViewById(R.id.et_email_my_profile)
        val etMobile: EditText = findViewById(R.id.et_mobile_my_profile)

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
}