package com.alemicode.amromovies.feature.movies.presentation.detail

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.core.testing.coroutines.MainDispatcherRule
import com.alemicode.amromovies.feature.movies.domain.usecase.GetMovieDetailUseCase
import com.alemicode.amromovies.feature.movies.presentation.FakeMoviesRepository
import com.alemicode.amromovies.feature.movies.presentation.testMovieDetail
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

    private val repository = FakeMoviesRepository()

    private fun createViewModel(movieId: Int = 1) = MovieDetailViewModel(
        movieId = movieId,
        getMovieDetail = GetMovieDetailUseCase(repository),
    )

    @Test
    fun `initial state is loading with no movie before anything is collected`() = runTest {
        val viewModel = createViewModel()

        assertThat(viewModel.state.value.isLoading).isTrue()
        assertThat(viewModel.state.value.movie).isNull()
    }

    @Test
    fun `collecting state loads the movie detail on success`() = runTest {
        repository.movieDetailResult = Result.Success(testMovieDetail(id = 1, title = "Interstellar"))
        val viewModel = createViewModel(movieId = 1)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.movie?.title).isEqualTo("Interstellar")
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.hasError).isFalse()
    }

    @Test
    fun `collecting state surfaces an error on failure`() = runTest {
        repository.movieDetailResult = Result.Error(DataError.Network.NOT_FOUND)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.hasError).isTrue()
        assertThat(viewModel.state.value.movie).isNull()
    }

    @Test
    fun `OnRetryClick reloads after a failure`() = runTest {
        repository.movieDetailResult = Result.Error(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        assertThat(viewModel.state.value.hasError).isTrue()

        repository.movieDetailResult = Result.Success(testMovieDetail(id = 1, title = "Interstellar"))
        viewModel.onAction(MovieDetailAction.OnRetryClick)

        assertThat(viewModel.state.value.hasError).isFalse()
        assertThat(viewModel.state.value.movie?.title).isEqualTo("Interstellar")
    }
}
