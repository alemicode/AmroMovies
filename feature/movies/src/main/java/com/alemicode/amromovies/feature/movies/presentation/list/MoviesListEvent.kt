package com.alemicode.amromovies.feature.movies.presentation.list

sealed interface MoviesListEvent {
    data class NavigateToDetail(val movieId: Int) : MoviesListEvent
}
