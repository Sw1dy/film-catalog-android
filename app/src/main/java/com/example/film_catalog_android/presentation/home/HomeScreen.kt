package com.example.film_catalog_android.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.film_catalog_android.core.ui.LandscapeMovieCard
import com.example.film_catalog_android.core.ui.MovieCard
import com.example.film_catalog_android.domain.model.Movie
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

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

        item {
            MovieFiltersPanel(
                uiState = uiState,
                onGenreSelected = onGenreSelected,
                onYearSelected = onYearSelected,
                onClearFilters = onClearFilters,
                onReloadFilters = onReloadFilters
            )
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
                .padding(end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
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

            MovieFiltersPanel(
                uiState = uiState,
                onGenreSelected = onGenreSelected,
                onYearSelected = onYearSelected,
                onClearFilters = onClearFilters,
                onReloadFilters = onReloadFilters
            )

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

@Composable
private fun MovieFiltersPanel(
    uiState: HomeUiState,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit,
    onReloadFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterDropdown(
                modifier = Modifier.weight(1f),
                title = "Жанр",
                selectedValue = uiState.selectedGenre ?: "Все",
                items = listOf("Все") + uiState.availableGenres,
                onItemSelected = { genre ->
                    onGenreSelected(
                        if (genre == "Все") null else genre
                    )
                }
            )

            FilterDropdown(
                modifier = Modifier.weight(1f),
                title = "Год",
                selectedValue = uiState.selectedYear?.toString() ?: "Все",
                items = listOf("Все") + uiState.availableYears.map { it.toString() },
                onItemSelected = { year ->
                    onYearSelected(
                        if (year == "Все") null else year.toIntOrNull()
                    )
                }
            )
        }

        if (uiState.filterErrorMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.filterErrorMessage,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = onReloadFilters
                ) {
                    Text("Повторить")
                }
            }
        }

        val hasActiveFilters = uiState.selectedGenre != null || uiState.selectedYear != null

        if (hasActiveFilters) {
            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = onClearFilters,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сбросить фильтры")
            }
        }
    }
}

@Composable
private fun FilterDropdown(
    modifier: Modifier = Modifier,
    title: String,
    selectedValue: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = {
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$title: $selectedValue",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(item)
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}