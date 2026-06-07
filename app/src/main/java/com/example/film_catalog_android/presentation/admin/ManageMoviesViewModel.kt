package com.example.film_catalog_android.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.repository.RepositoryProvider
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.usecase.movie.DeleteMovieUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ManageMoviesViewModel(
    private val movieRepository: MovieRepository = RepositoryProvider.movieRepository
) : ViewModel() {

    private val getMoviesUseCase = GetMoviesUseCase(movieRepository)
    private val deleteMovieUseCase = DeleteMovieUseCase(movieRepository)

    private var allMovies: List<Movie> = emptyList()

    private val queryFlow = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ManageMoviesUiState())
    val uiState: StateFlow<ManageMoviesUiState> = _uiState.asStateFlow()

    init {
        observeMoviesWithDebounce()
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                getMoviesUseCase()
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить фильмы"
                )
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            query = query,
            isLoading = query.isNotBlank(),
            errorMessage = null
        )

        queryFlow.value = query
    }

    fun clearQuery() {
        queryFlow.value = ""

        _uiState.value = _uiState.value.copy(
            query = "",
            movies = allMovies,
            isLoading = false,
            errorMessage = null
        )
    }

    fun deleteMovie(movieId: Long) {
        viewModelScope.launch {
            try {
                deleteMovieUseCase(movieId)
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Не удалось удалить фильм"
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeMoviesWithDebounce() {
        viewModelScope.launch {
            combine(
                movieRepository.observeMovies(),
                queryFlow
                    .debounce(250)
                    .distinctUntilChanged()
            ) { movies, query ->
                allMovies = movies

                val filteredMovies = if (query.isBlank()) {
                    movies
                } else {
                    movies.filter { movie ->
                        movie.title.contains(query, ignoreCase = true)
                    }
                }

                _uiState.value.copy(
                    query = query,
                    movies = filteredMovies,
                    isLoading = false,
                    errorMessage = null
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
