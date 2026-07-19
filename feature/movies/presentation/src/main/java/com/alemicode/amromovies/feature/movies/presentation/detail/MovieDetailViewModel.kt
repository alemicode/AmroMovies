package com.alemicode.amromovies.feature.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.core.common.UiState
import com.alemicode.amromovies.feature.movies.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetail: GetMovieDetailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<MovieDetailUi>>(UiState.Loading)
    val state = _state
        .onStart { loadMovieDetail() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value,
        )

    fun onAction(action: MovieDetailAction) {
        when (action) {
            MovieDetailAction.OnRetryClick -> loadMovieDetail()
        }
    }

    private fun loadMovieDetail() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when (val result = getMovieDetail(movieId)) {
                is Result.Success -> _state.value = UiState.Success(result.data.toMovieDetailUi())
                is Result.Error -> _state.value = UiState.Failure(result.error)
            }
        }
    }
}
