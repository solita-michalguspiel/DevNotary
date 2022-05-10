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
fun ColorBall(color: Color, isChosen : Boolean = false) {
Canvas(modifier = Modifier.size(30.dp), onDraw = {
    if(isChosen)drawCircle(Color.Black,style = Stroke(width = 2.5f))
    else drawCircle(Color.Black,style = Stroke(width = 0.5f))
    drawCircle(color,style = Fill)
} )
}

@Preview
@Composable
fun previewColorBall(){
    ColorBall(color = Color.Blue,false)
}
@Preview
@Composable
fun previewSelectedColorBall(){
    ColorBall(color = Color.Blue,true)
}

