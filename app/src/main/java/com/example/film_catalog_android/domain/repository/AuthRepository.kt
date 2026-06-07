package com.example.film_catalog_android.domain.repository

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    )

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    )

    suspend fun logout()
}
