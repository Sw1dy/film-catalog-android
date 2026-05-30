package com.example.film_catalog_android.presentation.auth

sealed interface AuthStartState {
    data object Loading : AuthStartState
    data object Authorized : AuthStartState
    data object Unauthorized : AuthStartState
}
