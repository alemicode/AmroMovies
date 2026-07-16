package com.alemicode.amromovies.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import coil3.compose.AsyncImage

@Composable
fun AmroPosterImage(
    posterUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = posterUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(AmroShapes.poster)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}

@ThemePreviews
@Composable
private fun AmroPosterImagePreview() {
    AmroTheme {
        AmroPosterImage(
            posterUrl = null,
            contentDescription = "Preview poster",
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(2f / 3f),
        )
    }
}
