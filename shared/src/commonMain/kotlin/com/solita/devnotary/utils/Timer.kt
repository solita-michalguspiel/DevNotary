package com.solita.devnotary.utils

import kotlinx.coroutines.flow.Flow

expect class Timer(seconds: Int){

    fun startTimer() : Flow<Int>

    fun stopTimer()
}