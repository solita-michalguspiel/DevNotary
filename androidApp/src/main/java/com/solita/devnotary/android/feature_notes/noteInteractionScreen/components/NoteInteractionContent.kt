package com.solita.devnotary.android.feature_notes.noteInteractionScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.solita.devnotary.Constants
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.TestTags
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel

@Composable
fun NoteInteractionContent(
    paddingValues: PaddingValues,
    displayedNoteState: State<Note>,
    viewModel: NoteDetailViewModel,
    note: Note?
) {
    fun isNoteValid(): Boolean {
        if (displayedNoteState.value.title.isBlank()) {
            viewModel.setNoteModificationError(Constants.NO_TITLE_ERROR)
            return false
        }
        if (displayedNoteState.value.content.isBlank()) {
            viewModel.setNoteModificationError(Constants.BLANK_NOTE_ERROR)
            return false
        }
        return true
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(paddingValues).testTag(TestTags.NOTE_INTERACTION_SCREEN)) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.medium)
                .weight(1.0f),
            backgroundColor = NoteColor(displayedNoteState.value.color).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                TitleTextField(
                    modifier = Modifier.testTag(TestTags.TITLE_TEXT_FIELD_TAG),
                    titleInput = displayedNoteState.value.title,
                    isEditEnabled = true,
                    onValueChange = { if (it.length <= 30) viewModel.changeTitleInput(it) })
                ContentTextField(
                    contentInput = displayedNoteState.value.content,
                    modifier = Modifier.weight(1.0f).testTag(TestTags.CONTENT_TEXT_FIELD_TAG),
                    isEditEnabled = true,
                    onValueChange = { viewModel.changeContentInput(it) }
                )
                ColorBallsRow(chosenNoteColor = displayedNoteState.value.color) {
                    viewModel.changeNoteColor(it)
                }
            }
        }
        if (note == null) {
            AddButton(modifier = Modifier.align(Alignment.End).testTag(TestTags.ADD_NOTE_BUTTON_TAG)) {
                if (isNoteValid()) viewModel.addNote()
            }
        } else {
            SaveButton(modifier = Modifier.align(Alignment.End).testTag(TestTags.SAVE_NOTE_BUTTON_TAG)) {
                if (isNoteValid()) viewModel.editNote()
            }
        }
    }
}