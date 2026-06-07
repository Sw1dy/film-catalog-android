package com.example.film_catalog_android.domain.usecase.auth

import com.example.film_catalog_android.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        authRepository.register(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            password = password
        )
    }
}
