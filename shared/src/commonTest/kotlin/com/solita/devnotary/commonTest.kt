package com.solita.devnotary

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {

    @Test
    fun testExample() {
        assertTrue(!Greeting().greeting().contains("Morning"), "Check 'Morning' is not mentioned")
    }
}