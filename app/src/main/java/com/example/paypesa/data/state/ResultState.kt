package com.example.paypesa.data.state

sealed class ResultState<out T: Any?> {

    object Loading: ResultState<Nothing>()
    data class Success<T: Any?>(val data: T?): ResultState<T>()
    data class Failure(val exception: Throwable?, val message: String? = null): ResultState<Nothing>()
}