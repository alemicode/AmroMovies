package com.alemicode.amromovies.feature.movies.data

import com.alemicode.amromovies.feature.movies.data.local.MoviesDao
import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** In-memory fake of the Room DAO - `replaceTrendingMovies` uses the interface's real default body. */
internal class FakeMoviesDao : MoviesDao {

    private val moviesFlow = MutableStateFlow<List<MovieEntity>>(emptyList())
    private val genres = mutableListOf<GenreEntity>()
    private val movieDetails = mutableMapOf<Int, MovieDetailEntity>()

    override fun observeTrendingMovies(): Flow<List<MovieEntity>> = moviesFlow

    override suspend fun clearTrendingMovies() {
        moviesFlow.value = emptyList()
    }

    override suspend fun insertTrendingMovies(movies: List<MovieEntity>) {
        moviesFlow.value = (moviesFlow.value + movies).associateBy { it.id }.values.toList()
    }

    override suspend fun getGenres(): List<GenreEntity> = genres.toList()

    override suspend fun insertGenres(genres: List<GenreEntity>) {
        genres.forEach { genre ->
            this.genres.removeAll { it.id == genre.id }
            this.genres.add(genre)
        }
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetailEntity? = movieDetails[movieId]

    override suspend fun insertMovieDetail(detail: MovieDetailEntity) {
        movieDetails[detail.id] = detail
    }
}
