package com.solita.devnotary.utils

import kotlinx.coroutines.CoroutineScope


@Suppress("EmptyDefaultConstructor")
expect open class CommonViewModel() {
    protected val sharedScope: CoroutineScope

    open fun onCleared()
}