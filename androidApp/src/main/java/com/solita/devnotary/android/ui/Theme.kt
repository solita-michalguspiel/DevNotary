package com.solita.devnotary.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color


private val DarkColorPalette = darkColors(
    primary = Colors().Black,
    primaryVariant = Colors().LightBlack,
    secondary = Colors().Gray,
    background = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Colors().Black,
    primaryVariant = Colors().LightBlack,
    secondary = Colors().Gray,
    background = Color.White,
)

@Composable
fun DevNotaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    CompositionLocalProvider(LocalSpacing provides Spacing(),
        LocalElevation provides Elevation(),
        LocalColors provides Colors()) {
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content,
    )
}