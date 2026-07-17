package com.alemicode.amromovies.feature.movies.presentation.detail

sealed interface MovieDetailAction {
    data object OnRetryClick : MovieDetailAction
}
