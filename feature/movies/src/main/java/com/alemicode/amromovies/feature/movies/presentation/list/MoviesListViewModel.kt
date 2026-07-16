package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import com.alemicode.amromovies.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.RefreshTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.filteredByGenre
import com.alemicode.amromovies.feature.movies.domain.usecase.sortedByField
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class FilterAndSort(
    val genreId: Int?,
    val field: SortField,
    val order: SortOrder,
)

class MoviesListViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val refreshTrendingMovies: RefreshTrendingMoviesUseCase,
) : ViewModel() {

    private val selectedGenreId = MutableStateFlow<Int?>(null)
    private val sortField = MutableStateFlow(SortField.POPULARITY)
    private val sortOrder = MutableStateFlow(SortOrder.DESCENDING)
    private val isLoading = MutableStateFlow(true)
    private val hasError = MutableStateFlow(false)

    private val _state = MutableStateFlow(MoviesListState(isLoading = true))
    val state = _state.asStateFlow()

    private val _events = Channel<MoviesListEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeMovies()
        refresh()
    }

    fun onAction(action: MoviesListAction) {
        when (action) {
            is MoviesListAction.OnGenreSelected -> selectedGenreId.value = action.genreId
            is MoviesListAction.OnSortFieldSelected -> sortField.value = action.field
            MoviesListAction.OnToggleSortOrder -> sortOrder.update {
                if (it == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
            }
            is MoviesListAction.OnMovieClick -> viewModelScope.launch {
                _events.send(MoviesListEvent.NavigateToDetail(action.movieId))
            }
            MoviesListAction.OnRetryClick -> refresh()
        }
    }

    private fun observeMovies() {
        val filterAndSort = combine(selectedGenreId, sortField, sortOrder) { genreId, field, order ->
            FilterAndSort(genreId, field, order)
        }

        combine(
            getTrendingMovies(),
            filterAndSort,
            isLoading,
            hasError,
        ) { movies, filterAndSortValue, loading, error ->
            val selectedGenre = movies
                .flatMap { it.genres }
                .distinctBy { it.id }
                .firstOrNull { it.id == filterAndSortValue.genreId }

            val visibleMovies = movies
                .filteredByGenre(selectedGenre)
                .sortedByField(filterAndSortValue.field, filterAndSortValue.order)

            MoviesListState(
                movies = visibleMovies.map { it.toMovieUi() },
                genreFilters = movies.toGenreFilters(filterAndSortValue.genreId),
                sortField = filterAndSortValue.field,
                sortOrder = filterAndSortValue.order,
                isLoading = loading,
                hasError = error && movies.isEmpty(),
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            isLoading.value = true
            when (refreshTrendingMovies()) {
                is Result.Success -> hasError.value = false
                is Result.Error -> hasError.value = true
            }
            isLoading.value = false
        }
    }
}
