package com.solita.devnotary.android.feature_notes.addNoteScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.components.DefaultSpacer
import com.solita.devnotary.android.ui.LocalElevation
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Shape
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.android.utils.getAvailableColors
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.ComposeViewModel
import com.solita.devnotary.feature_notes.presentation.NotesViewModel

@Composable
fun AddNoteScreenContent(viewModel: ComposableViewModel) {

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

        Card(
            modifier = Modifier.padding(LocalSpacing.current.small).weight(1.0f),
            backgroundColor = NoteColor(chosenColor).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            TextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                textStyle = Typography.h4,
                modifier = Modifier.fillMaxWidth()
            )
            DefaultSpacer()
            TextField(
                value = contentInput,
                onValueChange = { contentInput = it },
                shape = Shape().bitRoundedCornerShape,
                textStyle = Typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
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
        Button(
            onClick = { (viewModel as NotesViewModel).addNote(
                title =titleInput,
                content = contentInput,
                color = chosenColor,
            ) }, modifier = Modifier
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
    val composeViewModel = ComposeViewModel()
    AddNoteScreenContent(composeViewModel)
}