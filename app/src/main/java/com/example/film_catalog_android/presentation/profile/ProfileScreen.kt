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
import com.example.film_catalog_android.core.ui.MovieCard

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
                onRemoveFromWatchList = viewModel::removeFromWatchList
            )
        } else {
            ProfilePortraitContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onSettingsClick = onSettingsClick,
                onRemoveFromWatchList = viewModel::removeFromWatchList
            )
        }
    }
}

@Composable
private fun ProfilePortraitContent(
    uiState: ProfileUiState,
    onMovieClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onRemoveFromWatchList: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
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

        Spacer(modifier = Modifier.height(8.dp))

        WatchListContent(
            uiState = uiState,
            onMovieClick = onMovieClick,
            onRemoveFromWatchList = onRemoveFromWatchList
        )
    }
}

@Composable
private fun ProfileLandscapeContent(
    uiState: ProfileUiState,
    onMovieClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onRemoveFromWatchList: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.8f),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(
                firstName = uiState.firstName,
                lastName = uiState.lastName,
                onSettingsClick = onSettingsClick
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.7f)
        ) {
            Text(
                text = "Буду смотреть",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            WatchListContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onRemoveFromWatchList = onRemoveFromWatchList
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
    onRemoveFromWatchList: (Long) -> Unit
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
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.watchList) { movie ->
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