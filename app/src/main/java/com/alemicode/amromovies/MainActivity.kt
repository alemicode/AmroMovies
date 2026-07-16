package com.alemicode.amromovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.feature.movies.presentation.navigation.MoviesListRoute
import com.alemicode.amromovies.feature.movies.presentation.navigation.moviesGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmroApp()
        }
    }
}

@Composable
private fun AmroApp() {
    AmroTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = MoviesListRoute) {
            moviesGraph(navController)
        }
    }
}
