package com.solita.devnotary.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.solita.devnotary.android.ui.LocalColors


class NoteColor(private val color: String) {

    @Composable
    fun getColor(): Color {
        return when (color) {
            "red" ->LocalColors.current.Red
            "blue" ->LocalColors.current.Blue
            "green" -> LocalColors.current.Green
            "pink" -> LocalColors.current.Pink
            "yellow" -> LocalColors.current.Yellow
            else -> LocalColors.current.White
        }

    }


}