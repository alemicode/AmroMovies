package com.alemicode.amromovies.core.common

import kotlinx.coroutines.flow.MutableStateFlow

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Failure(val error: DataError) : UiState<Nothing>

    fun getDataOrNull(): T? = (this as? Success)?.data
}

inline fun <T> UiState<T>?.doOnSuccess(action: (T) -> Unit): UiState<T>? {
    if (this is UiState.Success<T>) {
        action(data)
    }
    return this
}

inline fun UiState<*>?.doOnError(action: (DataError) -> Unit): UiState<*>? {
    if (this is UiState.Failure) {
        action(error)
    }
    return this
}

inline fun UiState<*>?.doOnLoading(action: () -> Unit): UiState<*>? {
    if (this is UiState.Loading) {
        action()
    }
    return this
}

val UiState<*>?.isLoading: Boolean
    get() = this is UiState.Loading

val UiState<*>?.isFailure: Boolean
    get() = this is UiState.Failure

val UiState<*>?.isSuccess: Boolean
    get() = this is UiState.Success<*>