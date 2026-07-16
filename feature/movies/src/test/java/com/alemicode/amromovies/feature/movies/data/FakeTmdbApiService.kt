package com.alemicode.amromovies.feature.movies.data

import com.alemicode.amromovies.feature.movies.data.remote.TmdbApiService
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreListResponseDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.TrendingMoviesResponseDto

internal class FakeTmdbApiService : TmdbApiService {

    var trendingPages: Map<Int, TrendingMoviesResponseDto> = emptyMap()

    var trendingErrorPage: Int? = null
    var trendingError: Throwable = RuntimeException("trending page fetch failed")

    var genresResponse: GenreListResponseDto = GenreListResponseDto()
    var genresError: Throwable? = null

    var movieDetailResponses: Map<Int, MovieDetailDto> = emptyMap()
    var movieDetailError: Throwable? = null

    override suspend fun getTrendingMovies(page: Int, language: String): TrendingMoviesResponseDto {
        if (page == trendingErrorPage) throw trendingError
        return trendingPages.getValue(page)
    }

    override suspend fun getGenres(language: String): GenreListResponseDto {
        genresError?.let { throw it }
        return genresResponse
    }

    override suspend fun getMovieDetail(movieId: Int, language: String): MovieDetailDto {
        movieDetailError?.let { throw it }
        return movieDetailResponses.getValue(movieId)
    }
}
