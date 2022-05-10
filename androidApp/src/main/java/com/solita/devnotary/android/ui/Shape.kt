package com.solita.devnotary.android.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class Shape(
    val bitRoundedCornerShape : Shape = RoundedCornerShape(6.dp),
    val mediumRoundedCornerShape: Shape = RoundedCornerShape(14.dp),
    val largelyRoundedCornerShape : Shape = RoundedCornerShape(24.dp)
)