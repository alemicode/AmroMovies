package com.alemicode.amromovies.feature.movies.presentation.di

import com.alemicode.amromovies.feature.movies.presentation.detail.MovieDetailViewModel
import com.alemicode.amromovies.feature.movies.presentation.list.MoviesListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModelOf(::MoviesListViewModel)
    viewModel { (movieId: Int) -> MovieDetailViewModel(movieId, get()) }
}
