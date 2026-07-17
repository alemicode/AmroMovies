package com.alemicode.amromovies.feature.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetail: GetMovieDetailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state
        .onStart { loadMovieDetail() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), _state.value)

    fun onAction(action: MovieDetailAction) {
        when (action) {
            MovieDetailAction.OnRetryClick -> loadMovieDetail()
        }
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }
            when (val result = getMovieDetail(movieId)) {
                is Result.Success -> _state.update {
                    it.copy(movie = result.data.toMovieDetailUi(), isLoading = false, hasError = false)
                }
                is Result.Error -> _state.update {
                    it.copy(isLoading = false, hasError = true)
                }
            }
        }
    }
}
