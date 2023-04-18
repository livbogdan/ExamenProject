package com.livbogdan.examenproject.activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.firebase.FirestoreClass
import com.livbogdan.examenproject.models.Board
import com.livbogdan.examenproject.utils.Constants

class TaskListAktivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list_aktivity)

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, boardDocumentId)
    }

    private fun setupActionBar(toolBarTitle: String) {
        val toolbar: Toolbar = findViewById(R.id.tb_task_list_activity)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            title = toolBarTitle
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun boardDetails(board: Board){
        hideProgressDialog()
        setupActionBar(board.name)
    }

}