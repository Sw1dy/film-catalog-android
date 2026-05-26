package com.example.film_catalog_android.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_catalog_android.data.repository.MockMovieRepository
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
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
    private val queryKey = "search_query"

    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(
        SearchUiState(
            query = savedStateHandle[queryKey] ?: ""
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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

    fun repeatSearch(query: String) {
        onQueryChange(query)
    }

    fun clearHistory() {
        _uiState.value = _uiState.value.copy(
            history = emptyList()
        )
    }

    fun retryLastSearch() {
        search()
    }

    fun onResultClick(movie: Movie) {
        _uiState.value = _uiState.value.copy(
            history = addToHistory(movie.title)
        )
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

    private fun addToHistory(query: String): List<String> {
        val currentHistory = _uiState.value.history

        return listOf(query)
            .plus(currentHistory.filterNot { it.equals(query, ignoreCase = true) })
            .take(10)
    }
}