package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie

fun List<Movie>.filteredByGenre(genre: Genre?): List<Movie> =
    if (genre == null) this else filter { genre in it.genres }
