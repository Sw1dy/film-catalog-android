package com.example.film_catalog_android.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole
) {
    fun isAdmin(): Boolean = role == UserRole.ADMIN
}

// Для User используется id типа String
// (Совместимость с разными провайдерами: Firebase поддерживает вход через Email/Password, Google, Apple, телефон и анонимно.
// Каждая платформа возвращает уникальный ID в своем формате (число или строка). String позволяет избежать конфликтов.)