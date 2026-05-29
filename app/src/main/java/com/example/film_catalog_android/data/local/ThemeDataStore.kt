package com.example.film_catalog_android.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore(name = "theme_settings")

class ThemeDataStore(
    private val context: Context
) {
    private val darkThemeKey = booleanPreferencesKey("dark_theme")

    val isDarkTheme: Flow<Boolean> = context.themeDataStore.data.map { preferences ->
        preferences[darkThemeKey] ?: false
    }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[darkThemeKey] = isDarkTheme
        }
    }
}