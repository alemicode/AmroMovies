package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository

class GetMovieDetailUseCase(private val repository: MoviesRepository) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetail, DataError> =
        repository.getMovieDetail(movieId)
}
