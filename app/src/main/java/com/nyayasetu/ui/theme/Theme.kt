package com.nyayasetu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = NyayaBlue,
    onPrimary = NyayaSurface,
    secondary = NyayaGold,
    background = NyayaSurface,
    onBackground = NyayaText,
    surface = Color.White,
    onSurface = NyayaText,
)

private val DarkColors = darkColorScheme(
    primary = NyayaGold,
    onPrimary = NyayaBlueDark,
    secondary = NyayaBlue,
    background = NyayaBlueDark,
    onBackground = NyayaSurface,
    surface = Color(0xFF152033),
    onSurface = NyayaSurface,
)

@Composable
fun NyayaSetuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = NyayaTypography,
        content = content,
    )
}
