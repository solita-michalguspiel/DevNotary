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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Preview
@Composable
fun PreviewLocalNoteButtons() {
    LocalNoteButtons(modifier = Modifier, isEditEnabled = true, {})
}

@Preview
@Composable
fun PreviewSharedNoteButtons() {
    SharedNoteButtons(modifier = Modifier)
}

@Composable
fun LocalNoteButtons(modifier: Modifier, isEditEnabled: Boolean, navigateToNewNote: () -> Unit) {
    val buttonModifier = Modifier.padding(LocalSpacing.current.xSmall)
    Row(
        modifier = modifier
            .padding(LocalSpacing.current.small)
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.Bottom,
    ) {
        AddNewNoteButton(buttonModifier.weight(1f), navigateToNewNote = navigateToNewNote)
        DeleteButton(buttonModifier.weight(1f))
        if (isEditEnabled) SaveButton(buttonModifier.weight(1f)) else EditButton(
            buttonModifier.weight(
                1f
            )
        )
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
fun SharedNoteButtons(modifier: Modifier) {
    val buttonModifier = Modifier.padding(LocalSpacing.current.xSmall)
    Row(
        modifier = modifier
            .padding(LocalSpacing.current.small)
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        DeleteSharedNote(buttonModifier)
        SaveNoteLocallyButton(buttonModifier)

    }

}


@Composable
fun SaveNoteLocallyButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = { viewModel.addNote() }, modifier = modifier
    ) {
        Text(text = stringResource(R.string.save_note_locally))
    }
}

@Composable
fun DeleteSharedNote(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = { viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = true }, modifier = modifier
    ) {
        Text(text = stringResource(R.string.delete_note))
    }
}


@Composable
fun DeleteButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.isConfirmDeleteLocalNoteDialogOpen.value = true
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
            viewModel.isEditEnabled.value = true
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