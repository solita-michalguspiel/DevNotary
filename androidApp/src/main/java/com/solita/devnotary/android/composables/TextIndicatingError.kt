package com.solita.devnotary.android.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.solita.devnotary.android.theme.Typography

@Composable
fun TextIndicatingError(
    errorMessage: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
Text(text = errorMessage,modifier = modifier, color = Color.Red,style = Typography.body2, textAlign = textAlign)

}