package com.livbogdan.examenproject.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.livbogdan.examenproject.R
import com.livbogdan.examenproject.models.ClockViewModel
import kotlinx.coroutines.launch

class TimerActivity : AppCompatActivity() {

    private lateinit var viewModel: ClockViewModel
    private var isTimerRunning = false
    private var isTimerPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)


        //#region UI id variable
        val btnStartStop: Button = findViewById(R.id.button)
        val btnPauseResume: Button = findViewById(R.id.button2)
        val btnReset: Button = findViewById(R.id.button3)
        val tvHours: TextView = findViewById(R.id.tv_hours)
        val tvMinutes: TextView = findViewById(R.id.tv_minutes)
        val tvSeconds: TextView = findViewById(R.id.tv_seconds)
        //#endregion

        //#region View Model
        viewModel = ViewModelProvider(this)[ClockViewModel::class.java]



        lifecycleScope.launch {
            viewModel.timeElapsedStateFlow.collect { timeElapsed ->
                // Update the UI with the new number of seconds elapsed
                tvHours.text = String.format("%02d", timeElapsed.first)
                tvMinutes.text = String.format("%02d", timeElapsed.second)
                tvSeconds.text = String.format("%02d", timeElapsed.third)
            }
        }
        //#endregion

        //#region Buttons
        //TODO Assign correct text to buttons
        btnStartStop.setOnClickListener {
            if (isTimerRunning) {
                viewModel.stopTimer()
                isTimerRunning = false
                btnStartStop.text = getString(R.string.start_button_text)
                btnPauseResume.isEnabled = false
                btnPauseResume.text = getString(R.string.pause_button_text)
                isTimerPaused = false
            } else {
                viewModel.startTimer()
                isTimerRunning = true
                btnStartStop.text = getString(R.string.stop_button_text)
                btnPauseResume.isEnabled = true
                btnPauseResume.text = getString(R.string.pause_button_text)
                isTimerPaused = false
            }
        }

        btnPauseResume.setOnClickListener {
            isTimerPaused = if (isTimerPaused) {
                btnPauseResume.text = getString(R.string.pause_button_text)
                viewModel.startTimer()
                false
            } else {
                btnPauseResume.text = getString(R.string.resume_button_text)
                viewModel.pauseTimer()
                true
            }
        }

        btnReset.setOnClickListener {
            viewModel.stopTimer()
            viewModel.resetTimer()
            tvHours.text = "00"
            tvMinutes.text = "00"
            tvSeconds.text = "00"
            isTimerRunning = false
            isTimerPaused = false
            btnStartStop.text = getString(R.string.stop_button_text)
            btnPauseResume.isEnabled = false
            btnPauseResume.text = getString(R.string.stop_button_text)
        }
        //#endregion

        setupActionBar()
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.tb_timer_activity)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_icon)
            title = "TIMER"
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }
}