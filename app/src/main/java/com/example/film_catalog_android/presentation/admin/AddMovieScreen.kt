package com.example.film_catalog_android.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.film_catalog_android.core.ui.MovieFormTextField

@Composable
fun AddMovieScreen(
    onBackClick: () -> Unit,
    onMovieAdded: () -> Unit,
    viewModel: AddMovieViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 24.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Добавление фильма",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        item {
            MovieFormTextField(
                value = uiState.imageUrl,
                onValueChange = viewModel::onImageUrlChange,
                placeholder = "URL изображения"
            )
        }

        item {
            MovieFormTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = "Название"
            )
        }

        item {
            MovieFormTextField(
                value = uiState.year,
                onValueChange = viewModel::onYearChange,
                placeholder = "Год"
            )
        }

        item {
            MovieFormTextField(
                value = uiState.genre,
                onValueChange = viewModel::onGenreChange,
                placeholder = "Жанр"
            )
        }

        item {
            MovieFormTextField(
                value = uiState.rating,
                onValueChange = viewModel::onRatingChange,
                placeholder = "Рейтинг"
            )
        }

        item {
            MovieFormTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                placeholder = "Описание",
                singleLine = false,
                minLines = 3
            )
        }

        if (uiState.errorMessage != null) {
            item {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.addMovie(onMovieAdded)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Добавить")
                }
            }
        }

        item {
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Назад")
            }
        }
    }
}