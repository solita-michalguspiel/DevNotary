package com.solita.devnotary.android.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

data class Shapes(
    val smallRoundedCornerShape: RoundedCornerShape = RoundedCornerShape(4.dp),
    val mediumRoundedCornerShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    val largeRoundedCornerShape: RoundedCornerShape = RoundedCornerShape(12.dp),

)

val LocalShapes = compositionLocalOf { Shapes() }