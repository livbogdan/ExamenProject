package com.livbogdan.examenproject.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.models.ClockViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class TimerActivity : AppCompatActivity() {

    private lateinit var viewModel: ClockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        viewModel = ViewModelProvider(this)[ClockViewModel::class.java]

        val myTextView: TextView = findViewById(R.id.textView)
        lifecycleScope.launch {
            viewModel.timeElapsedStateFlow.collect { timeElapsed ->
                // Update the UI with the new number of seconds elapsed
                myTextView.text = String.format(
                    "%02d:%02d:%02d",
                    timeElapsed.first,
                    timeElapsed.second,
                    timeElapsed.third)
            }
        }

        val pauseButton: Button = findViewById(R.id.button3)
        pauseButton.setOnClickListener {
            viewModel.stopTimer()
        }


        val continueButton: Button = findViewById(R.id.button2)
        continueButton.setOnClickListener {
            viewModel.startTimer()
        }

        val resetButton: Button = findViewById(R.id.button)
        resetButton.setOnClickListener {
            viewModel.stopTimer()
            viewModel.resetTimer()
            myTextView.text = "00:00:00"
        }
    }

    //TODO Add ActionBar function
    //TODO Create start Timer Button and visual swap between. Start and continue Button
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }
}