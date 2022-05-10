package com.solita.devnotary.android.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


data class Colors(
    val Black: Color = Color(0xFF000000),
    val LightBlack: Color = Color(0xFF1B1B1B),
    val White: Color = Color(0xFFFFFFFF),
    val Gray: Color = Color(0xFF777777),
    val Red: Color = Color(0xFFFF2C2C),
    val Blue: Color = Color(0xFF0E22E2),
    val LightBlue: Color = Color(0xFF4FA9F8),
    val Pink: Color = Color(0xFFF518BE),
    val Yellow: Color = Color(0xFFF5D316),
    val Green: Color = Color(0xFF1ED809)
)

val LocalColors = compositionLocalOf { Colors() }