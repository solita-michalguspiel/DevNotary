package com.solita.devnotary.android.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Elevation(
    val default : Dp = 8.dp,
    val small : Dp = 4.dp,
    val medium : Dp = 8.dp,
    val large : Dp = 12.dp,
)

val LocalElevation =  compositionLocalOf{Elevation()}