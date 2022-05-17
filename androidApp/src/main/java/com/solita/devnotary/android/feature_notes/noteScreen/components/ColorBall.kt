package com.solita.devnotary.android.feature_notes.addNoteScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorBall(modifier: Modifier = Modifier, color: Color, isChosen: Boolean = false) {
    Canvas(modifier = modifier.size(40.dp), onDraw = {
        if (isChosen) drawCircle(Color.Black, style = Stroke(width = 2.5f))
        else drawCircle(Color.Black, style = Stroke(width = 0.5f))
        drawCircle(color, style = Fill)
    })
}

@Preview
@Composable
fun PreviewColorBall() {
    ColorBall(color = Color.Blue, isChosen = false)
}

@Preview
@Composable
fun PreviewSelectedColorBall() {
    ColorBall(color = Color.Blue, isChosen = true)
}

