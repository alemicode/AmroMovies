package com.alemicode.amromovies.feature.movies.domain.repository

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    /** Always backed by the local cache - never errors, may just be empty until the first refresh. */
    fun observeTrendingMovies(): Flow<List<Movie>>

    /** Fetches this week's top 100 trending movies from TMDB and writes them into the cache. */
    suspend fun refreshTrendingMovies(): Result<Unit, DataError>

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail, DataError>
}
