package com.alemicode.amromovies.feature.movies.presentation.list

import com.alemicode.amromovies.feature.movies.domain.model.Movie

data class GenreFilterUi(
    val id: Int?,
    val label: String,
    val selected: Boolean,
)

fun List<Movie>.toGenreFilters(selectedGenreId: Int?): List<GenreFilterUi> {
    val distinctGenres = flatMap { it.genres }
        .distinctBy { it.id }
        .sortedBy { it.name }

    val allFilter = GenreFilterUi(id = null, label = "All", selected = selectedGenreId == null)
    val genreFilters = distinctGenres.map { genre ->
        GenreFilterUi(id = genre.id, label = genre.name, selected = genre.id == selectedGenreId)
    }
    return listOf(allFilter) + genreFilters
}
