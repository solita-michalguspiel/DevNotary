package com.solita.devnotary.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.solita.devnotary.android.theme.Colors
import com.solita.devnotary.android.theme.LocalColors


class NoteColor(private val color: String) {

    @Composable
    fun getColor(): Color {
        return when (color) {
            "red" -> LocalColors.current.Red
            "blue" -> LocalColors.current.LightBlue
            "green" -> LocalColors.current.Green
            "pink" -> LocalColors.current.Pink
            "yellow" -> LocalColors.current.Yellow
            "white" -> LocalColors.current.White
            else -> LocalColors.current.White
        }

    }

}

enum class AvailableColors(val color: Color, val colorName: String) {
    RED(Colors().Red, "red"),
    BLUE(Colors().LightBlue, "blue"),
    GREEN(Colors().Green, "green"),
    PINK(Colors().Pink, "pink"),
    YELLOW(Colors().Yellow, "yellow"),
    WHITE(Colors().White, "white")
}

val getAvailableColors
    get() = listOf(
        AvailableColors.RED,
        AvailableColors.BLUE,
        AvailableColors.GREEN,
        AvailableColors.PINK,
        AvailableColors.YELLOW,
        AvailableColors.WHITE
    )