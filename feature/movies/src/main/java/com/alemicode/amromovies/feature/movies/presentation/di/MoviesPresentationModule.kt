package com.alemicode.amromovies.feature.movies.presentation.di

import com.alemicode.amromovies.feature.movies.presentation.list.MoviesListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModelOf(::MoviesListViewModel)
}
