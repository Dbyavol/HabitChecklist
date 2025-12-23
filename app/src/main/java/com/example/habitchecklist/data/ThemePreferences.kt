package com.example.habitchecklist.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object ThemePreferences {

    private val DARK_THEME = booleanPreferencesKey("dark_theme")

    fun isDark(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[DARK_THEME] ?: false
        }

    suspend fun setDark(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME] = enabled
        }
    }
}
