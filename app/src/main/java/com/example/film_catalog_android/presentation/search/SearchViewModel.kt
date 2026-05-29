package com.example.film_catalog_android.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.data.repository.SearchHistoryRepositoryImpl
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieRepository: MovieRepository = MockMovieRepository()

    private val searchHistoryRepository: SearchHistoryRepository =
        SearchHistoryRepositoryImpl(
            DatabaseProvider.getDatabase().searchHistoryDao()
        )

    private val queryKey = "search_query"

    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(
        SearchUiState(
            query = savedStateHandle[queryKey] ?: ""
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    fun onQueryChange(query: String) {
        savedStateHandle[queryKey] = query

        _uiState.value = _uiState.value.copy(
            query = query,
            errorMessage = null
        )

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = null,
                hasSearched = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(250)
            performSearch(query)
        }
    }

    fun clearQuery() {
        searchJob?.cancel()
        savedStateHandle[queryKey] = ""

        _uiState.value = _uiState.value.copy(
            query = "",
            isLoading = false,
            results = emptyList(),
            errorMessage = null,
            hasSearched = false
        )
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) return

        searchJob?.cancel()

        viewModelScope.launch {
            performSearch(query)
        }
    }

    fun repeatSearch(title: String) {
        onQueryChange(title)
    }

    fun clearHistory() {
        viewModelScope.launch {
            searchHistoryRepository.clearHistory()
        }
    }

    fun retryLastSearch() {
        search()
    }

    fun onResultClick(movie: Movie) {
        viewModelScope.launch {
            searchHistoryRepository.addToHistory(
                movieId = movie.id,
                title = movie.title
            )
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            searchHistoryRepository.observeHistory().collect { history ->
                _uiState.value = _uiState.value.copy(
                    history = history
                )
            }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            hasSearched = true
        )

        try {
            val movies = movieRepository.searchMovies(query)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                results = movies,
                errorMessage = null
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = "Не удалось загрузить данные"
            )
        }
    }
}