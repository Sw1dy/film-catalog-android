package com.example.film_catalog_android.domain.usecase.auth

import com.example.film_catalog_android.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
