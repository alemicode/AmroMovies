package com.alemicode.amromovies.core.common


sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.alemicode.amromovies.core.common.Error>(val error: E) : Result<Nothing, E>
}