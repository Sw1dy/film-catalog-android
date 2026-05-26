package com.example.film_catalog_android.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.ThemeStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeTheme()
    }

    fun onThemeChanged(isDarkTheme: Boolean) {
        ThemeStorage.setDarkTheme(isDarkTheme)
    }

    fun logout() {
        // Позже подключим выход из Firebase auth
    }

    private fun observeTheme() {
        viewModelScope.launch {
            ThemeStorage.isDarkTheme.collect { isDark ->
                _uiState.value = _uiState.value.copy(
                    isDarkTheme = isDark
                )
            }
        }
    }
}