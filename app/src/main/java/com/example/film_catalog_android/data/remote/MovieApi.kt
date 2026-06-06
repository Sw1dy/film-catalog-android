package com.example.film_catalog_android.data.remote

import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.remote.dto.CreateMovieRequest
import com.example.film_catalog_android.data.remote.dto.MovieDto
import com.example.film_catalog_android.data.remote.dto.UpdateMovieRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.flow.first

class MovieApi {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }

        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun getMovies(
        genre: String? = null,
        year: Int? = null
    ): List<MovieDto> {
        return client
            .get("${NetworkConfig.BASE_URL}/movies") {
                url {
                    if (!genre.isNullOrBlank()) {
                        parameters.append("genre", genre)
                    }

                    if (year != null) {
                        parameters.append("year", year.toString())
                    }
                }
            }
            .body()
    }

    suspend fun getGenres(): List<String> {
        return client
            .get("${NetworkConfig.BASE_URL}/movies/genres")
            .body<List<String>>()
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    suspend fun getYears(): List<Int> {
        return client
            .get("${NetworkConfig.BASE_URL}/movies/years")
            .body<List<Int>>()
            .distinct()
            .sortedDescending()
    }
    suspend fun getMovieById(id: Long): MovieDto {
        return client
            .get("${NetworkConfig.BASE_URL}/movies/$id")
            .body()
    }

    suspend fun searchMovies(query: String): List<MovieDto> {
        return client
            .get("${NetworkConfig.BASE_URL}/movies/search") {
                url {
                    parameters.append("query", query)
                }
            }
            .body()
    }

    suspend fun addMovie(request: CreateMovieRequest): MovieDto {
        val token = requireAdminToken()

        return client
            .post("${NetworkConfig.BASE_URL}/movies") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .body()
    }

    suspend fun updateMovie(id: Long, request: UpdateMovieRequest): MovieDto {
        val token = requireAdminToken()

        return client
            .put("${NetworkConfig.BASE_URL}/movies/$id") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .body()
    }

    suspend fun deleteMovie(id: Long) {
        val token = requireAdminToken()

        client.delete("${NetworkConfig.BASE_URL}/movies/$id") {
            bearerAuth(token)
        }
    }

    private suspend fun requireAdminToken(): String {
        val token = UserSessionStorage.token.first()

        if (token.isNullOrBlank() || !UserSessionStorage.isAdmin.first()) {
            throw IllegalStateException("Недостаточно прав для управления фильмами")
        }

        return token
    }
}
