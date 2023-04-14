package com.livbogdan.examenproject.activitys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.User
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

	private val startMyProfileActivityForResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			if (result.resultCode == RESULT_OK) {
				FirestoreClass().loadUserData(this)
			} else {
				Log.e("cancelled", "cancelled")
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setupActionBar()

		val navView: NavigationView = findViewById(R.id.nav_view)
		navView.setNavigationItemSelectedListener(this)

		FirestoreClass().loadUserData(this)

		val fabBoard: FloatingActionButton = findViewById(R.id.fab_create_board)
		fabBoard.setOnClickListener {
			startActivity(Intent(this,
				CreateBoardActivity::class.java))
		}

	}

	private fun setupActionBar() {
		val toolbar: Toolbar = findViewById(R.id.tb_main_activity)
		setSupportActionBar(toolbar)
		toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

		toolbar.setNavigationOnClickListener {
			toggleDrawer()
		}
	}

	private fun toggleDrawer() {
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START)
		} else {
			drawer.openDrawer(GravityCompat.START)
		}
	}

	override fun onBackPressed() {
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.nav_my_profile -> {
				startMyProfileActivityForResult.launch(Intent(this, MyProfileActivity::class.java))
				Toast.makeText(this, "My profile", Toast.LENGTH_SHORT).show()
			}
			R.id.nav_sign_out -> {
				FirebaseAuth.getInstance().signOut()

				val intent = Intent(this, IntroActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
				finish()

				Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show()
			}
		}

		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
		drawer.closeDrawer(GravityCompat.START)

		return true
	}

	fun updateNavigationUserDetails(user: User) {
		val navUserImage: CircleImageView = findViewById(R.id.iv_user_image)
		val tvUsername: TextView = findViewById(R.id.tv_username)

		Glide
			.with(this)
			.load(user.image)
			.centerCrop()
			.placeholder(R.drawable.ic_user_place_holder)
			.into(navUserImage)

		tvUsername.text = user.name
	}
}