package com.solita.devnotary.android.feature_notes._sharedComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance


@Composable
fun LocalNoteButtons(modifier: Modifier, isEditEnabledState: State<Boolean>) {
    Row(modifier = modifier.padding(LocalSpacing.current.small)) {
        DeleteButton()
        if (isEditEnabledState.value) SaveButton() else EditButton()
    }
}

@Composable
fun SaveButton() {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = { viewModel.editNote() },
        modifier = Modifier.padding(LocalSpacing.current.xSmall)
    ) {
        Text(text = stringResource(id = R.string.save_note))
    }
}

@Composable
fun SaveNoteLocallyButton(modifier: Modifier) {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = { viewModel.addNote() },
        modifier = modifier.padding(LocalSpacing.current.xSmall)
    ) {
        Text(text = stringResource(R.string.save_note_locally))
    }
}

@Composable
fun DeleteButton() {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.deleteNote()
        }, modifier = Modifier
            .padding(
                LocalSpacing.current.xSmall
            )
    ) {
        Text(text = stringResource(id = R.string.delete_note))
    }
}

@Composable
fun EditButton() {
    val viewModel: NotesViewModel by androidDi.instance()
    Button(
        onClick = {
            viewModel.isEditEnabled.value = true
        }, modifier = Modifier
            .padding(
                LocalSpacing.current.xSmall
            )
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
        }, modifier = modifier
            .padding(
                LocalSpacing.current.xSmall
            )
    ) {
        Text(text = stringResource(id = R.string.add_note))
    }
}