package com.solita.devnotary.android.feature_notes._sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.TestTags
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance

@Preview
@Composable
fun PreviewLocalNoteButtons() {
    LocalNoteButtons(modifier = Modifier, {},{})
}
@Preview
@Composable
fun PreviewSharedNoteButtons() {
    SharedNoteButtons(modifier = Modifier,false)
}
@Composable
fun LocalNoteButtons(modifier: Modifier, navigateToNewNote: () -> Unit,navigateToEditNote: () -> Unit) {
    val buttonModifier = Modifier.padding(LocalSpacing.current.xSmall)
    Row(
        modifier = modifier
            .padding(LocalSpacing.current.small)
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.Bottom,
    ) {
        AddNewNoteButton(buttonModifier.weight(1f).testTag(TestTags.NEW_NOTE_BUTTON_TAG), navigateToNewNote = navigateToNewNote)
        DeleteButton(buttonModifier.weight(1f).testTag(TestTags.DELETE_NOTE_BUTTON_TAG))
        EditButton(
            buttonModifier.weight(
                1f
            ).testTag(TestTags.EDIT_NOTE_BUTTON_TAG)
        ){navigateToEditNote()}
    }
}
@Composable
fun AddNewNoteButton(modifier: Modifier, navigateToNewNote: () -> Unit) {
    Button(onClick = {
        navigateToNewNote()
    }, modifier = modifier) {
        Text(text = stringResource(id = R.string.new_note))
    }
}
@Composable
fun DeleteButton(modifier: Modifier) {
    val viewModel: NoteDetailViewModel by di.instance()
    Button(
        onClick = {
            viewModel.isConfirmDeleteLocalNoteDialogOpen.value = true
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.delete_note))
    }
}
@Composable
fun EditButton(modifier: Modifier,navigateToEditNote: () -> Unit) {
    Button(
        onClick = {
            navigateToEditNote()
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.edit_note))
    }
}

@Composable
fun SharedNoteButtons(modifier: Modifier,isLoading: Boolean) {
    val buttonModifier = Modifier.padding(LocalSpacing.current.xSmall)
    Row(
        modifier = modifier
            .padding(LocalSpacing.current.small)
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        DeleteSharedNote(buttonModifier,isLoading)
        SaveNoteLocallyButton(buttonModifier,isLoading)
    }
}
@Composable
fun SaveNoteLocallyButton(modifier: Modifier,isLoading: Boolean) {
    val viewModel: NoteDetailViewModel by di.instance()
    Button(
        onClick = { viewModel.addNote() }, modifier = modifier, enabled = !isLoading
    ) {
        Text(text = stringResource(R.string.save_note_locally))
    }
}
@Composable
fun DeleteSharedNote(modifier: Modifier,isLoading: Boolean) {
    val viewModel: NoteDetailViewModel by di.instance()
    Button(
        onClick = { viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = true }, modifier = modifier, enabled = !isLoading
    ) {
        Text(text = stringResource(R.string.delete_note))
    }
}