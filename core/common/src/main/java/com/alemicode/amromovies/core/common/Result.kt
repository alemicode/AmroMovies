package com.alemicode.amromovies.core.common

/**
 * A typed success/failure outcome, used from the data layer up through domain and presentation -
 * anywhere a function can fail with a specific, known error rather than an arbitrary exception.
 */
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.alemicode.amromovies.core.common.Error>(val error: E) : Result<Nothing, E>
}

/** A [Result] whose success side carries no data, only that the operation succeeded. */
typealias EmptyResult<E> = Result<Unit, E>
