package com.solita.devnotary.android.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class Shape(
    val small : Shape = RoundedCornerShape(4.dp),
    val medium: Shape = RoundedCornerShape(4.dp),
    val large : Shape = RoundedCornerShape(0.dp)
)