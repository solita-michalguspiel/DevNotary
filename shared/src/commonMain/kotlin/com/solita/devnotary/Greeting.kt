package com.solita.devnotary

import kotlinx.coroutines.coroutineScope


class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }


}