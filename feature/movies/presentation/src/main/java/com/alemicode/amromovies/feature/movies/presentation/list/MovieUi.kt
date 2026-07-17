package com.alemicode.amromovies.feature.movies.presentation.list

import com.alemicode.amromovies.feature.movies.domain.model.Movie

data class MovieUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val genreLabel: String,
    val year: String?,
)

fun Movie.toMovieUi(): MovieUi = MovieUi(
    id = id,
    title = title,
    posterUrl = posterUrl,
    genreLabel = genres.joinToString(" • ") { it.name },
    year = releaseDate?.year?.toString(),
)
