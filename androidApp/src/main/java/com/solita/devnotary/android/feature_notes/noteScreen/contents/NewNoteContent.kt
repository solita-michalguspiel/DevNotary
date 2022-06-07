package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.solita.devnotary.Constants
import com.solita.devnotary.android.feature_notes._sharedComponents.AddButton
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance

@Composable
fun NewNoteContent() {
    val viewModel: NoteDetailViewModel by di.instance()
    val displayedNoteState = viewModel.displayedNote.collectAsState(initial = Constants.CLEAR_NOTE)

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(displayedNoteState.value.color).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                TitleTextField(titleInput = displayedNoteState.value.title,
                    isEditEnabled = true,
                    onValueChange = { if (it.length <= 30) viewModel.changeTitleInput(it) })
                ContentTextField(
                    contentInput = displayedNoteState.value.content,
                    modifier = Modifier.weight(1.0f), true,
                    onValueChange = {viewModel.changeContentInput(it)}
                )
                ColorBallsRow(chosenNoteColor = displayedNoteState.value.color){
                    viewModel.changeNoteColor(it)
                }
            }
        }
        AddButton(modifier = Modifier.align(Alignment.End)){
            if(displayedNoteState.value.title.isNullOrBlank()) viewModel.setNoteModificationError(
                Constants.NO_TITLE_ERROR
            )
            else if(displayedNoteState.value.content.isNullOrBlank()) viewModel.setNoteModificationError(
                Constants.BLANK_NOTE_ERROR
            )
            else viewModel.addNote()
        }
    }
}
