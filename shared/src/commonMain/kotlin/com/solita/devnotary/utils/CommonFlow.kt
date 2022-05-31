package com.solita.devnotary.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> MutableStateFlow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this,this.value)

class CommonFlow<T>(private val origin: Flow<T>, private val initialValue : T) : Flow<T> by origin {

    private var _value : T = initialValue
    val value : T = _value

    init {
        keepValueUpdated()
    }

    private fun keepValueUpdated() : Closeable{
        val job = Job()
        onEach { _value = it }.launchIn(CoroutineScope(job + Dispatchers.Main))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }

    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()
        onEach { block(it) }.launchIn(CoroutineScope(job + Dispatchers.Main))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}


interface Closeable {
    fun close()
}