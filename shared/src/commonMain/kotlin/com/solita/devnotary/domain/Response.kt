package com.solita.devnotary.domain

sealed class Response<out T> {
    object Loading:Response<Nothing>()

    object Empty: Response<Nothing>()

    data class  Success<out T>(
        val data : T
    ):Response<T>()
    data class Error(val message: String):Response<Nothing>()

}