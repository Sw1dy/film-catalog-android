package com.example.film_catalog_android.core.ui.movie

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = ratingBadgeColor(rating)
    ) {
        Text(
            text = String.format("%.1f", rating),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2B2B2B)
        )
    }
}

private fun ratingBadgeColor(rating: Double): Color {
    return when {
        rating < 5.0 -> Color(0xFFCF8585)
        rating < 7.0 -> Color(0xFFAAAAAA)
        rating < 8.0 -> Color(0xFF9AC89D)
        else -> Color(0xFFC8BD9A)
    }
}