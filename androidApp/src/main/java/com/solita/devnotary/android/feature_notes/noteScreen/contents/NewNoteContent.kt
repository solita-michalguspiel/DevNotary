package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.feature_notes._sharedComponents.AddButton
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun NewNoteContent() {
    val viewModel: NotesViewModel by androidDi.instance()
    val titleInputState = viewModel.titleInput.collectAsState()
    val contentInputState = viewModel.contentInput.collectAsState()
    val noteColorState = viewModel.noteColor.collectAsState()

    LaunchedEffect(Unit){
        viewModel.clearContent()
    }

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(noteColorState.value).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                TitleTextField(titleInput = titleInputState.value,
                    isEditEnabled = true,
                    onValueChange = { if (it.length <= 30) viewModel.titleInput.value = it })
                ContentTextField(
                    contentInput = contentInputState.value,
                    modifier = Modifier.weight(1.0f), true,
                    onValueChange = {viewModel.contentInput.value = it}
                )
                ColorBallsRow(chosenNoteColor = noteColorState.value){
                    viewModel.noteColor.value = it
                }
            }
        }
        AddButton(modifier = Modifier.align(Alignment.End))
    }
}
