package com.example.gym.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gym.datastore.DataStoreManager.PreferencesKeys.ENVIRONMENT_KEY
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val ENVIRONMENT_KEY = stringPreferencesKey("environment")
    }


//    val environment: Flow<String> = context.dataStore.data.map { preferences ->
//        preferences[ENVIRONMENT_KEY] ?: when (BuildConfig.BUILD_TYPE) {
//            BUILD_TYPE_RELEASE -> Environment.ENV_PROD
//            BUILD_TYPE_DEBUG -> Environment.ENV_DEV
//            else -> Environment.ENV_DEV
//        }
//    }

    suspend fun setEnvironment(environment: String) = context.dataStore.edit { preferences ->
        preferences[ENVIRONMENT_KEY] = environment
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
