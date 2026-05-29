package com.example.film_catalog_android.data.local

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object ThemeStorage {

    private lateinit var themeDataStore: ThemeDataStore

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var isDarkTheme: StateFlow<Boolean>
        private set

    fun init(context: Context) {
        themeDataStore = ThemeDataStore(context.applicationContext)

        isDarkTheme = themeDataStore.isDarkTheme.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
    }

    fun setDarkTheme(isDark: Boolean) {
        scope.launch {
            themeDataStore.setDarkTheme(isDark)
        }
    }
}