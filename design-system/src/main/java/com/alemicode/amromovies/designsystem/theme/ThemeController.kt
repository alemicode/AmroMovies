package com.alemicode.amromovies.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class ThemeController(initialDarkTheme: Boolean) {
    var isDarkTheme: Boolean by mutableStateOf(initialDarkTheme)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}

val LocalThemeController = compositionLocalOf<ThemeController> {
    error("No ThemeController provided - wrap content in AmroTheme")
}
