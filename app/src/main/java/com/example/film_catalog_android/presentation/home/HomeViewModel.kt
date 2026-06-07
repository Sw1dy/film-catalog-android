package com.example.film_catalog_android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.repository.RepositoryProvider
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import com.example.film_catalog_android.domain.usecase.movie.GetMovieGenresUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMovieYearsUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMoviesUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ObserveWatchListIdsUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ToggleWatchListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository = RepositoryProvider.movieRepository
) : ViewModel() {

    private val getMoviesUseCase = GetMoviesUseCase(movieRepository)
    private val getMovieGenresUseCase = GetMovieGenresUseCase(movieRepository)
    private val getMovieYearsUseCase = GetMovieYearsUseCase(movieRepository)
    private val watchListRepository: WatchListRepository =
        RepositoryProvider.watchListRepository
    private val observeWatchListIdsUseCase = ObserveWatchListIdsUseCase(watchListRepository)
    private val toggleWatchListUseCase = ToggleWatchListUseCase(watchListRepository)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeMovies()
        observeUserRole()
        loadMovies()
        loadFilterOptions()
    }

    fun loadMovies() {
        viewModelScope.launch {
            val currentState = _uiState.value

            _uiState.value = currentState.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                getMoviesUseCase(
                    genre = currentState.selectedGenre,
                    year = currentState.selectedYear
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить фильмы"
                )
            }
        }
    }

    fun loadFilterOptions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isFiltersLoading = true,
                filterErrorMessage = null
            )

            try {
                val genres = getMovieGenresUseCase()
                val years = getMovieYearsUseCase()

                _uiState.value = _uiState.value.copy(
                    availableGenres = genres,
                    availableYears = years,
                    isFiltersLoading = false,
                    filterErrorMessage = null
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isFiltersLoading = false,
                    filterErrorMessage = "Не удалось загрузить фильтры"
                )
            }
        }
    }

    fun selectGenre(genre: String?) {
        _uiState.value = _uiState.value.copy(
            selectedGenre = genre
        )

        loadMovies()
    }

    fun selectYear(year: Int?) {
        _uiState.value = _uiState.value.copy(
            selectedYear = year
        )

        loadMovies()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedGenre = null,
            selectedYear = null
        )

        loadMovies()
    }

    fun toggleWatchList(movie: Movie) {
        viewModelScope.launch {
            toggleWatchListUseCase(movie)
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            combine(
                movieRepository.observeMovies(),
                observeWatchListIdsUseCase()
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
