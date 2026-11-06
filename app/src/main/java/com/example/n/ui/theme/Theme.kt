package com.example.n.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Color(0xFF0288D1),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = Color(0xFF006064),

    tertiary = Color(0xFF2E7D32),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE8F5E9),
    onTertiaryContainer = Color(0xFF1B5E20),

    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF191C1E),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191C1E),

    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44474F),

    outline = Color(0xFF74777F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004881),
    onPrimaryContainer = Color(0xFFD0E4FF),

    secondary = Color(0xFF81D4FA),
    onSecondary = Color(0xFF003547),
    secondaryContainer = Color(0xFF004D65),
    onSecondaryContainer = Color(0xFFE0F7FA),

    tertiary = Color(0xFFA5D6A7),
    onTertiary = Color(0xFF00390A),
    tertiaryContainer = Color(0xFF005313),
    onTertiaryContainer = Color(0xFFE8F5E9),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E2E5),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E2E5),

    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFC4C6CF),

    outline = Color(0xFF8E9099)
)

@Composable
fun ĐồÁnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
