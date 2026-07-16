package com.alemicode.amromovies.feature.movies.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alemicode.amromovies.feature.movies.presentation.detail.MovieDetailRoot
import com.alemicode.amromovies.feature.movies.presentation.list.MoviesListRoot

fun NavGraphBuilder.moviesGraph(navController: NavController) {
    composable<MoviesListRoute> {
        MoviesListRoot(
            onMovieClick = { movieId -> navController.navigate(MovieDetailRoute(movieId)) },
        )
    }

    composable<MovieDetailRoute> { backStackEntry ->
        val route: MovieDetailRoute = backStackEntry.toRoute()
        MovieDetailRoot(
            movieId = route.movieId,
            onBackClick = { navController.navigateUp() },
        )
    }
}
