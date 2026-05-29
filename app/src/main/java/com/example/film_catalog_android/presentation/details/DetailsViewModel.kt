package com.example.film_catalog_android.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.data.repository.WatchListRepositoryImpl
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieRepository: MovieRepository = MockMovieRepository()

    private val watchListRepository: WatchListRepository =
        WatchListRepositoryImpl(
            watchListDao = DatabaseProvider.getDatabase().watchListDao(),
            movieRepository = movieRepository
        )

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
                val watchListIds = watchListRepository.observeWatchListIds().first()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie?.copy(
                        isInWatchlist = movie.id in watchListIds
                    ),
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

        viewModelScope.launch {
            watchListRepository.toggleMovie(movie)
        }
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            watchListRepository.observeWatchListIds().collect { watchListIds ->
                val movie = _uiState.value.movie ?: return@collect

                _uiState.value = _uiState.value.copy(
                    movie = movie.copy(
                        isInWatchlist = movie.id in watchListIds
                    )
                )
            }
        }
    }
}