package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.delay

class MockMovieRepository : MovieRepository {

    private val movies = listOf(
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

    override suspend fun getMovies(): List<Movie> {
        delay(500)
        return movies
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        delay(700)

        return movies.filter { movie ->
            movie.title.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getMovieById(id: Long): Movie? {
        delay(300)
        return movies.find { it.id == id }
    }
}