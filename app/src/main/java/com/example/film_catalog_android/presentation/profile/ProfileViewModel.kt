package com.example.film_catalog_android.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.WatchListStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeWatchList()
    }

    fun removeFromWatchList(movieId: Long) {
        WatchListStorage.removeMovie(movieId)
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            WatchListStorage.movies.collect { movies ->
                _uiState.value = _uiState.value.copy(
                    watchList = movies
                )
            }
        }
    }
}