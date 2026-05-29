package com.example.film_catalog_android.data.local

import com.example.film_catalog_android.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MovieStorage {

    private val initialMovies = listOf(
        Movie(
            id = 1,
            title = "Грязные деньги",
            description = "История о мире финансов, рисков и сложных моральных решений.",
            year = 2018,
            genre = "Драма",
            rating = 6.2,
            imageUrl = ""
        ),
        Movie(
            id = 2,
            title = "Детство Шелдона",
            description = "История о непростом детстве вундеркинда Шелдона Купера.",
            year = 2017,
            genre = "Комедия",
            rating = 7.3,
            imageUrl = ""
        ),
        Movie(
            id = 3,
            title = "Джентльмены",
            description = "Криминальная комедия о столкновении интересов в преступном мире.",
            year = 2019,
            genre = "Криминал",
            rating = 8.2,
            imageUrl = ""
        ),
        Movie(
            id = 4,
            title = "Хвост Феи",
            description = "Фэнтезийная история о магии, дружбе и приключениях.",
            year = 2009,
            genre = "Аниме",
            rating = 7.9,
            imageUrl = ""
        )
    )

    private val _movies = MutableStateFlow(initialMovies)
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    fun getMovies(): List<Movie> {
        return _movies.value
    }

    fun searchMovies(query: String): List<Movie> {
        return _movies.value.filter { movie ->
            movie.title.contains(query, ignoreCase = true)
        }
    }

    fun getMovieById(id: Long): Movie? {
        return _movies.value.find { movie ->
            movie.id == id
        }
    }

    fun addMovie(movie: Movie) {
        val nextId = ((_movies.value.maxOfOrNull { it.id } ?: 0L) + 1L)

        _movies.value = listOf(
            movie.copy(id = nextId)
        ) + _movies.value
    }

    fun updateMovie(movie: Movie) {
        _movies.value = _movies.value.map { currentMovie ->
            if (currentMovie.id == movie.id) {
                movie
            } else {
                currentMovie
            }
        }
    }

    fun deleteMovie(movieId: Long) {
        _movies.value = _movies.value.filterNot { movie ->
            movie.id == movieId
        }
    }
}