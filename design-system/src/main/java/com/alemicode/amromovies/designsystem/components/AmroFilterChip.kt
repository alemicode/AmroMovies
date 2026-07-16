package com.alemicode.amromovies.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space

@Composable
fun AmroFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "filterChipContainerColor",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "filterChipContentColor",
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = AmroShapes.pill,
        color = containerColor,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.space.space16,
                vertical = MaterialTheme.space.space8,
            ),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AmroFilterChipSelectedPreview() {
    AmroTheme {
        AmroFilterChip(label = "Action", selected = true, onClick = {})
    }
}

@ThemePreviews
@Composable
private fun AmroFilterChipUnselectedPreview() {
    AmroTheme {
        AmroFilterChip(label = "Action", selected = false, onClick = {})
    }
}
