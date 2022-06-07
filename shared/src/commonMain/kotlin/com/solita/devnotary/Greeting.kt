package com.solita.devnotary


class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }

}