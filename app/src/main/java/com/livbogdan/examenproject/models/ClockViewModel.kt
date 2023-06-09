package com.livbogdan.examenproject.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Timer
import java.util.TimerTask

class ClockViewModel : ViewModel() {
    private var timer: Timer? = null
    private var secondsElapsed: Long = 0
    private var savedSecondsElapsed: Long = 0
    private var isTimerRunning = false

    private val _timeElapsedStateFlow = MutableStateFlow(Triple(0L, 0L, 0L))
    val timeElapsedStateFlow: StateFlow<Triple<Long, Long, Long>>
        get() = _timeElapsedStateFlow
    private fun getTimeElapsed(seconds: Long): Triple<Long, Long, Long> {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return Triple(hours, minutes, remainingSeconds)
    }

    fun startTimer() {
        if (!isTimerRunning) {
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    secondsElapsed++
                    val timeElapsed = getTimeElapsed(secondsElapsed)
                    _timeElapsedStateFlow.value = timeElapsed
                }
            }, 0, 1000)
            isTimerRunning = true
        }
    }

    fun pauseTimer(){
        if (isTimerRunning) {
            savedSecondsElapsed = secondsElapsed
            stopTimer()
        }
    }

    fun resetTimer() {
        secondsElapsed = 0
        val timeElapsed = getTimeElapsed(secondsElapsed)
        _timeElapsedStateFlow.value = timeElapsed
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

}
