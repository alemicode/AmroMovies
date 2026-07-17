package com.alemicode.amromovies.feature.movies.presentation.list

import com.alemicode.amromovies.feature.movies.domain.model.SortField

sealed interface MoviesListAction {
    data class OnGenreSelected(val genreId: Int?) : MoviesListAction
    data class OnSortFieldSelected(val field: SortField) : MoviesListAction
    data object OnToggleSortOrder : MoviesListAction
    data class OnMovieClick(val movieId: Int) : MoviesListAction
    data object OnRetryClick : MoviesListAction
}
