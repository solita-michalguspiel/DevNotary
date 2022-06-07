package com.solita.devnotary.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.native.internal.GC

@Suppress("EmptyDefaultConstructor")
actual open class CommonViewModel actual constructor() {
    protected actual val sharedScope: CoroutineScope = createViewModelScope()

    actual open fun onCleared() {
        sharedScope.cancel()

        dispatch_async(dispatch_get_main_queue()) { GC.collect() }
    }

    fun <T> Flow<T>.watch(block: (T) -> Unit): Closeable {
        val job = Job()
        onEach {
            block(it)
        }.launchIn(
            CoroutineScope(Dispatchers.Main + job)
        )
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

@ThreadLocal
private var createViewModelScope: () -> CoroutineScope = {
    CoroutineScope(createUIDispatcher())
}

private fun createUIDispatcher(): CoroutineDispatcher = UIDispatcher()