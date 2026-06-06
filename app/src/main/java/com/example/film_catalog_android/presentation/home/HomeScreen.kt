package com.example.film_catalog_android.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.film_catalog_android.core.ui.movie.LandscapeMovieCard
import com.example.film_catalog_android.core.ui.movie.MovieCard
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.core.ui.filter.MovieFiltersPanel

@Composable
fun HomeScreen(
    onMovieClick: (Long) -> Unit,
    onAddMovieClick: () -> Unit,
    onManageMoviesClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeHomeContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onAddMovieClick = onAddMovieClick,
                onManageMoviesClick = onManageMoviesClick,
                onFavoriteClick = viewModel::toggleWatchList,
                onRetryClick = viewModel::loadMovies,
                onGenreSelected = viewModel::selectGenre,
                onYearSelected = viewModel::selectYear,
                onClearFilters = viewModel::clearFilters,
                onReloadFilters = viewModel::loadFilterOptions
            )
        } else {
            PortraitHomeContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onAddMovieClick = onAddMovieClick,
                onManageMoviesClick = onManageMoviesClick,
                onFavoriteClick = viewModel::toggleWatchList,
                onRetryClick = viewModel::loadMovies,
                onGenreSelected = viewModel::selectGenre,
                onYearSelected = viewModel::selectYear,
                onClearFilters = viewModel::clearFilters,
                onReloadFilters = viewModel::loadFilterOptions
            )
        }
    }
}

@Composable
private fun PortraitHomeContent(
    uiState: HomeUiState,
    onMovieClick: (Long) -> Unit,
    onAddMovieClick: () -> Unit,
    onManageMoviesClick: () -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onRetryClick: () -> Unit,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit,
    onReloadFilters: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 32.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeHeader(
                isAdmin = uiState.isAdmin,
                onAddMovieClick = onAddMovieClick,
                onManageMoviesClick = onManageMoviesClick
            )
        }

        if (uiState.availableGenres.isNotEmpty() || uiState.availableYears.isNotEmpty()) {
            item {
                MovieFiltersPanel(
                    selectedGenre = uiState.selectedGenre,
                    selectedYear = uiState.selectedYear,
                    availableGenres = uiState.availableGenres,
                    availableYears = uiState.availableYears,
                    filterErrorMessage = uiState.filterErrorMessage,
                    onGenreSelected = onGenreSelected,
                    onYearSelected = onYearSelected,
                    onClearFilters = onClearFilters,
                    onReloadFilters = onReloadFilters
                )
            }
        }

        if (uiState.isLoading) {
            item {
                LoadingBlock()
            }
        }

        if (uiState.errorMessage != null) {
            item {
                ErrorBlock(
                    message = uiState.errorMessage,
                    onRetryClick = onRetryClick
                )
            }
        }

        items(uiState.movies) { movie ->
            MovieCard(
                movie = movie,
                onClick = {
                    onMovieClick(movie.id)
                },
                onFavoriteClick = {
                    onFavoriteClick(movie)
                }
            )
        }
    }
}

@Composable
private fun LandscapeHomeContent(
    uiState: HomeUiState,
    onMovieClick: (Long) -> Unit,
    onAddMovieClick: () -> Unit,
    onManageMoviesClick: () -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onRetryClick: () -> Unit,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit,
    onReloadFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 16.dp,
                bottom = 0.dp
            )
    ) {
        Column(
            modifier = Modifier
                .width(360.dp)
                .padding(end = 24.dp)
        ) {
            Text(
                text = "Советуем посмотреть",
                style = MaterialTheme.typography.titleLarge
            )

            if (uiState.isAdmin) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onAddMovieClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Добавить фильм"
                        )
                    }

                    IconButton(
                        onClick = onManageMoviesClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Управление фильмами"
                        )
                    }
                }
            }

            if (uiState.availableGenres.isNotEmpty() || uiState.availableYears.isNotEmpty()) {
                MovieFiltersPanel(
                    selectedGenre = uiState.selectedGenre,
                    selectedYear = uiState.selectedYear,
                    availableGenres = uiState.availableGenres,
                    availableYears = uiState.availableYears,
                    filterErrorMessage = uiState.filterErrorMessage,
                    onGenreSelected = onGenreSelected,
                    onYearSelected = onYearSelected,
                    onClearFilters = onClearFilters,
                    onReloadFilters = onReloadFilters
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            if (uiState.errorMessage != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    TextButton(
                        onClick = onRetryClick
                    ) {
                        Text(text = "Повторить")
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = 0.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.movies) { movie ->
                LandscapeMovieCard(
                    movie = movie,
                    onClick = {
                        onMovieClick(movie.id)
                    },
                    onFavoriteClick = {
                        onFavoriteClick(movie)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    isAdmin: Boolean,
    onAddMovieClick: () -> Unit,
    onManageMoviesClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Советуем посмотреть",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        if (isAdmin) {
            IconButton(
                onClick = onAddMovieClick
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Добавить фильм"
                )
            }

            IconButton(
                onClick = onManageMoviesClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Управление фильмами"
                )
            }
        }
    }
}

@Composable
private fun LoadingBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorBlock(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )

        TextButton(
            onClick = onRetryClick
        ) {
            Text(text = "Повторить")
        }
    }
}