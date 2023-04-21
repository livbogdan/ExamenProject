package com.livbogdan.examenproject.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.activitys.TaskListAktivity
import com.livbogdan.examenproject.models.Task

open class TaskListAdapter(
    private val context: Context,
    private var list: ArrayList<Task>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_task, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.9).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(
            (15.toDp().toPx()), 0, (40.toDp()).toPx(),0)
        view.layoutParams = layoutParams

        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("CutPasteId")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            // New
            //#region All holders item view variable
            val tvAddTaskList = holder.itemView.findViewById<TextView>(R.id.tv_add_task_list)
            val llTaskItem = holder.itemView.findViewById<LinearLayout>(R.id.ll_task_item)
            val tvTaskListTitle = holder.itemView.findViewById<TextView>(R.id.tv_task_list_title)
            val cvAddTaskListName = holder.itemView.findViewById<CardView>(R.id.cv_add_task_list_name)
            val ibCloseListName = holder.itemView.findViewById<ImageButton>(R.id.ib_close_list_name)
            val ibDoneListName = holder.itemView.findViewById<ImageButton>(R.id.ib_done_list_name)
            val etTaskListName = holder.itemView.findViewById<EditText>(R.id.et_task_list_name)
            val ibEditListName = holder.itemView.findViewById<ImageButton>(R.id.ib_edit_list_name)
            val llTitleView = holder.itemView.findViewById<LinearLayout>(R.id.ll_title_view)
            val cvEditTaskListName = holder.itemView.findViewById<CardView>(R.id.cv_edit_task_list_name)
            val ibCloseEditableView = holder.itemView.findViewById<ImageButton>(R.id.ib_close_editable_view)
            val ibDoneEditListName = holder.itemView.findViewById<ImageButton>(R.id.ib_done_edit_list_name)
            val etEditTaskListName = holder.itemView.findViewById<EditText>(R.id.et_edit_task_list_name)
            val ibDeleteList = holder.itemView.findViewById<ImageButton>(R.id.ib_delete_list)
            val tvAddCard = holder.itemView.findViewById<TextView>(R.id.tv_add_card)
            val cvAddCard = holder.itemView.findViewById<CardView>(R.id.cv_add_card)
            val ibCloseCardName = holder.itemView.findViewById<ImageButton>(R.id.ib_close_card_name)
            val ibDoneCardName = holder.itemView.findViewById<ImageButton>(R.id.ib_done_card_name)
            val etCardName = holder.itemView.findViewById<EditText>(R.id.et_card_name)
            val rvCardList = holder.itemView.findViewById<RecyclerView>(R.id.rv_card_list)
            //#endregion

            if (position == list.size - 1) {
                tvAddTaskList.visibility = View.VISIBLE
                llTaskItem.visibility = View.GONE
            } else {
                tvAddTaskList.visibility = View.GONE
                llTaskItem.visibility = View.VISIBLE
            }

            tvTaskListTitle.text = model.title

            tvAddTaskList.setOnClickListener {
                tvAddTaskList.visibility = View.GONE
                cvAddTaskListName.visibility = View.VISIBLE
            }

            ibCloseListName.setOnClickListener {
                tvAddTaskList.visibility = View.VISIBLE
                cvAddTaskListName.visibility = View.GONE
            }

            ibDoneListName.setOnClickListener {
                val listName = etTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListAktivity) {
                        context.createTaskList(listName)
                    }
                } else {
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                }
            }

            ibEditListName.setOnClickListener {
                etEditTaskListName.setText(model.title)
                llTitleView.visibility = View.GONE
                cvEditTaskListName.visibility = View.VISIBLE
            }

            ibCloseEditableView.setOnClickListener {
                llTitleView.visibility = View.VISIBLE
                cvEditTaskListName.visibility = View.GONE
            }

            ibDoneEditListName.setOnClickListener {
                val listName = etEditTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListAktivity) {
                        context.updateTaskList(position, listName, model)
                    }
                } else {
                    Toast.makeText(context, "Please Enter a List Name.", Toast.LENGTH_SHORT).show()
                }
             }


            ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(position, model.title)
            }

            tvAddCard.setOnClickListener {
                tvAddCard.visibility = View.GONE
                cvAddCard.visibility = View.VISIBLE

                ibCloseCardName.setOnClickListener {
                    tvAddCard.visibility = View.VISIBLE
                    cvAddCard.visibility = View.GONE
                }

                ibDoneCardName.setOnClickListener {
                    val cardName = etCardName.text.toString()
                    if (cardName.isNotEmpty()) {
                        if (context is TaskListAktivity) {
                            context.addCardToTaskList(position, cardName)
                        }
                    } else {
                        Toast.makeText(context, "Please Enter Card Name.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

           rvCardList.layoutManager = LinearLayoutManager(context)
           rvCardList.setHasFixedSize(true)

           val adapter = CardListAdapter(context, model.cards)
           rvCardList.adapter = adapter

           }

    }

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed

            if (context is TaskListAktivity) {
                context.deleteTaskList(position)
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}

