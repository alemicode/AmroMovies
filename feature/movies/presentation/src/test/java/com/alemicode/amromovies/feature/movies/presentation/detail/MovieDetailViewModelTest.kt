package com.alemicode.amromovies.feature.movies.presentation.detail

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.core.common.UiState
import com.alemicode.amromovies.core.testing.coroutines.MainDispatcherRule
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import com.alemicode.amromovies.feature.movies.domain.usecase.GetMovieDetailUseCase
import com.alemicode.amromovies.feature.movies.presentation.testMovieDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<MoviesRepository>()

    private fun createViewModel(movieId: Int = 1) = MovieDetailViewModel(
        movieId = movieId,
        getMovieDetail = GetMovieDetailUseCase(repository),
    )

    @Test
    fun `initial state is loading before anything is collected`() = runTest {
        val viewModel = createViewModel()

        assertThat(viewModel.state.value).isEqualTo(UiState.Loading)
    }

    @Test
    fun `collecting state loads the movie detail on success`() = runTest {
        coEvery { repository.getMovieDetail(1) } returns
                Result.Success(testMovieDetail(id = 1, title = "Interstellar"))
        val viewModel = createViewModel(movieId = 1)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value).isEqualTo(
            UiState.Success(testMovieDetail(id = 1, title = "Interstellar").toMovieDetailUi()),
        )
    }

    @Test
    fun `collecting state surfaces an error on failure`() = runTest {
        coEvery { repository.getMovieDetail(any()) } returns Result.Error(DataError.Network.NOT_FOUND)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value).isEqualTo(UiState.Failure(DataError.Network.NOT_FOUND))
    }

    @Test
    fun `OnRetryClick reloads after a failure`() = runTest {
        coEvery { repository.getMovieDetail(any()) } returns Result.Error(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        assertThat(viewModel.state.value).isEqualTo(UiState.Failure(DataError.Network.NO_INTERNET))

        coEvery { repository.getMovieDetail(any()) } returns Result.Success(testMovieDetail(id = 1, title = "Interstellar"))
        viewModel.onAction(MovieDetailAction.OnRetryClick)

        assertThat(viewModel.state.value).isEqualTo(
            UiState.Success(testMovieDetail(id = 1, title = "Interstellar").toMovieDetailUi()),
        )
    }
}
