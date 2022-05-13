package com.solita.devnotary.android.feature_notes

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.Dialog
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.feature_notes._sharedUtils.showAddNewNoteScaffold
import com.solita.devnotary.android.feature_notes._sharedUtils.showScaffold
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteEditContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.NewNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.SharedNoteContent
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun NoteScreen(
    navController: NavController,
    noteId: String? = null,
    noteTitle: String? = null,
    noteContent: String? = null,
    noteDateTime: String? = null,
    noteColor: String? = null,
) {

    val viewModel: NotesViewModel by androidDi.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.fillContent(noteId, noteTitle, noteContent, noteDateTime, noteColor)
    }

    Scaffold(scaffoldState = scaffoldState) {
        when (val noteModification = viewModel.noteModificationStatus.collectAsState().value) {
            is Response.Success<Operation> -> {
                when(noteModification.data){
                    is Operation.Edit -> navController.popBackStack(Screen.NotesListScreen.route,false)
                    is Operation.Delete -> navController.popBackStack(Screen.NotesListScreen.route, false)
                    is Operation.Add -> {}
                    is Operation.Share -> {}
                }
            }
            is Response.Error -> {
                scaffoldState.showScaffold(noteModification.message, coroutineScope)
                viewModel.resetNoteModificationStatus()
            }
            else -> {}
        }

        if (viewModel.isConfirmDeleteDialogOpen.collectAsState().value) {
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


        when (viewModel.noteScreenState.collectAsState().value) {
            NoteScreenState.NewNote -> {
                NewNoteContent()
            }
            NoteScreenState.LocalNote -> {
                LocalNoteContent()
            }
            NoteScreenState.LocalNoteEdit -> {
                LocalNoteEditContent()
            }
            NoteScreenState.SharedNote -> {
                SharedNoteContent()
            }
            else -> {}
        }
    }
}
