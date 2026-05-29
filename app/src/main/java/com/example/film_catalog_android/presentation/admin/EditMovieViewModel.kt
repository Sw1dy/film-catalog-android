package com.example.film_catalog_android.presentation.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditMovieViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieRepository: MovieRepository = MockMovieRepository()

    private val _uiState = MutableStateFlow(MovieFormUiState())
    val uiState: StateFlow<MovieFormUiState> = _uiState.asStateFlow()

    private var movieId: Long? = null

    init {
        val id = savedStateHandle.get<String>("movieId")?.toLongOrNull()
        movieId = id

        if (id == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Фильм не найден"
            )
        } else {
            loadMovie(id)
        }
    }

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

    fun saveMovie(onSuccess: () -> Unit) {
        val id = movieId ?: run {
            showError("Фильм не найден")
            return
        }

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
            id = id,
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
                movieRepository.updateMovie(movie)

                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось сохранить изменения"
                )
            }
        }
    }

    private fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val movie = movieRepository.getMovieById(movieId)

                if (movie == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Фильм не найден"
                    )
                } else {
                    _uiState.value = MovieFormUiState(
                        imageUrl = movie.imageUrl,
                        title = movie.title,
                        year = movie.year.toString(),
                        genre = movie.genre,
                        rating = movie.rating.toString(),
                        description = movie.description,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить фильм"
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