package com.alemicode.amromovies.feature.movies.data.remote

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.core.network.safeApiCall
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreListResponseDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.TrendingMoviesResponseDto

private const val LANGUAGE = "en-US"

class RemoteMoviesDataSource(private val api: TmdbApiService) {

    suspend fun getTrendingMovies(page: Int): Result<TrendingMoviesResponseDto, DataError.Network> =
        safeApiCall { api.getTrendingMovies(page = page, language = LANGUAGE) }

    suspend fun getGenres(): Result<GenreListResponseDto, DataError.Network> =
        safeApiCall { api.getGenres(language = LANGUAGE) }

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetailDto, DataError.Network> =
        safeApiCall { api.getMovieDetail(movieId = movieId, language = LANGUAGE) }
}
