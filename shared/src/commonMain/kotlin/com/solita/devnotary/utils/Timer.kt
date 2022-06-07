package com.solita.devnotary.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job

class Timer(private val seconds: Int){
    private var timerJob: Job? = null

    fun startTimer() : Flow<Int> = flow {
        var timeLeft = seconds
        coroutineScope {
            val coroutine = this.coroutineContext
            timerJob = coroutine.job
            emit(timeLeft)
            for (i in 0 until seconds) {
                delay(1000)
                timeLeft -= 1
                emit(timeLeft)
                println(timeLeft)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}