package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.Dialog
import com.solita.devnotary.feature_notes.presentation.NotesViewModel

@Composable
fun ConfirmDeleteDialog(viewModel: NotesViewModel) {
    Dialog(
        onDismissRequest = { viewModel.isConfirmDeleteDialogOpen.value = false },
        title = stringResource(id = R.string.are_u_sure),
        text = null,
        confirmButton = {
            Button(onClick = {
                viewModel.deleteNoteAndCloseDialog()
            }) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.isConfirmDeleteDialogOpen.value = false }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}