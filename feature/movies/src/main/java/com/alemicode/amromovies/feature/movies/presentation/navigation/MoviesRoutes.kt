package com.alemicode.amromovies.feature.movies.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MoviesListRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)
