package com.example.film_catalog_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.film_catalog_android.core.navigation.AppNavigation
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.ui.theme.FilmcatalogandroidTheme
import com.example.film_catalog_android.data.local.ThemeStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeStorage.init(applicationContext)
        DatabaseProvider.init(applicationContext)

        setContent {
            val isDarkTheme by ThemeStorage.isDarkTheme.collectAsState()

            FilmcatalogandroidTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavigation()
            }
        }
    }
}