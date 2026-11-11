package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TaxiYellow,
    onPrimary = Slate950,
    primaryContainer = TaxiYellowDark,
    onPrimaryContainer = Color.White,
    secondary = SkyBlue400,
    onSecondary = Slate950,
    secondaryContainer = Slate800,
    onSecondaryContainer = SkyBlue400,
    tertiary = PowerballRedLight,
    onTertiary = Color.White,
    background = Slate950,
    onBackground = Color.White,
    surface = Slate900,
    onSurface = Color.White,
    surfaceVariant = Slate800,
    onSurfaceVariant = Slate300,
    outline = Slate700
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek taxi dark mode
    dynamicColor: Boolean = false, // Keep taxi branding colors consistent
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
