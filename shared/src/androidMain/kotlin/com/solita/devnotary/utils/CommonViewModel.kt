package com.solita.devnotary.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class CommonViewModel actual constructor() : ViewModel() {
    protected actual val sharedScope: CoroutineScope = viewModelScope

    public actual override fun onCleared() {
        super.onCleared()
    }
}