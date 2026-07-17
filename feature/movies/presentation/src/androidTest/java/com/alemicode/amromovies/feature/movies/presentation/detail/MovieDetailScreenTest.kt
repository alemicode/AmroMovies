package com.alemicode.amromovies.feature.movies.presentation.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isTrue
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.feature.movies.presentation.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val retryLabel = context.getString(R.string.action_retry)
    private val imdbLabel = context.getString(R.string.movie_detail_view_on_imdb)
    private val backContentDescription = context.getString(R.string.cd_navigate_back)

    private val sampleMovie = MovieDetailUi(
        id = 1,
        title = "Interstellar",
        tagline = "Mankind was born on Earth. It was never meant to die here.",
        posterUrl = null,
        genreLabel = "Adventure • Mystery • Drama",
        overview = "A team of explorers travel through a wormhole in space.",
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
    fun contentStateShowsMovieDetails() {
        composeTestRule.setContent {
            AmroTheme {
                MovieDetailScreen(
                    state = MovieDetailState(movie = sampleMovie, isLoading = false, hasError = false),
                    onAction = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText(sampleMovie.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMovie.tagline!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMovie.status).assertIsDisplayed()
        composeTestRule.onNodeWithText(imdbLabel).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun errorStateShowsRetryButtonAndInvokesActionOnClick() {
        var retryClicked = false
        composeTestRule.setContent {
            AmroTheme {
                MovieDetailScreen(
                    state = MovieDetailState(isLoading = false, hasError = true),
                    onAction = { action -> retryClicked = action is MovieDetailAction.OnRetryClick },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText(retryLabel).performClick()

        assertThat(retryClicked).isTrue()
    }

    @Test
    fun backButtonInvokesOnBackClick() {
        var backClicked = false
        composeTestRule.setContent {
            AmroTheme {
                MovieDetailScreen(
                    state = MovieDetailState(movie = sampleMovie, isLoading = false, hasError = false),
                    onAction = {},
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(backContentDescription).performClick()

        assertThat(backClicked).isTrue()
    }
}
