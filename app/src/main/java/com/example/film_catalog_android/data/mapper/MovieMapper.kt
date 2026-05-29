package com.example.film_catalog_android.data.mapper

import com.example.film_catalog_android.data.remote.dto.CreateMovieRequest
import com.example.film_catalog_android.data.remote.dto.MovieDto
import com.example.film_catalog_android.data.remote.dto.UpdateMovieRequest
import com.example.film_catalog_android.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        description = description,
        year = year,
        genre = genre,
        rating = rating,
        imageUrl = imageUrl
    )
}

fun Movie.toCreateRequest(): CreateMovieRequest {
    return CreateMovieRequest(
        title = title,
        description = description,
        year = year,
        genre = genre,
        rating = rating,
        imageUrl = imageUrl
    )
}

fun Movie.toUpdateRequest(): UpdateMovieRequest {
    return UpdateMovieRequest(
        title = title,
        description = description,
        year = year,
        genre = genre,
        rating = rating,
        imageUrl = imageUrl
    )
}