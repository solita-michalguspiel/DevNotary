package com.solita.devnotary.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job

actual class Timer actual constructor(private val seconds: Int){
    private var timerJob: Job? = null

    actual fun startTimer() : Flow<Int> = flow {
        var timeLeft = seconds
        emit(timeLeft)//SO THAT THE TIMER STARTS RIGHT AWAY.
        coroutineScope {
            val coroutine = this.coroutineContext
            timerJob = coroutine.job
            for (i in 0 until seconds) {
                delay(1000)
                timeLeft -= 1
                emit(timeLeft)
                println(timeLeft)
            }
        }
    }

    actual fun stopTimer() {
        timerJob?.cancel()
    }
}