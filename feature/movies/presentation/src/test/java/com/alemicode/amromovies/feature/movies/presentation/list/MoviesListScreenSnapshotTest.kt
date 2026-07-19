package com.alemicode.amromovies.feature.movies.presentation.list

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.UiState
import com.alemicode.amromovies.core.testing.paparazzi.BasePaparazziMatrixTest
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziDeviceSize
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziFontScale
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziLocaleConfig
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class MoviesListScreenSnapshotTest(
    @TestParameter deviceSize: PaparazziDeviceSize,
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziMatrixTest(deviceSize, locale, fontScale) {

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
                        content = UiState.Success(sampleMovies),
                        genreFilters = sampleGenreFilters,
                        sortField = SortField.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
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
                        content = UiState.Success(sampleMovies),
                        genreFilters = sampleGenreFilters,
                        sortField = SortField.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
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
                    state = MoviesListState(content = UiState.Loading),
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
                    state = MoviesListState(content = UiState.Failure(DataError.Network.UNKNOWN)),
                    onAction = {},
                )
            }
        }
    }
}
