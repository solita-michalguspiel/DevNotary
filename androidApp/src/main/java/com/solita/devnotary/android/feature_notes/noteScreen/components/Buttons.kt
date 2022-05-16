package com.solita.devnotary.android.feature_notes._sharedComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance


@Preview
@Composable
fun PreviewLocalNoteButtons() {
    LocalNoteButtons(modifier = Modifier, isEditEnabled = true)
}

@Composable
fun LocalNoteButtons(modifier: Modifier, isEditEnabled: Boolean) {
    val buttonModifier = Modifier.padding(LocalSpacing.current.xSmall)
    Row(
        modifier = modifier.padding(LocalSpacing.current.small),
        verticalAlignment = Alignment.Bottom
    ) {
        AddNewNoteButton(buttonModifier)
        DeleteButton(buttonModifier)
        if (isEditEnabled) SaveButton(buttonModifier) else EditButton(buttonModifier)
    }
}

@Composable
fun AddNewNoteButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(onClick = {
        viewModel.changeNoteScreenState(NoteScreenState.NewNote)
    }, modifier = modifier) {
        Text(text = stringResource(id = R.string.new_note))
    }
}


@Composable
fun SaveButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.editNote()
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.save_note))
    }
}

@Composable
fun SaveNoteLocallyButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = { viewModel.addNote() }, modifier = modifier.padding(LocalSpacing.current.small)
    ) {
        Text(text = stringResource(R.string.save_note_locally))
    }
}

@Composable
fun DeleteButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.isConfirmDeleteDialogOpen.value = true
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.delete_note))
    }
}

@Composable
fun EditButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.changeNoteScreenState(NoteScreenState.LocalNoteEdit)
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.edit_note))
    }
}


@Composable
fun AddButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.addNote()
        }, modifier = modifier.padding(LocalSpacing.current.small)
    ) {
        Text(text = stringResource(id = R.string.add_note))
    }
}