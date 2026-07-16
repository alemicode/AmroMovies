package com.alemicode.amromovies.designsystem.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.LocalThemeController
import com.alemicode.amromovies.designsystem.theme.ThemePreviews

@Composable
fun AmroThemeToggleButton(modifier: Modifier = Modifier) {
    val themeController = LocalThemeController.current

    IconButton(onClick = { themeController.toggleTheme() }, modifier = modifier) {
        Text(
            text = if (themeController.isDarkTheme) "☀" else "🌙",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@ThemePreviews
@Composable
private fun AmroThemeToggleButtonPreview() {
    AmroTheme {
        AmroThemeToggleButton()
    }
}
