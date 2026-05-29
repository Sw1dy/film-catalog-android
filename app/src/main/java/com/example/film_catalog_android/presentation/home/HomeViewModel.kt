package com.example.film_catalog_android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.local.WatchListStorage
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository = MockMovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
        observeWatchList()
        observeUserRole()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val movies = movieRepository.getMovies()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = movies.updateWatchListState()
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить фильмы"
                )
            }
        }
    }

    fun toggleWatchList(movie: Movie) {
        WatchListStorage.toggleMovie(movie)
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            WatchListStorage.movies.collect {
                _uiState.value = _uiState.value.copy(
                    movies = _uiState.value.movies.updateWatchListState()
                )
            }
        }
    }

    private fun observeUserRole() {
        viewModelScope.launch {
            UserSessionStorage.isAdmin.collect { isAdmin ->
                _uiState.value = _uiState.value.copy(
                    isAdmin = isAdmin
                )
            }
        }
    }

    private fun List<Movie>.updateWatchListState(): List<Movie> {
        return map { movie ->
            movie.copy(
                isInWatchlist = WatchListStorage.isMovieInWatchList(movie.id)
            )
        }
    }
}