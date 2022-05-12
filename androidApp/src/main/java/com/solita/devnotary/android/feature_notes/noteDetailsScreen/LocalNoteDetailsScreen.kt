package com.solita.devnotary.android.feature_notes

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.feature_notes._sharedUtils.showScaffold
import com.solita.devnotary.android.feature_notes.addNoteScreen.components.NoteContent
import com.solita.devnotary.android.utils.Constants
import com.solita.devnotary.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNoteDetailsScreen(
    navController: NavController,
    noteId: String,
    noteTitle: String,
    noteContent: String,
    noteDateTime: String,
    noteColor: String,
) {


    val viewModel: NotesViewModel by androidDi.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        when (val noteModification = viewModel.noteModificationStatus.collectAsState().value) {
            is Response.Success<Operation> -> {
                if (noteModification.data is Operation.Edit) scaffoldState.showScaffold(
                    noteModification.data.message,
                    coroutineScope
                )
                if (noteModification.data is Operation.Delete) navController.popBackStack()
            }
            else -> {}
        }

        if (viewModel.isConfirmDeleteDialogOpen.collectAsState().value) {
            AlertDialog(
                onDismissRequest = { viewModel.isConfirmDeleteDialogOpen.value = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.isConfirmDeleteDialogOpen.value = false
                        viewModel.deleteNote()
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

        NoteContent(
            noteId = noteId,
            noteTitle = noteTitle,
            noteContent = noteContent,
            noteColor = noteColor,
            noteDateTime = noteDateTime,
            noteType = Constants.LOCAL_NOTE,
            editEnabled = false
        )


    }


}