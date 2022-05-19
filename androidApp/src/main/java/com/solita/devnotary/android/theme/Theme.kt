package com.solita.devnotary.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorPalette = lightColors(
    primary = Colors().ThemeBlue,
    surface = Colors().LightGray,
    primaryVariant = Colors().ThemeBlueSecond,
    secondary = Colors().ThemeDarkestYellow,
    background = Colors().VeryLightGray,
    onBackground = Colors().Black,
    onSurface = Colors().Black,
    onPrimary = Colors().White
)

private val DarkColorPalette = darkColors(
    primary = Colors().ThemeLightBlue,
    surface = Colors().Gray,
    primaryVariant = Colors().ThemeBlueSecond,
    secondary = Colors().ThemeDarkYellow,
    background = Colors().LightBlack,
    onSurface = Colors().Black,
    onBackground = Colors().White,
    onPrimary = Colors().Black
)

@Composable
fun DevNotaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    systemUiController.setSystemBarsColor(colors.background)
    CompositionLocalProvider(LocalSpacing provides Spacing(),
        LocalElevation provides Elevation(),
        LocalColors provides Colors(),
    LocalShapes provides Shapes()) {
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content,
    )
}