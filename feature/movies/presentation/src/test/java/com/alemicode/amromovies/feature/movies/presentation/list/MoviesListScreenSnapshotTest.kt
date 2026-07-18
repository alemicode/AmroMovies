package com.alemicode.amromovies.feature.movies.presentation.list

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import org.junit.Rule
import org.junit.Test

class MoviesListScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_9)

    private val sampleMovies = listOf(
        MovieUi(
            id = 1,
            title = "Spiderman Across The Spider Verse",
            posterUrl = null,
            genreLabel = "Action • Comedy • Super Hero • Animation",
            year = "2023",
        ),
        MovieUi(
            id = 2,
            title = "Interstellar",
            posterUrl = null,
            genreLabel = "Adventure • Mystery • Drama",
            year = "2016",
        ),
    )

    private val sampleGenreFilters = listOf(
        GenreFilterUi(id = null, label = "All", selected = true),
        GenreFilterUi(id = 1, label = "Action", selected = false),
        GenreFilterUi(id = 2, label = "Adventure", selected = false),
    )

    @Test
    fun `movies list content in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MoviesListScreen(
                    state = MoviesListState(
                        movies = sampleMovies,
                        genreFilters = sampleGenreFilters,
                        sortField = SortField.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
                        isLoading = false,
                        hasError = false,
                    ),
                    onAction = {},
                )
            }
        }
    }

    @Test
    fun `movies list content in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MoviesListScreen(
                    state = MoviesListState(
                        movies = sampleMovies,
                        genreFilters = sampleGenreFilters,
                        sortField = SortField.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
                        isLoading = false,
                        hasError = false,
                    ),
                    onAction = {},
                )
            }
        }
    }

    @Test
    fun `movies list loading state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MoviesListScreen(
                    state = MoviesListState(isLoading = true),
                    onAction = {},
                )
            }
        }
    }

    @Test
    fun `movies list error state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MoviesListScreen(
                    state = MoviesListState(isLoading = false, hasError = true),
                    onAction = {},
                )
            }
        }
    }
}
