package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie

/**
 * Filters within the already-loaded list only - never fetches more. Matches the brief: if the
 * top 100 trending movies has 20 comedies, filtering for comedy shows those 20, not a search for
 * 100 comedies across all of TMDB. `genre == null` means "no filter applied".
 */
fun List<Movie>.filteredByGenre(genre: Genre?): List<Movie> =
    if (genre == null) this else filter { genre in it.genres }
