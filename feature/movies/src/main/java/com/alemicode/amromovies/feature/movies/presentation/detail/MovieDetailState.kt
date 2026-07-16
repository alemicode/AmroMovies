package com.alemicode.amromovies.feature.movies.presentation.detail

import androidx.compose.runtime.Stable

@Stable
data class MovieDetailState(
    val movie: MovieDetailUi? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)
