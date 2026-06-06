package com.example.film_catalog_android.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.film_catalog_android.core.ui.movie.LandscapeMovieCard
import com.example.film_catalog_android.core.ui.movie.MovieCard
import com.example.film_catalog_android.core.ui.filter.MovieFiltersPanel

@Composable
fun ProfileScreen(
    onMovieClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            ProfileLandscapeContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onSettingsClick = onSettingsClick,
                onRemoveFromWatchList = viewModel::removeFromWatchList,
                onGenreSelected = viewModel::selectGenre,
                onYearSelected = viewModel::selectYear,
                onClearFilters = viewModel::clearFilters
            )
        } else {
            ProfilePortraitContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onSettingsClick = onSettingsClick,
                onRemoveFromWatchList = viewModel::removeFromWatchList,
                onGenreSelected = viewModel::selectGenre,
                onYearSelected = viewModel::selectYear,
                onClearFilters = viewModel::clearFilters
            )
        }
    }
}

@Composable
private fun ProfilePortraitContent(
    uiState: ProfileUiState,
    onMovieClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onRemoveFromWatchList: (Long) -> Unit,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 0.dp
            )
    ) {
        ProfileHeader(
            firstName = uiState.firstName,
            lastName = uiState.lastName,
            onSettingsClick = onSettingsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Буду смотреть",
            style = MaterialTheme.typography.titleMedium
        )

        if (uiState.availableGenres.isNotEmpty() || uiState.availableYears.isNotEmpty()) {
            MovieFiltersPanel(
                selectedGenre = uiState.selectedGenre,
                selectedYear = uiState.selectedYear,
                availableGenres = uiState.availableGenres,
                availableYears = uiState.availableYears,
                onGenreSelected = onGenreSelected,
                onYearSelected = onYearSelected,
                onClearFilters = onClearFilters
            )
        }

        WatchListContent(
            uiState = uiState,
            onMovieClick = onMovieClick,
            onRemoveFromWatchList = onRemoveFromWatchList,
            isLandscape = false
        )
    }
}

@Composable
private fun ProfileLandscapeContent(
    uiState: ProfileUiState,
    onMovieClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onRemoveFromWatchList: (Long) -> Unit,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 16.dp,
                bottom = 0.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(360.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(
                firstName = uiState.firstName,
                lastName = uiState.lastName,
                onSettingsClick = onSettingsClick
            )

            if (uiState.availableGenres.isNotEmpty() || uiState.availableYears.isNotEmpty()) {
                MovieFiltersPanel(
                    selectedGenre = uiState.selectedGenre,
                    selectedYear = uiState.selectedYear,
                    availableGenres = uiState.availableGenres,
                    availableYears = uiState.availableYears,
                    onGenreSelected = onGenreSelected,
                    onYearSelected = onYearSelected,
                    onClearFilters = onClearFilters
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = "Буду смотреть",
                style = MaterialTheme.typography.titleMedium
            )

            WatchListContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onRemoveFromWatchList = onRemoveFromWatchList,
                isLandscape = true
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    firstName: String,
    lastName: String,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = firstName,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = lastName,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = onSettingsClick
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Настройки"
            )
        }
    }
}

@Composable
private fun WatchListContent(
    uiState: ProfileUiState,
    onMovieClick: (Long) -> Unit,
    onRemoveFromWatchList: (Long) -> Unit,
    isLandscape: Boolean
) {
    if (uiState.watchList.isEmpty()) {
        Text(
            text = "Список пока пуст",
            style = MaterialTheme.typography.bodyLarge
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 0.dp,
                end = 0.dp,
                top = if (isLandscape) 0.dp else 8.dp,
                bottom = if (isLandscape) 0.dp else 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(
                if (isLandscape) 12.dp else 16.dp
            )
        ) {
            items(uiState.watchList) { movie ->
                if (isLandscape) {
                    LandscapeMovieCard(
                        movie = movie,
                        onClick = {
                            onMovieClick(movie.id)
                        },
                        onFavoriteClick = {
                            onRemoveFromWatchList(movie.id)
                        }
                    )
                } else {
                    MovieCard(
                        movie = movie,
                        onClick = {
                            onMovieClick(movie.id)
                        },
                        onFavoriteClick = {
                            onRemoveFromWatchList(movie.id)
                        }
                    )
                }
            }
        }
    }
}