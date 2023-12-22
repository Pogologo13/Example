package com.app.skillcinema.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SharedPrefsRepository.PREFERENCE_NAME)
class SharedPrefsRepository @Inject constructor( applicationContext: Context) {

    private val dataStore = applicationContext.dataStore

    val isFirstTimeFlow: Flow<Int> = dataStore.data
        .map { preferences -> preferences[DATA_STORE_KEY] ?: 0 }

    suspend fun setValueForDataStore(value: Int) {
        dataStore.edit { settings ->
            settings[DATA_STORE_KEY] = value
        }
    }

    companion object {
        const val PREFERENCE_NAME = "Data of first entered"
        private const val SHARED_PREFS_KEY = "is done"
        val DATA_STORE_KEY = intPreferencesKey(SHARED_PREFS_KEY)
    }
}