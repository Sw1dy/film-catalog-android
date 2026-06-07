package com.example.film_catalog_android.domain.usecase.auth

import com.example.film_catalog_android.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) {
        authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}
