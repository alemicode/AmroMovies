package com.alemicode.amromovies.feature.movies.domain.di

import com.alemicode.amromovies.feature.movies.domain.usecase.GetMovieDetailUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.alemicode.amromovies.feature.movies.domain.usecase.RefreshTrendingMoviesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val moviesDomainModule = module {
    singleOf(::GetTrendingMoviesUseCase)
    singleOf(::RefreshTrendingMoviesUseCase)
    singleOf(::GetMovieDetailUseCase)
}
