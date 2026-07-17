package com.alemicode.amromovies.feature.movies.domain.usecase

import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder

fun List<Movie>.sortedByField(field: SortField, order: SortOrder): List<Movie> {
    val comparator = when (field) {
        SortField.POPULARITY -> compareBy<Movie> { it.popularity }
        SortField.TITLE -> compareBy { it.title.lowercase() }
        SortField.RELEASE_DATE -> compareBy { it.releaseDate }
    }
    return sortedWith(if (order == SortOrder.DESCENDING) comparator.reversed() else comparator)
}
