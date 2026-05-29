package com.example.film_catalog_android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.data.repository.WatchListRepositoryImpl
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository = MockMovieRepository()
) : ViewModel() {

    private val watchListRepository: WatchListRepository =
        WatchListRepositoryImpl(
            watchListDao = DatabaseProvider.getDatabase().watchListDao(),
            movieRepository = movieRepository
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeMovies()
        observeUserRole()
    }

    fun loadMovies() {
        observeMovies()
    }

    fun toggleWatchList(movie: Movie) {
        viewModelScope.launch {
            watchListRepository.toggleMovie(movie)
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                combine(
                    movieRepository.observeMovies(),
                    watchListRepository.observeWatchListIds()
                ) { movies, watchListIds ->
                    movies.map { movie ->
                        movie.copy(
                            isInWatchlist = movie.id in watchListIds
                        )
                    }
                }.collect { movies ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movies = movies,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить фильмы"
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
}