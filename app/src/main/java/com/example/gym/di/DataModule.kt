package com.example.gym.di

import android.content.Context
import com.example.gym.room.GymDatabase
import com.example.gym.room.dao.ExercisesDao
import com.example.gym.datastore.DataStoreManager
import com.example.gym.room.dao.SessionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    // Authentication

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)

    // Web Service

    @Provides
    fun provideDatabase(context: Context): GymDatabase = GymDatabase.getInstance(context)

    @Provides
    fun provideExercisesDao(context: Context): ExercisesDao = GymDatabase.getInstance(context).exercisesDao()

    @Provides
    fun provideSessionsDao(context: Context): SessionsDao = GymDatabase.getInstance(context).sessionsDao()

}
