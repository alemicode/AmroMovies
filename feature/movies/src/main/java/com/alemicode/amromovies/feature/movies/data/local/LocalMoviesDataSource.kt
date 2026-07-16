package com.alemicode.amromovies.feature.movies.data.local

import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

class LocalMoviesDataSource(private val dao: MoviesDao) {

    fun observeTrendingMovies(): Flow<List<MovieEntity>> = dao.observeTrendingMovies()

    suspend fun replaceTrendingMovies(movies: List<MovieEntity>) = dao.replaceTrendingMovies(movies)

    suspend fun getGenres(): List<GenreEntity> = dao.getGenres()

    suspend fun replaceGenres(genres: List<GenreEntity>) = dao.insertGenres(genres)

    suspend fun getMovieDetail(movieId: Int): MovieDetailEntity? = dao.getMovieDetail(movieId)

    suspend fun insertMovieDetail(detail: MovieDetailEntity) = dao.insertMovieDetail(detail)
}
