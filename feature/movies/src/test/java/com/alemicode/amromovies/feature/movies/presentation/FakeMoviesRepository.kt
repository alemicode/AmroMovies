package com.alemicode.amromovies.feature.movies.presentation

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeMoviesRepository : MoviesRepository {

    private val moviesFlow = MutableStateFlow<List<Movie>>(emptyList())

    var refreshResult: Result<Unit, DataError> = Result.Success(Unit)
    var movieDetailResult: Result<MovieDetail, DataError> = Result.Error(DataError.Network.NOT_FOUND)

    var refreshCallCount = 0
        private set

    override fun observeTrendingMovies(): Flow<List<Movie>> = moviesFlow

    override suspend fun refreshTrendingMovies(): Result<Unit, DataError> {
        refreshCallCount++
        return refreshResult
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail, DataError> = movieDetailResult

    fun emit(movies: List<Movie>) {
        moviesFlow.value = movies
    }
}
