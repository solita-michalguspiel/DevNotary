package com.solita.devnotary

import com.benasher44.uuid.uuid4
import com.solita.devnotary.utils.Crypto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class TestCrypto {

    @Test
    fun testCrypto(){
        val myCrypto = Crypto()
        val message = "Very important message let's encrypt that and make sure nobody can read it!!!"
        val key = uuid4()
        val encryptedMessage = myCrypto.encryptMessage(key.toString(),message)
        val encryptedMessageAsString = encryptedMessage.map{it.toByte()}.toByteArray().decodeToString()
        println()
        println("Encrypted message : $encryptedMessageAsString")
        val decryptedMessage = myCrypto.decryptMessage(key.toString(),encryptedMessage)
        println(decryptedMessage)
        encryptedMessage shouldNotBe message
        decryptedMessage shouldBe message
    }


    @Test
    fun testByteEncoding(){
        val originalMessage = "Very important message let's encrypt that and make sure nobody can read it!!!"
        val message = originalMessage.encodeToByteArray().decodeToString().encodeToByteArray().decodeToString()
        message shouldBe originalMessage
    }

}