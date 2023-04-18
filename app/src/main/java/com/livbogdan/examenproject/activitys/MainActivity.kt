package com.livbogdan.examenproject.activitys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.adapters.BoardItemsAdapter
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.Board
import com.livbogdan.examenproject.models.User
import com.livbogdan.examenproject.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

	private val startMyProfileActivityForResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			if (result.resultCode == RESULT_OK) {
				FirestoreClass().loadUserData(this, true)
			} else {
				Log.e("cancelled", "cancelled")
			}
		}

	private val updateBoardList =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
			if (result.resultCode == RESULT_OK) {
				FirestoreClass().getBoardsList(this)
			} else {
				Log.e("cancelled", "cancelled")
			}
		}

	private lateinit var mUserName: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setupActionBar()

		val navView: NavigationView = findViewById(R.id.nav_view)
		navView.setNavigationItemSelectedListener(this)

		FirestoreClass().loadUserData(this, true)

		val fabBoard: FloatingActionButton = findViewById(R.id.fab_create_board)
		fabBoard.setOnClickListener {
			val intent = Intent(this,
				CreateBoardActivity::class.java)
			intent.putExtra(Constants.NAME, mUserName)
			updateBoardList.launch(intent)
		}

	}


	// Sets up the action bar with a navigation icon and an onClickListener that toggles the navigation drawer.
	private fun setupActionBar() {
		val toolbar: Toolbar = findViewById(R.id.tb_main_activity)
		setSupportActionBar(toolbar)
		toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

		toolbar.setNavigationOnClickListener {
			toggleDrawer()
		}
	}

	// Toggles the navigation drawer when the navigation icon is clicked.
	private fun toggleDrawer() {
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START)
		} else {
			drawer.openDrawer(GravityCompat.START)
		}
	}

	// Handles the back button press by closing the navigation drawer if it is open,
	// Otherwise it delegates to the onBackPressed method of the onBackPressedDispatcher.
	override fun onBackPressed() {
		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START)
		} else {
			onBackPressedDispatcher.onBackPressed()
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

	fun updateNavigationUserDetails(user: User, readBoardList: Boolean) {

		mUserName = user.name

		val navUserImage: CircleImageView = findViewById(R.id.iv_user_image)
		val tvUsername: TextView = findViewById(R.id.tv_username)

		Glide
			.with(this)
			.load(user.image)
			.centerCrop()
			.placeholder(R.drawable.ic_user_place_holder)
			.into(navUserImage)

		tvUsername.text = user.name

		if(readBoardList){
			showProgressDialog(resources.getString(R.string.please_wait))
			FirestoreClass().getBoardsList(this)
		}
	}

	fun populateBoardListToUI(boardList: ArrayList<Board>){
		hideProgressDialog()
		val recyclerView: RecyclerView = findViewById(R.id.rv_Board_List)
		val textView: TextView = findViewById(R.id.tv_no_boards_available)

		if (boardList.size > 0) {

			recyclerView.visibility = View.VISIBLE
			textView.visibility = View.GONE

			recyclerView.layoutManager = LinearLayoutManager(this)
			recyclerView.setHasFixedSize(true)


			val adapter = BoardItemsAdapter(this, boardList)
			recyclerView.adapter = adapter
			adapter.setOnClickListener(object :
				BoardItemsAdapter.OnClickListener {
				override fun onClick(position: Int, model: Board) {
					val intent = Intent(this@MainActivity, TaskListAktivity::class.java)
					intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
					startActivity(intent)
				}
			})

		} else {
			recyclerView.visibility = View.GONE
			textView.visibility = View.VISIBLE
		}

	}
}