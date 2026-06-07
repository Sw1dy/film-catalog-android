package com.example.film_catalog_android.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.usecase.movie.AddMovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddMovieViewModel(
    private val addMovieUseCase: AddMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieFormUiState())
    val uiState: StateFlow<MovieFormUiState> = _uiState.asStateFlow()

    fun onImageUrlChange(value: String) {
        _uiState.value = _uiState.value.copy(imageUrl = value, errorMessage = null)
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value, errorMessage = null)
    }

    fun onYearChange(value: String) {
        _uiState.value = _uiState.value.copy(year = value, errorMessage = null)
    }

    fun onGenreChange(value: String) {
        _uiState.value = _uiState.value.copy(genre = value, errorMessage = null)
    }

    fun onRatingChange(value: String) {
        _uiState.value = _uiState.value.copy(rating = value, errorMessage = null)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value, errorMessage = null)
    }

    fun addMovie(onSuccess: () -> Unit) {
        val state = _uiState.value

        val year = state.year.toIntOrNull()
        val rating = state.rating.replace(',', '.').toDoubleOrNull()

        when {
            state.title.isBlank() -> {
                showError("Введите название фильма")
                return
            }

            year == null || year < 1888 -> {
                showError("Введите корректный год")
                return
            }

            state.genre.isBlank() -> {
                showError("Введите жанр")
                return
            }

            rating == null || rating !in 0.0..10.0 -> {
                showError("Рейтинг должен быть от 0 до 10")
                return
            }

            state.description.isBlank() -> {
                showError("Введите описание")
                return
            }
        }

        val movie = Movie(
            id = 0,
            title = state.title.trim(),
            description = state.description.trim(),
            year = year,
            genre = state.genre.trim(),
            rating = rating,
            imageUrl = state.imageUrl.trim()
        )

        if (!movie.isValid()) {
            showError("Проверьте корректность данных фильма")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                addMovieUseCase(movie)
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось добавить фильм"
                )
            }
        }
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }
}
