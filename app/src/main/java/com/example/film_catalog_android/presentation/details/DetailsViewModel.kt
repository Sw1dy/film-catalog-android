package com.example.film_catalog_android.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.WatchListStorage
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieRepository: MovieRepository = MockMovieRepository()

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private var currentMovieId: Long? = null

    init {
        val movieId = savedStateHandle.get<String>("movieId")?.toLongOrNull()
        currentMovieId = movieId

        if (movieId == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Фильм не найден"
            )
        } else {
            loadMovie(movieId)
            observeWatchList()
        }
    }

    fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val movie = movieRepository.getMovieById(movieId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie?.withWatchListState(),
                    errorMessage = if (movie == null) "Фильм не найден" else null
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить данные"
                )
            }
        }
    }

    fun toggleWatchList() {
        val movie = _uiState.value.movie ?: return
        WatchListStorage.toggleMovie(movie)
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            WatchListStorage.movies.collect {
                val currentMovie = _uiState.value.movie

                if (currentMovie != null) {
                    _uiState.value = _uiState.value.copy(
                        movie = currentMovie.withWatchListState()
                    )
                }
            }
        }
    }

    private fun Movie.withWatchListState(): Movie {
        return copy(
            isInWatchlist = WatchListStorage.isMovieInWatchList(id)
        )
    }
}