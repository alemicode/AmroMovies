package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingMoviesUseCase(private val repository: MoviesRepository) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeTrendingMovies()
}