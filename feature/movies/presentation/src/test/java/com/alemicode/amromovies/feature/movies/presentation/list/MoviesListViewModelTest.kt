package com.alemicode.amromovies.feature.movies.presentation.list

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.core.testing.MainDispatcherExtension
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import com.alemicode.amromovies.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.RefreshTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.presentation.FakeMoviesRepository
import com.alemicode.amromovies.feature.movies.presentation.testMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class)
class MoviesListViewModelTest {

    private val repository = FakeMoviesRepository()

    private fun createViewModel() = MoviesListViewModel(
        getTrendingMovies = GetTrendingMoviesUseCase(repository),
        refreshTrendingMovies = RefreshTrendingMoviesUseCase(repository),
    )

    @Test
    fun `initial state is loading with no movies before anything is collected`() = runTest {
        val viewModel = createViewModel()

        assertThat(viewModel.state.value.isLoading).isTrue()
        assertThat(viewModel.state.value.movies).isEmpty()
        assertThat(repository.refreshCallCount).isEqualTo(0)
    }

    @Test
    fun `collecting state triggers a refresh and reflects the repository's movies`() = runTest {
        repository.emit(listOf(testMovie(id = 1, title = "Interstellar")))
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.movies.map { it.id }).isEqualTo(listOf(1))
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(repository.refreshCallCount).isEqualTo(1)
    }

    @Test
    fun `new emissions from the repository update movies while collected`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        repository.emit(listOf(testMovie(id = 1), testMovie(id = 2)))
        assertThat(viewModel.state.value.movies.map { it.id }).isEqualTo(listOf(1, 2))

        repository.emit(listOf(testMovie(id = 3)))
        assertThat(viewModel.state.value.movies.map { it.id }).isEqualTo(listOf(3))
    }

    @Test
    fun `OnGenreSelected filters movies down to that genre`() = runTest {
        val action = Genre(1, "Action")
        val comedy = Genre(2, "Comedy")
        repository.emit(
            listOf(
                testMovie(id = 1, genres = listOf(action)),
                testMovie(id = 2, genres = listOf(comedy)),
            ),
        )
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        viewModel.onAction(MoviesListAction.OnGenreSelected(genreId = 1))

        assertThat(viewModel.state.value.movies.map { it.id }).isEqualTo(listOf(1))
        assertThat(viewModel.state.value.selectedGenreId).isEqualTo(1)
    }

    @Test
    fun `OnGenreSelected with a genre matching nothing yields an empty, non-error result`() = runTest {
        repository.emit(listOf(testMovie(id = 1, genres = listOf(Genre(1, "Action")))))
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        viewModel.onAction(MoviesListAction.OnGenreSelected(genreId = 999))

        assertThat(viewModel.state.value.movies).isEmpty()
        assertThat(viewModel.state.value.hasError).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `OnGenreSelected with null resets the filter back to all movies`() = runTest {
        val action = Genre(1, "Action")
        repository.emit(listOf(testMovie(id = 1, genres = listOf(action)), testMovie(id = 2)))
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        viewModel.onAction(MoviesListAction.OnGenreSelected(genreId = 1))

        viewModel.onAction(MoviesListAction.OnGenreSelected(genreId = null))

        assertThat(viewModel.state.value.movies.map { it.id }).isEqualTo(listOf(1, 2))
        assertThat(viewModel.state.value.selectedGenreId).isEqualTo(null)
    }

    @Test
    fun `genre filters expose All plus the distinct genres from the loaded movies`() = runTest {
        repository.emit(
            listOf(
                testMovie(id = 1, genres = listOf(Genre(1, "Action"), Genre(2, "Comedy"))),
                testMovie(id = 2, genres = listOf(Genre(1, "Action"))),
            ),
        )
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.genreFilters.map { it.label }).isEqualTo(listOf("All", "Action", "Comedy"))
    }

    @Test
    fun `OnSortFieldSelected sorts movies by title in the current sort order`() = runTest {
        repository.emit(listOf(testMovie(id = 1, title = "Alpha"), testMovie(id = 2, title = "Zeta")))
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        viewModel.onAction(MoviesListAction.OnToggleSortOrder)

        viewModel.onAction(MoviesListAction.OnSortFieldSelected(SortField.TITLE))

        assertThat(viewModel.state.value.sortOrder).isEqualTo(SortOrder.ASCENDING)
        assertThat(viewModel.state.value.movies.map { it.title }).isEqualTo(listOf("Alpha", "Zeta"))
        assertThat(viewModel.state.value.sortField).isEqualTo(SortField.TITLE)
    }

    @Test
    fun `OnToggleSortOrder flips between ascending and descending`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        assertThat(viewModel.state.value.sortOrder).isEqualTo(SortOrder.DESCENDING)

        viewModel.onAction(MoviesListAction.OnToggleSortOrder)
        assertThat(viewModel.state.value.sortOrder).isEqualTo(SortOrder.ASCENDING)

        viewModel.onAction(MoviesListAction.OnToggleSortOrder)
        assertThat(viewModel.state.value.sortOrder).isEqualTo(SortOrder.DESCENDING)
    }

    @Test
    fun `refresh failure with an empty cache surfaces an error`() = runTest {
        repository.refreshResult = Result.Error(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.hasError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `refresh failure with a warm cache keeps showing cached movies without an error`() = runTest {
        repository.emit(listOf(testMovie(id = 1)))
        repository.refreshResult = Result.Error(DataError.Network.NO_INTERNET)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        assertThat(viewModel.state.value.hasError).isFalse()
        assertThat(viewModel.state.value.movies).isNotEmpty()
    }

    @Test
    fun `OnRetryClick triggers another refresh call`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        assertThat(repository.refreshCallCount).isEqualTo(1)

        viewModel.onAction(MoviesListAction.OnRetryClick)

        assertThat(repository.refreshCallCount).isEqualTo(2)
    }

    @Test
    fun `OnMovieClick does not change state - navigation is handled by the Root composable`() = runTest {
        repository.emit(listOf(testMovie(id = 1)))
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
        val stateBefore = viewModel.state.value

        viewModel.onAction(MoviesListAction.OnMovieClick(movieId = 1))

        assertThat(viewModel.state.value).isEqualTo(stateBefore)
    }
}
