package com.leva.kmm.shared.domain.model

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[Exception=$message"
            Loading -> "Loading"
        }
    }
}

val Result<*>.succeded
    get() = this is Result.Success && data != null

