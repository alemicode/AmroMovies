package com.alemicode.amromovies.feature.movies.data.remote

import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreListResponseDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.TrendingMoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(@Query("page") page: Int): TrendingMoviesResponseDto

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreListResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): MovieDetailDto
}
