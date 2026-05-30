package com.example.film_catalog_android.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.repository.RepositoryProvider
import com.example.film_catalog_android.data.repository.WatchListRepositoryImpl
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val movieRepository: MovieRepository = RepositoryProvider.movieRepository

    private val watchListRepository: WatchListRepository =
        WatchListRepositoryImpl(
            watchListDao = DatabaseProvider.getDatabase().watchListDao(),
            movieRepository = movieRepository
        )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeUser()
        observeWatchList()
    }

    fun removeFromWatchList(movieId: Long) {
        viewModelScope.launch {
            watchListRepository.removeMovie(movieId)
        }
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            watchListRepository.observeWatchList().collect { movies ->
                _uiState.value = _uiState.value.copy(
                    watchList = movies.map { movie ->
                        movie.copy(isInWatchlist = true)
                    }
                )
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
}
