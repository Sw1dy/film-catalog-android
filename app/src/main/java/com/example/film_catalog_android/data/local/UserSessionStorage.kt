package com.example.film_catalog_android.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSessionStorage {
    //Пока true, чтобы были видны админские инструменты

    private val _isAdmin = MutableStateFlow(true)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    fun setAdmin(value: Boolean) {
        _isAdmin.value = value
    }

    fun logout() {
        _isAdmin.value = false
    }
}