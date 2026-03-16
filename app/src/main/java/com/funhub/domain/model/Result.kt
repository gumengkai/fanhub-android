package com.funhub.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val message: String = exception.message ?: "Unknown error") : Result<Nothing>()
    data class Loading(val message: String = "Loading...") : Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Throwable, String) -> Unit): Result<T> {
    if (this is Result.Error) action(exception, message)
    return this
}

inline fun <T> Result<T>.onLoading(action: (String) -> Unit): Result<T> {
    if (this is Result.Loading) action(message)
    return this
}
