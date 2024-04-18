package com.example.gym.new_session

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
internal interface NewSessionMiddlewares {

    @Binds
    @IntoSet
    fun provideNewSessionMiddleware(impl: NewSessionMiddleware): Middleware<NewSessionState>

    @Module
    @InstallIn(ViewModelComponent::class)
    object NewSessionModule {
        @Provides
        @ViewModelScoped
        fun provideNewSessionStore(
            middlewares: Set<@JvmSuppressWildcards Middleware<NewSessionState>>,
            commonMiddlewares: Set<@JvmSuppressWildcards Middleware<MviState>>
        ): MviStore<NewSessionState> = MviStore(
            initialState = NewSessionState(),
            reducer = NewSessionReducer(),
            middlewares = middlewares,
            commonMiddlewares = commonMiddlewares
        )
    }
}