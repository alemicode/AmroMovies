package com.alemicode.amromovies.feature.movies.presentation.detail

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.TestName
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo

class MovieDetailScreenSnapshotTest {

    private lateinit var paparazzi: Paparazzi

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_9).apply {
            setup(
                testName = TestName(
                    packageName = testInfo.testClass.get().`package`?.name.orEmpty(),
                    className = testInfo.testClass.get().simpleName,
                    methodName = testInfo.testMethod.get().name,
                ),
            )
        }
    }

    @AfterEach
    fun tearDown() {
        paparazzi.teardown()
    }

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
                    state = MovieDetailState(movie = sampleMovie, isLoading = false, hasError = false),
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
                    state = MovieDetailState(movie = sampleMovie, isLoading = false, hasError = false),
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
                    state = MovieDetailState(isLoading = true),
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
                    state = MovieDetailState(isLoading = false, hasError = true),
                    onAction = {},
                    onBackClick = {},
                )
            }
        }
    }
}
