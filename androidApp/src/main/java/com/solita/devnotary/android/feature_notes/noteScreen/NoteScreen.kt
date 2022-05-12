package com.solita.devnotary.android.feature_notes

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.feature_notes._sharedUtils.showAddNewNoteScaffold
import com.solita.devnotary.android.feature_notes._sharedUtils.showScaffold
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteEditContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.NewNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.SharedNoteContent
import com.solita.devnotary.domain.NoteScreenState
import com.solita.devnotary.domain.Operation
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
                if (noteModification.data is Operation.Edit) {
                    scaffoldState.showScaffold(
                        noteModification.data.message,
                        coroutineScope
                    )
                } else if (noteModification.data is Operation.Delete) {
                    navController.popBackStack(Screen.LocalNotesScreen.route,false)
                } else if (noteModification.data is Operation.Add) {
                    scaffoldState.showAddNewNoteScaffold(coroutineScope)
                }
            }
            else -> {}
        }

        if (viewModel.isConfirmDeleteDialogOpen.collectAsState().value) {
            AlertDialog(
                onDismissRequest = { viewModel.isConfirmDeleteDialogOpen.value = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteNoteAndCloseDialog()
                    }) {
                        Text(text = "Yes")
                    }
                },
                title = { Text(text = "Are You sure?") },
                dismissButton = {
                    Button(onClick = { viewModel.isConfirmDeleteDialogOpen.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }


        when (val noteScreenState = viewModel.noteScreenState.collectAsState().value) {
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
