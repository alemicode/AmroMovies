package com.alemicode.amromovies.feature.movies.presentation

import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail

internal fun testMovie(
    id: Int,
    title: String = "Movie $id",
    genres: List<Genre> = emptyList(),
    popularity: Double = 0.0,
): Movie = Movie(
    id = id,
    title = title,
    posterUrl = null,
    genres = genres,
    popularity = popularity,
    releaseDate = null,
)

internal fun testMovieDetail(
    id: Int,
    title: String = "Movie $id",
): MovieDetail = MovieDetail(
    id = id,
    title = title,
    tagline = null,
    posterUrl = null,
    genres = emptyList(),
    overview = "",
    voteAverage = 0.0,
    voteCount = 0,
    budget = 0L,
    revenue = 0L,
    status = "Released",
    imdbUrl = null,
    runtimeMinutes = null,
    releaseDate = null,
)
