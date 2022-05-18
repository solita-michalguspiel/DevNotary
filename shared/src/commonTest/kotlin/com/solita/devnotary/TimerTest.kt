package com.solita.devnotary

import com.solita.devnotary.utils.Timer
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
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

    @Test
    fun given10SecondsTimerWasStarted_AndStopTimerWasCalledAfterJustOver5Seconds_ItShouldStopAt5Seconds(): TestResult =
        runTest {
            var timeLeft = 10
            val timer = Timer(timeLeft)
            launch { timer.startTimer().collect { timeLeft = it } }
            advanceTimeBy(5001)
            timer.stopTimer()
            timeLeft shouldBe 5
        }

}