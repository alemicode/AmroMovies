package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.feature.movies.domain.model.Movie

fun List<Movie>.filtereMovieByGenre(genreId: Int?): List<Movie> =
    if (genreId == null) {
        this
    } else {
        filter { movie -> movie.genres.any { it.id == genreId } }
    }