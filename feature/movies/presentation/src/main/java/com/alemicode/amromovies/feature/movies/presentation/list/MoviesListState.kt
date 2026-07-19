package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.compose.runtime.Stable
import com.alemicode.amromovies.core.common.UiState
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder

@Stable
data class MoviesListState(
    val content: UiState<List<MovieUi>> = UiState.Loading,
    val genreFilters: List<GenreFilterUi> = emptyList(),
    val selectedGenreId: Int? = null,
    val sortField: SortField = SortField.POPULARITY,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
)
