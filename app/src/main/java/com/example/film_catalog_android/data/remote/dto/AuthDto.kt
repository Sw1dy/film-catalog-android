package com.example.film_catalog_android.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class UserDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)
