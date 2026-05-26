package com.example.film_catalog_android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value,
            errorMessage = null
        )
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            lastName = value,
            errorMessage = null
        )
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            errorMessage = null
        )
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            errorMessage = null
        )
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (!isEmailValid(state.email)) {
            _uiState.value = state.copy(
                errorMessage = "Введите корректную электронную почту"
            )
            return
        }

        if (state.password.length < 6) {
            _uiState.value = state.copy(
                errorMessage = "Пароль должен содержать минимум 6 символов"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            delay(500)

            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.firstName.isBlank()) {
            _uiState.value = state.copy(
                errorMessage = "Введите имя"
            )
            return
        }

        if (state.lastName.isBlank()) {
            _uiState.value = state.copy(
                errorMessage = "Введите фамилию"
            )
            return
        }

        if (!isEmailValid(state.email)) {
            _uiState.value = state.copy(
                errorMessage = "Введите корректную электронную почту"
            )
            return
        }

        if (state.password.length < 6) {
            _uiState.value = state.copy(
                errorMessage = "Пароль должен содержать минимум 6 символов"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            delay(500)

            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}