package com.solita.devnotary.android.feature_notes.addNoteScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.components.DefaultSpacer
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.android.utils.getAvailableColors

@Composable
fun AddNoteScreenContent() {

    var titleInput by remember {
        mutableStateOf("")
    }
    var contentInput by remember {
        mutableStateOf("")
    }
    var chosenColor by remember {
        mutableStateOf("")
    }

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            textStyle = Typography.h4
        )
        DefaultSpacer()
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            getAvailableColors.forEach {
                IconButton(
                    onClick = { chosenColor = it.colorName },
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.xSmall)
                ) {
                    if (chosenColor == it.colorName) ColorBall(
                        it.color,
                        true
                    ) else ColorBall(color = it.color, false)
                }
            }
        }
        TextField(
            value = contentInput,
            onValueChange = { contentInput = it },
            textStyle = Typography.body2,
            modifier = Modifier.fillMaxSize()
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Add note")
        }


    }

}

@Preview
@Composable
fun PreviewAddNoteScreenContent() {
    AddNoteScreenContent()
}