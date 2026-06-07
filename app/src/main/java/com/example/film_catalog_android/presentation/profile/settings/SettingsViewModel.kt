package com.example.film_catalog_android.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.ThemeStorage
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeTheme()
        observeEmail()
    }

    fun onThemeChanged(isDarkTheme: Boolean) {
        ThemeStorage.setDarkTheme(isDarkTheme)
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onSuccess()
        }
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

    private fun observeEmail() {
        viewModelScope.launch {
            UserSessionStorage.email.collect { email ->
                _uiState.value = _uiState.value.copy(
                    email = email
                )
            }
        }
    }
}
