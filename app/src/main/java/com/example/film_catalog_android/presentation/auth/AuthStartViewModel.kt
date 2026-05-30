package com.example.film_catalog_android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.UserSessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthStartViewModel : ViewModel() {

    private val _authStartState = MutableStateFlow<AuthStartState>(
        AuthStartState.Loading
    )
    val authStartState: StateFlow<AuthStartState> = _authStartState.asStateFlow()

    init {
        viewModelScope.launch {
            UserSessionStorage.isAuthorized.collect { isAuthorized ->
                _authStartState.value = if (isAuthorized) {
                    AuthStartState.Authorized
                } else {
                    AuthStartState.Unauthorized
                }
            }
        }
    }
}
