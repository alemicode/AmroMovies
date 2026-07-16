package com.alemicode.amromovies.feature.movies.data.di

import android.content.Context
import androidx.room.Room
import com.alemicode.amromovies.feature.movies.data.MoviesRepositoryImpl
import com.alemicode.amromovies.feature.movies.data.local.LocalMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.local.MoviesDatabase
import com.alemicode.amromovies.feature.movies.data.remote.RemoteMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.remote.TmdbApiService
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "movies.db"

val moviesDataModule = module {
    single {
        Room.databaseBuilder(get<Context>(), MoviesDatabase::class.java, DATABASE_NAME).build()
    }
    single { get<MoviesDatabase>().moviesDao() }
    singleOf(::LocalMoviesDataSource)

    single { get<Retrofit>().create(TmdbApiService::class.java) }
    singleOf(::RemoteMoviesDataSource)

    singleOf(::MoviesRepositoryImpl).bind<MoviesRepository>()
}
