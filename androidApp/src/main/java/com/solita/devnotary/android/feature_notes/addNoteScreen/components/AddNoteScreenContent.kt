package com.solita.devnotary.android.feature_notes.addNoteScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.DefaultSpacer
import com.solita.devnotary.android.ui.LocalElevation
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Shape
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.android.utils.getAvailableColors
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun AddNoteScreenContent() {

    val viewModel: NotesViewModel by androidDi.instance()

    val titleInput = viewModel.titleInput.collectAsState()
    val contentInput = viewModel.contentInput.collectAsState()
    val chosenColor = viewModel.chosenColor.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(chosenColor.value).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column() {
                TextField(
                    value = titleInput.value,
                    onValueChange = { if (titleInput.value.length < 30) viewModel.titleInput.value = it },
                    textStyle = Typography.h4,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White.copy(alpha = 0.0f),
                    ), label = { Text(text = "Note title")}
                )
                DefaultSpacer()
                TextField(
                    value = contentInput.value,
                    onValueChange = { viewModel.contentInput.value = it},
                    shape = Shape().bitRoundedCornerShape,
                    textStyle = Typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White.copy(alpha = 0.0f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.0f),
                        focusedIndicatorColor = Color.White.copy(alpha = 0.0f)
                    ),
                    label = { Text(text = "Note content")}
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = LocalSpacing.current.small),
                    horizontalArrangement = Arrangement.Center

                ) {
                    getAvailableColors.forEach {
                        IconButton(
                            onClick = { viewModel.chosenColor.value = it.colorName },
                            modifier = Modifier.padding(horizontal = LocalSpacing.current.xSmall)
                        ) {
                            if (viewModel.chosenColor.value == it.colorName) ColorBall(
                                it.color,
                                true
                            ) else ColorBall(color = it.color, false)
                        }
                    }
                }

            }
        }
        Button(
            onClick = {
                viewModel.addNote()
            }, modifier = Modifier
                .padding(
                    top = LocalSpacing.current.small,
                    bottom = LocalSpacing.current.small,
                    end = LocalSpacing.current.default
                )
                .align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.add_note))
        }
    }
}

@Preview
@Composable
fun PreviewAddNoteScreenContent() {
    AddNoteScreenContent()
}