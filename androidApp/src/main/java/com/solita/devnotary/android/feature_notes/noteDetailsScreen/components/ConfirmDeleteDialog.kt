package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.Dialog
import com.solita.devnotary.android.utils.TestTags

@Composable
fun ConfirmDeleteDialog(deleteNote : () -> Unit,closeDialog: () -> Unit) {
    Dialog(
        onDismissRequest = { closeDialog() },
        title = stringResource(id = R.string.are_u_sure),
        text = null,
        confirmButton = {
            Button(
                modifier = Modifier.testTag(TestTags.CONFIRM_DELETE_BUTTON_TAG),
                onClick = {
                deleteNote()
            }) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(onClick = { closeDialog() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}