package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.compose.runtime.Stable
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder

@Stable
data class MoviesListState(
    val movies: List<MovieUi> = emptyList(),
    val genreFilters: List<GenreFilterUi> = emptyList(),
    val sortField: SortField = SortField.POPULARITY,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
