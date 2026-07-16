package com.alemicode.amromovies

import android.app.Application
import com.alemicode.amromovies.core.common.di.commonModule
import com.alemicode.amromovies.core.network.di.networkModule
import com.alemicode.amromovies.feature.movies.data.di.moviesDataModule
import com.alemicode.amromovies.feature.movies.domain.di.moviesDomainModule
import com.alemicode.amromovies.feature.movies.presentation.di.moviesPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AmroMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AmroMoviesApplication)
            modules(
                commonModule,
                networkModule,
                moviesDataModule,
                moviesDomainModule,
                moviesPresentationModule,
            )
        }
    }
}
