package com.alemicode.amromovies.feature.movies.presentation.detail

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.UiState
import com.alemicode.amromovies.core.testing.paparazzi.BasePaparazziMatrixTest
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziDeviceSize
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziFontScale
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziLocaleConfig
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class MovieDetailScreenSnapshotTest(
    @TestParameter deviceSize: PaparazziDeviceSize,
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziMatrixTest(deviceSize, locale, fontScale) {

    private val sampleMovie = MovieDetailUi(
        id = 1,
        title = "Interstellar",
        tagline = "Mankind was born on Earth. It was never meant to die here.",
        posterUrl = null,
        genreLabel = "Adventure • Mystery • Drama",
        overview = "A team of explorers travel through a wormhole in space in an " +
            "attempt to ensure humanity's survival.",
        voteAverage = "8.7",
        voteCount = "32,145",
        budget = "$165,000,000",
        revenue = "$677,000,000",
        status = "Released",
        imdbUrl = "https://www.imdb.com/title/tt0816692",
        runtime = "2h 49m",
        releaseDate = "Nov 6, 2014",
    )

    @Test
    fun `movie detail content in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MovieDetailScreen(
                    state = UiState.Success(sampleMovie),
                    onAction = {},
                    onBackClick = {},
                )
            }
        }
    }

    @Test
    fun `movie detail content in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MovieDetailScreen(
                    state = UiState.Success(sampleMovie),
                    onAction = {},
                    onBackClick = {},
                )
            }
        }
    }

    @Test
    fun `movie detail loading state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MovieDetailScreen(
                    state = UiState.Loading,
                    onAction = {},
                    onBackClick = {},
                )
            }
        }
    }

    @Test
    fun `movie detail error state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MovieDetailScreen(
                    state = UiState.Failure(DataError.Network.UNKNOWN),
                    onAction = {},
                    onBackClick = {},
                )
            }
        }
    }
}
