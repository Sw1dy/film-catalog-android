package com.example.film_catalog_android.data.remote

import com.example.film_catalog_android.data.remote.dto.AuthResponse
import com.example.film_catalog_android.data.remote.dto.LoginRequest
import com.example.film_catalog_android.data.remote.dto.RegisterRequest
import com.example.film_catalog_android.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

class AuthApi {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }

        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        return client
            .post("${NetworkConfig.BASE_URL}/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .body()
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return client
            .post("${NetworkConfig.BASE_URL}/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .body()
    }

    suspend fun me(token: String): UserDto {
        return client
            .get("${NetworkConfig.BASE_URL}/auth/me") {
                bearerAuth(token)
            }
            .body()
    }
}
