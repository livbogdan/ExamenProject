package com.livbogdan.examenproject.activitys

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.livbogdan.examenproject.R

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setupActionBar()

		val navView: NavigationView =findViewById(R.id.nav_view)
		navView.setNavigationItemSelectedListener(this)
	}

	private fun setupActionBar(){
		val toolbar: Toolbar = findViewById(R.id.tb_main_activity)
		setSupportActionBar(toolbar)
		toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

		toolbar.setNavigationOnClickListener {
			toggleDrawer()
		}
	}

	private fun toggleDrawer(){
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)){
			drawer.closeDrawer(GravityCompat.START)
		}else{
			drawer.openDrawer(GravityCompat.START)
		}
	}

	override fun onBackPressed() {
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)){
			drawer.closeDrawer(GravityCompat.START)
		}else{
			doubleBackToExit()
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when(item.itemId) {
			R.id.nav_my_profile -> {
				Toast.makeText(this, "My profile",
					Toast.LENGTH_SHORT).show()
			}
			R.id.nav_sign_out ->{
				FirebaseAuth.getInstance().signOut()

				val intent = Intent(this, IntroActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
				finish()

				Toast.makeText(this,"Sign Out",
					Toast.LENGTH_SHORT).show()
			}
		}

		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
		drawer.closeDrawer(GravityCompat.START)

		return true
	}
}