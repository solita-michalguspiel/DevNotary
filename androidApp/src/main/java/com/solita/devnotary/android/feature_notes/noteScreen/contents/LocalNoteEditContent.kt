package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes._sharedComponents.LocalNoteButtons
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNoteEditContent(
    navigateToNewNote: () -> Unit
) {
    val viewModel: NotesViewModel by di.instance()
    val titleInputState = viewModel.titleInput.collectAsState("")
    val contentInputState = viewModel.contentInput.collectAsState("")
    val noteColorState = viewModel.noteColor.collectAsState()

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
                    onValueChange = { if (it.length <= 30) viewModel.changeTitleInput(it)})
                ContentTextField(
                    contentInput = contentInputState.value,
                    modifier = Modifier.weight(1.0f),
                    isEditEnabled = true,
                    onValueChange = { viewModel.changeContentInput(it) }
                )
                ColorBallsRow(chosenNoteColor = noteColorState.value) {
                    viewModel.noteColor.value = it
                }
            }
        }
        LocalNoteButtons(modifier = Modifier.align(Alignment.End), true, navigateToNewNote)
    }
}
