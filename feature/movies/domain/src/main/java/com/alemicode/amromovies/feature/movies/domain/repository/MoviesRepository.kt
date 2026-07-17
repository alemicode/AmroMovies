package com.alemicode.amromovies.feature.movies.domain.repository

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun observeTrendingMovies(): Flow<List<Movie>>

    suspend fun refreshTrendingMovies(): Result<Unit, DataError>

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail, DataError>
}
