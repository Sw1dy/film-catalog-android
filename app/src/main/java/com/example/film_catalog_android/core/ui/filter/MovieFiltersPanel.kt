package com.example.film_catalog_android.core.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MovieFiltersPanel(
    selectedGenre: String?,
    selectedYear: Int?,
    availableGenres: List<String>,
    availableYears: List<Int>,
    filterErrorMessage: String? = null,
    onGenreSelected: (String?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onClearFilters: () -> Unit,
    onReloadFilters: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val hasActiveFilters = selectedGenre != null || selectedYear != null

    Column(
        modifier = modifier
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
                selectedValue = selectedGenre ?: "Все",
                items = listOf("Все") + availableGenres,
                onItemSelected = { genre ->
                    onGenreSelected(
                        if (genre == "Все") null else genre
                    )
                }
            )

            FilterDropdown(
                modifier = Modifier.weight(1f),
                title = "Год",
                selectedValue = selectedYear?.toString() ?: "Все",
                items = listOf("Все") + availableYears.map { it.toString() },
                onItemSelected = { year ->
                    onYearSelected(
                        if (year == "Все") null else year.toIntOrNull()
                    )
                }
            )
        }

        if (filterErrorMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = filterErrorMessage,
                    modifier = Modifier.weight(1f)
                )

                if (onReloadFilters != null) {
                    TextButton(
                        onClick = onReloadFilters
                    ) {
                        Text("Повторить")
                    }
                }
            }
        }

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