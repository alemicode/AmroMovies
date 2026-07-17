package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository

class RefreshTrendingMoviesUseCase(private val repository: MoviesRepository) {
    suspend operator fun invoke(): Result<Unit, DataError> = repository.refreshTrendingMovies()
}
