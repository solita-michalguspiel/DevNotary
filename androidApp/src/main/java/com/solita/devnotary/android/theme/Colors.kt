package com.solita.devnotary.android.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors(
    val Black: Color = Color(0xFF0A0A0A),
    val LightBlack: Color = Color(0xFF1B1B1B),
    val White: Color = Color(0xFFFFFFFF),
    val Gray: Color = Color(0xFF777777),
    val LightGray: Color = Color(0xFFD1D1D1),
    val VeryLightGray: Color = Color(0xFFE6E6E6),
    val Red: Color = Color(0xFFFF5757),
    val Blue: Color = Color(0xFF0E22E2),
    val Pink: Color = Color(0xFFEE7DD2),
    val Yellow: Color = Color(0xFFFAE25C),
    val Green: Color = Color(0xFF79FA6B),


    /**THEME*/
    val ThemeDarkYellow : Color = Color(0xFFF5BA1D),
    val ThemeDarkestYellow : Color = Color(0xFFA87B00),
    val ThemeLightBlue: Color = Color(0xFF3867FF),
    val ThemeBlue : Color = Color(0xFF0A2FA8),
    val ThemeBlueSecond : Color = Color(0xFF1D50F5)

    )

val LocalColors = compositionLocalOf { Colors() }