package com.solita.devnotary.android.feature_notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.Dialog
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.composables.TextIndicatingError
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteEditContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.NewNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.SharedNoteContent
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreen(
    navController: NavController,
    noteIndex: String?
) {
    val viewModel: NotesViewModel by androidDi.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val noteModification = viewModel.noteModificationStatus.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.prepareNoteScreen(noteIndex)
    }

    LaunchedEffect(noteModification){
        if(noteModification is Response.Error){
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(noteModification.message)
                viewModel.resetNoteModificationStatus()
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        when (noteModification) {
            is Response.Success<Operation> -> {
                when (noteModification.data) {
                    is Operation.Edit -> navController.popBackStack(
                        Screen.NotesListScreen.route,
                        false
                    )
                    is Operation.Delete -> navController.popBackStack(
                        Screen.NotesListScreen.route,
                        false
                    )
                    is Operation.Add -> {}
                    is Operation.Share -> {}
                }
            }
            else -> {}
        }

        if (viewModel.isConfirmDeleteDialogOpen.collectAsState().value) {
            ConfirmDeleteDialog(viewModel)
        }

        if (viewModel.isShareDialogOpen.collectAsState().value) {
            ShareNoteDialog(viewModel)
        }


        when (viewModel.noteScreenState.collectAsState().value) {
            NoteScreenState.NewNote -> {
                viewModel.prepareNoteScreen(null)
                NewNoteContent()
            }
            NoteScreenState.LocalNote -> {
                LocalNoteContent { navController.navigate(Screen.UsersWithAccessScreen.route) }
            }
            NoteScreenState.LocalNoteEdit -> {
                LocalNoteEditContent()
            }
            NoteScreenState.SharedNote -> {
                SharedNoteContent{navController.popBackStack()}
            }
            else -> {}
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(viewModel: NotesViewModel) {
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

@Composable
private fun ShareNoteDialog(viewModel: NotesViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.closeShareDialog() },
        title = {
            Text(
                text = stringResource(id = R.string.share_note),
                modifier = Modifier.padding(bottom = LocalSpacing.current.xLarge), style = Typography.body1
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
                    textStyle = Typography.body1.copy(color = Color.Black),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                when (val response = viewModel.noteSharingState.collectAsState().value) {
                    is Response.Loading -> ProgressIndicator()
                    is Response.Error -> TextIndicatingError(errorMessage = response.message, modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center)
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