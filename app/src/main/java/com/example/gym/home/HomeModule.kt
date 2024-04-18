package com.example.gym.home

import com.example.gym.mvi.CommonMiddleware
import com.example.gym.mvi.Middleware
import com.example.gym.mvi.MviState
import com.example.gym.mvi.MviStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
internal interface HomeMiddlewares {

    @Binds
    @IntoSet
    fun provideHomeMiddleware(impl: HomeMiddleware): Middleware<HomeState>

    @Binds
    @IntoSet
    fun provideCommonMiddleware(impl: CommonMiddleware): Middleware<MviState>

    @Module
    @InstallIn(ViewModelComponent::class)
    object HomeModule {
        @Provides
        @ViewModelScoped
        fun provideHomeStore(
            middlewares: Set<@JvmSuppressWildcards Middleware<HomeState>>,
            commonMiddlewares: Set<@JvmSuppressWildcards Middleware<MviState>>
        ): MviStore<HomeState> = MviStore(
            initialState = HomeState(),
            reducer = HomeReducer(),
            middlewares = middlewares,
            commonMiddlewares = commonMiddlewares
        )
    }
}