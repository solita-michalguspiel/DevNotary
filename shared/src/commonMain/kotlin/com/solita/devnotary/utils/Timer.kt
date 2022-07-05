package com.solita.devnotary.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import kotlinx.datetime.Clock

class Timer constructor(private val seconds: Int) {

  private var timerJob: Job? = null

  fun startTimer(): Flow<Int> = flow {
    val startTime = Clock.System.now().epochSeconds
    val finishTime = startTime + seconds
    emit(seconds) // SO THAT THE TIMER STARTS RIGHT AWAY
    coroutineScope {
      val coroutine = this.coroutineContext
      timerJob = coroutine.job
      while (Clock.System.now().epochSeconds <= finishTime) {
        delay(900) // SO THAT IT REFRESH MORE OFTEN THAN EVERY 1s
        val timeLeft = (finishTime - Clock.System.now().epochSeconds).toInt()
        if (timeLeft < 0) emit(0)
        else emit(timeLeft)
      }
      timerJob?.cancel()
    }
  }

  fun stopTimer() {
    timerJob?.cancel()
  }
}
