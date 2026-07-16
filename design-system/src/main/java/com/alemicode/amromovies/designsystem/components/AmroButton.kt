package com.alemicode.amromovies.designsystem.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews

private val ButtonHeight = 48.dp
private val LoadingIndicatorSize = 18.dp

@Composable
fun AmroButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        enabled = enabled && !loading,
        shape = AmroShapes.pill,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(LoadingIndicatorSize),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AmroButtonPreview() {
    AmroTheme {
        AmroButton(label = "Add To Watchlist", onClick = {})
    }
}

@ThemePreviews
@Composable
private fun AmroButtonLoadingPreview() {
    AmroTheme {
        AmroButton(label = "Add To Watchlist", onClick = {}, loading = true)
    }
}

@ThemePreviews
@Composable
private fun AmroButtonDisabledPreview() {
    AmroTheme {
        AmroButton(label = "Add To Watchlist", onClick = {}, enabled = false)
    }
}
