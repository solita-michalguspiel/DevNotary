package com.solita.devnotary.utils

class Crypto {

    fun encryptMessage(key: String, message: String): Array<Int> {
        val byteKey = key.encodeToByteArray().sumOf { it * key.length }
        val messageAsByteArray = message.encodeToByteArray()
        return messageAsByteArray.map { (it - byteKey).toByte() }.toByteArray().map { it.toInt() }.toTypedArray()
    }

    fun decryptMessage(key: String, message: Array<Int>): String {
        val byteKey = key.encodeToByteArray().sumOf { it * key.length }
        return message.map { (it + byteKey).toByte() }.toByteArray().decodeToString()
    }

}