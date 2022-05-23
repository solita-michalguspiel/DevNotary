package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.composables.TextIndicatingError
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
 fun ShareNoteDialog() {
    val viewModel: NotesViewModel by di.instance()
    AlertDialog(
        onDismissRequest = {
            viewModel.closeShareDialog()
            viewModel.restartNoteSharingState()
                           },
        title = {
            Text(
                text = stringResource(id = R.string.share_note),
                modifier = Modifier.padding(bottom = LocalSpacing.current.xLarge),
                style = Typography.body1
            )
        },
        text = {
            Column {
                TextField(
                    value = viewModel.anotherUserEmailAddress.collectAsState().value,
                    onValueChange = { viewModel.anotherUserEmailAddress.value = it },
                    label =
                    {
                        Text(
                            text = stringResource(R.string.user_email_address),
                            style = Typography.caption
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                    ),
                    textStyle = Typography.body1.copy(color = Color.Black),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                when (val response = viewModel.noteSharingState.collectAsState().value) {
                    is Response.Loading -> ProgressIndicator()
                    is Response.Error -> TextIndicatingError(
                        errorMessage = response.message,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    is Response.Success -> {
                        Text(
                            text = stringResource(id = R.string.note_shared_success),
                            style = Typography.body2,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                    is Response.Empty -> {}
                }
            }
        },
        confirmButton = {
            Button(onClick = { viewModel.shareNote() }) {
                Text(text = stringResource(id = R.string.share))
            }
        },
    )
}