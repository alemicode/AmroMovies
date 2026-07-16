package com.alemicode.amromovies.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

private val AmroDarkColorScheme = darkColorScheme(
    primary = AmroPrimary,
    onPrimary = AmroOnPrimary,
    background = AmroBackgroundDark,
    onBackground = AmroOnSurfaceDark,
    surface = AmroSurfaceDark,
    onSurface = AmroOnSurfaceDark,
    surfaceVariant = AmroSurfaceVariantDark,
    onSurfaceVariant = AmroOnSurfaceVariantDark,
    error = AmroErrorDark,
    onError = AmroOnErrorDark,
)

private val AmroLightColorScheme = lightColorScheme(
    primary = AmroPrimary,
    onPrimary = AmroOnPrimary,
    background = AmroBackgroundLight,
    onBackground = AmroOnSurfaceLight,
    surface = AmroSurfaceLight,
    onSurface = AmroOnSurfaceLight,
    surfaceVariant = AmroSurfaceVariantLight,
    onSurfaceVariant = AmroOnSurfaceVariantLight,
    error = AmroErrorLight,
    onError = AmroOnErrorLight,
)

@Composable
fun AmroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeController = remember { ThemeController(darkTheme) }

    CompositionLocalProvider(
        LocalSpace provides Space(),
        LocalThemeController provides themeController,
    ) {
        MaterialTheme(
            colorScheme = if (themeController.isDarkTheme) AmroDarkColorScheme else AmroLightColorScheme,
            typography = AmroTypography,
            content = content,
        )
    }
}
