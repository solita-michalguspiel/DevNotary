package com.solita.devnotary

import com.solita.devnotary.utils.MyClockFakeImpl
import com.solita.devnotary.utils.Timer
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimerTest {

  private val mainThreadSurrogate = newSingleThreadContext("UI thread")

  private val testDispatcher = StandardTestDispatcher()

  @BeforeTest
  fun setup() {
    Dispatchers.setMain(testDispatcher)
  }

  @AfterTest
  fun tearDown() {
    Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    mainThreadSurrogate.close()
  }

  val fakeTimeInEpoch: Long = 32142141271L

  @Test
  fun given10SecondsTimerWasStarted_AndStopTimerWasCalledAfterJustOver5Seconds_ItShouldStopAt5Seconds(): TestResult =
    runTest {
      val fakeClock = MyClockFakeImpl(fakeTimeInEpoch)
      var timeLeft = 10
      val timer = Timer(timeLeft, clock = fakeClock)
      launch {
        repeat(5) {
          delay(1000)
          fakeClock.iterateClockTimeBySecond()
        }
      }
      launch {
        timer.startTimer().collect {
          timeLeft = it
          if(timeLeft == 5) cancel("Already 5 seconds left")
        }
      }
      advanceTimeBy(5200)
      timeLeft shouldBe 5
    }
}
