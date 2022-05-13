package com.solita.devnotary.android.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val default : Dp = 12.dp,
    val xxSmall : Dp = 2.dp,
    val xSmall : Dp = 4.dp,
    val small : Dp = 12.dp,
    val medium : Dp = 16.dp,
    val large : Dp = 22.dp,
    val xLarge : Dp = 32.dp,
)
val LocalSpacing = compositionLocalOf { Spacing() }