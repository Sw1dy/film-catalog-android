package com.example.film_catalog_android.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.repository.RemoteMovieRepository
import com.example.film_catalog_android.data.repository.RepositoryProvider
import com.example.film_catalog_android.data.repository.WatchListRepositoryImpl
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import com.example.film_catalog_android.domain.usecase.movie.GetMovieByIdUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ObserveWatchListIdsUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ToggleWatchListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieRepository: MovieRepository = RepositoryProvider.movieRepository

    //Временно так
    private val watchListRepository: WatchListRepository =
        WatchListRepositoryImpl(
            watchListDao = DatabaseProvider.getDatabase().watchListDao(),
            movieRepository = movieRepository
        )

    private val getMovieByIdUseCase = GetMovieByIdUseCase(movieRepository)
    private val observeWatchListIdsUseCase = ObserveWatchListIdsUseCase(watchListRepository)
    private val toggleWatchListUseCase = ToggleWatchListUseCase(watchListRepository)

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
                val movie = getMovieByIdUseCase(movieId)
                val watchListIds = observeWatchListIdsUseCase().first()

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
            toggleWatchListUseCase(movie)
        }
    }

    private fun observeWatchList() {
        viewModelScope.launch {
            observeWatchListIdsUseCase().collect { watchListIds ->
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