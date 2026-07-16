package com.alemicode.amromovies.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space

@Composable
fun AmroPill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .clip(AmroShapes.pill)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = MaterialTheme.space.space8, vertical = MaterialTheme.space.space4),
    )
}

@ThemePreviews
@Composable
private fun AmroPillPreview() {
    AmroTheme {
        AmroPill(text = "2023")
    }
}
