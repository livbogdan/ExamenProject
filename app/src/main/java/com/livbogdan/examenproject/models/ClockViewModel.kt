package com.livbogdan.examenproject.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

class ClockViewModel : ViewModel() {
    private var timer: Timer? = null
    private var secondsElapsed: Long = 0
    private val _timeElapsedLiveData = MutableLiveData<Triple<Long, Long, Long>>()
    private var savedSecondsElapsed: Long = 0

    val timeElapsedLiveData: LiveData<Triple<Long, Long, Long>>
        get() = _timeElapsedLiveData

    fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                secondsElapsed++
                val timeElapsed = getTimeElapsed(secondsElapsed)
                _timeElapsedLiveData.postValue(timeElapsed)
            }
        }, 0, 1000)
    }

    fun pauseTimer(){
        savedSecondsElapsed = secondsElapsed
        stopTimer()
    }

    fun continueTimer(){
        secondsElapsed = savedSecondsElapsed
        startTimer()
    }

    fun resetTimer() {
        secondsElapsed = 0
        val timeElapsed = getTimeElapsed(secondsElapsed)
        _timeElapsedLiveData.postValue(timeElapsed)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }



    private fun getTimeElapsed(seconds: Long): Triple<Long, Long, Long> {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return Triple(hours, minutes, remainingSeconds)
    }
}