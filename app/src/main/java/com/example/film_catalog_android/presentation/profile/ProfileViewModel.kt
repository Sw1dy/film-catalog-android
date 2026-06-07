package com.example.film_catalog_android.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.repository.RepositoryProvider
import com.example.film_catalog_android.domain.repository.WatchListRepository
import com.example.film_catalog_android.domain.usecase.watchlist.ObserveWatchListUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.RemoveFromWatchListUseCase
import com.example.film_catalog_android.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.filter

class ProfileViewModel : ViewModel() {

    private val watchListRepository: WatchListRepository =
        RepositoryProvider.watchListRepository
    private val observeWatchListUseCase = ObserveWatchListUseCase(watchListRepository)
    private val removeFromWatchListUseCase = RemoveFromWatchListUseCase(watchListRepository)

    private var allMovies: List<Movie> = emptyList()
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeUser()
        observeWatchList()
    }

    fun removeFromWatchList(movieId: Long) {
        viewModelScope.launch {
            removeFromWatchListUseCase(movieId)
        }
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            observeWatchListUseCase().collect { movies ->
                allMovies = movies.map { movie ->
                    movie.copy(isInWatchlist = true)
                }

                updateFilteredMovies()
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            UserSessionStorage.firstName.collect { firstName ->
                _uiState.value = _uiState.value.copy(firstName = firstName)
            }
        }

        viewModelScope.launch {
            UserSessionStorage.lastName.collect { lastName ->
                _uiState.value = _uiState.value.copy(lastName = lastName)
            }
        }
    }

    fun selectGenre(genre: String?) {
        _uiState.value = _uiState.value.copy(
            selectedGenre = genre
        )
        updateFilteredMovies()
    }

    fun selectYear(year: Int?) {
        _uiState.value = _uiState.value.copy(
            selectedYear = year
        )
        updateFilteredMovies()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedGenre = null,
            selectedYear = null
        )
        updateFilteredMovies()
    }

    private fun updateFilteredMovies() {
        val state = _uiState.value

        val filteredMovies = allMovies.filter { movie ->
            val genreMatches = state.selectedGenre == null || movie.genre == state.selectedGenre
            val yearMatches = state.selectedYear == null || movie.year == state.selectedYear

            genreMatches && yearMatches
        }

        _uiState.value = state.copy(
            watchList = filteredMovies,
            availableGenres = allMovies
                .map { it.genre }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted(),
            availableYears = allMovies
                .map { it.year }
                .distinct()
                .sortedDescending()
        )
    }
}
