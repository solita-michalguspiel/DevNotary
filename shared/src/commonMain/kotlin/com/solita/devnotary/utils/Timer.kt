package com.solita.devnotary.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import kotlinx.datetime.Clock

interface MyClock{
  val now: Long
}

class MyClockImpl: MyClock{
  override val now: Long
    get() = Clock.System.now().epochSeconds
}

class MyClockFakeImpl(var fakeNow: Long) : MyClock{

  override val now: Long get() = fakeNow
  fun iterateClockTimeBySecond(){
    fakeNow++
  }
}

class Timer constructor(private val seconds: Int,private val clock: MyClock = MyClockImpl()) {

  private var timerJob: Job? = null
  private var lastEmittedValue = -1

  fun startTimer(): Flow<Int> = flow {
    suspend fun FlowCollector<Int>.checkAndEmit(seconds: Int) {
      if (seconds != lastEmittedValue) {
        emit(seconds)
        lastEmittedValue = seconds
      }
    }
    val startTime = clock.now
    val finishTime = startTime + seconds
    checkAndEmit(seconds)//emit(seconds) // SO THAT THE TIMER STARTS RIGHT AWAY
    coroutineScope {
      val coroutine = this.coroutineContext
      timerJob = coroutine.job
      while (clock.now <= finishTime) {
        delay(50)
        val timeLeft = (finishTime - clock.now).toInt()
        if (timeLeft < 0) checkAndEmit(0)
        else checkAndEmit(timeLeft)
      }
      timerJob?.cancel()
    }
  }

  fun stopTimer() {
    timerJob?.cancel()
  }
}
