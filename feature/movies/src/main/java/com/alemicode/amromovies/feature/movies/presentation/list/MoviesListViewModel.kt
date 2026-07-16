package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import com.alemicode.amromovies.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.RefreshTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.filteredByGenre
import com.alemicode.amromovies.feature.movies.domain.usecase.sortedByField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val refreshTrendingMovies: RefreshTrendingMoviesUseCase,
) : ViewModel() {

    private var cachedMovies: List<Movie> = emptyList()

    private val _state = MutableStateFlow(MoviesListState())
    val state = _state
        .onStart {
            observeMovies()
            refresh()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), _state.value)

    fun onAction(action: MoviesListAction) {
        when (action) {
            is MoviesListAction.OnGenreSelected -> {
                _state.update { it.copy(selectedGenreId = action.genreId) }
                applyFilterAndSort()
            }
            is MoviesListAction.OnSortFieldSelected -> {
                _state.update { it.copy(sortField = action.field) }
                applyFilterAndSort()
            }
            MoviesListAction.OnToggleSortOrder -> {
                _state.update {
                    val nextOrder = if (it.sortOrder == SortOrder.ASCENDING) {
                        SortOrder.DESCENDING
                    } else {
                        SortOrder.ASCENDING
                    }
                    it.copy(sortOrder = nextOrder)
                }
                applyFilterAndSort()
            }
            MoviesListAction.OnRetryClick -> refresh()
            is MoviesListAction.OnMovieClick -> Unit
        }
    }

    private fun observeMovies() {
        getTrendingMovies()
            .onEach { movies ->
                cachedMovies = movies
                applyFilterAndSort()
            }
            .launchIn(viewModelScope)
    }

    private fun applyFilterAndSort() {
        _state.update { current ->
            current.copy(
                movies = cachedMovies
                    .filteredByGenre(current.selectedGenreId)
                    .sortedByField(current.sortField, current.sortOrder)
                    .map { it.toMovieUi() },
                genreFilters = cachedMovies.toGenreFilters(current.selectedGenreId),
            )
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (refreshTrendingMovies()) {
                is Result.Success -> _state.update { it.copy(isLoading = false, hasError = false) }
                is Result.Error -> _state.update { it.copy(isLoading = false, hasError = cachedMovies.isEmpty()) }
            }
        }
    }
}
