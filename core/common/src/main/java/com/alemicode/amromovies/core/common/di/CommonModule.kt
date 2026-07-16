package com.alemicode.amromovies.core.common.di

import com.alemicode.amromovies.core.common.DefaultDispatcherProvider
import com.alemicode.amromovies.core.common.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    singleOf(::DefaultDispatcherProvider).bind<DispatcherProvider>()
}
