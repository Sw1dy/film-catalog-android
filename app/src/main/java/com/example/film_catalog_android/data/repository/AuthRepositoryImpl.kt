package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.remote.AuthApi
import com.example.film_catalog_android.data.remote.dto.LoginRequest
import com.example.film_catalog_android.data.remote.dto.RegisterRequest
import com.example.film_catalog_android.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi = AuthApi()
) : AuthRepository {

    override suspend fun login(email: String, password: String) {
        val response = authApi.login(
            LoginRequest(
                email = email,
                password = password
            )
        )

        UserSessionStorage.saveSession(
            token = response.token,
            userId = response.user.id,
            firstName = response.user.firstName,
            lastName = response.user.lastName,
            email = response.user.email,
            role = response.user.role
        )
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        val response = authApi.register(
            RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
        )

        UserSessionStorage.saveSession(
            token = response.token,
            userId = response.user.id,
            firstName = response.user.firstName,
            lastName = response.user.lastName,
            email = response.user.email,
            role = response.user.role
        )
    }

    override suspend fun logout() {
        UserSessionStorage.clearSession()
    }
}
