package com.solita.devnotary.android.feature_notes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.solita.devnotary.android.feature_notes.noteScreen.components.ConfirmDeleteDialog
import com.solita.devnotary.android.feature_notes.noteScreen.components.ShareNoteDialog
import com.solita.devnotary.android.feature_notes.noteScreen.contents.NoteDetailsContent
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.navigation.navigateToNoteDetailsScreen
import com.solita.devnotary.android.navigation.navigateToNoteInteractionScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance

@Composable
fun NoteDetailsScreen(
    navController: NavController,
    note: Note,
    paddingValues: PaddingValues,
) {
    val viewModel: NoteDetailViewModel by di.instance()
    val noteModification = viewModel.noteModificationStatus.collectAsState(Response.Empty).value

    LaunchedEffect(Unit) {
        viewModel.prepareNoteScreen(note)
    }

    when (noteModification) {
        is Response.Success<Operation> -> {
            when (noteModification.data) {
                is Operation.Delete -> {
                    println("Operation delete received, popping backstack!")
                    navController.popBackStack(
                        Screen.NotesListScreen.route,
                        false
                    )
                }
                is Operation.Add -> navController.navigateToNoteDetailsScreen(noteModification.data.note!!)
                else -> {}
            }
            viewModel.resetNoteModificationStatus()
        }
        else -> {}
    }

    if (viewModel.isConfirmDeleteLocalNoteDialogOpen.collectAsState().value) {
        ConfirmDeleteDialog({ viewModel.deleteNoteAndCloseDialog() },
            { viewModel.isConfirmDeleteLocalNoteDialogOpen.value = false })
    }

    if (viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.collectAsState().value) {
        ConfirmDeleteDialog(deleteNote = { viewModel.deleteOwnAccessFromSharedNote() }) {
            viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = false
        }
    }
    if (viewModel.isShareDialogOpen.collectAsState().value) {
        ShareNoteDialog()
    }
    NoteDetailsContent(
        note,
        { navController.navigate(Screen.UsersWithAccessScreen.route) },
        { navController.navigateToNoteInteractionScreen() },
        { navController.navigateToNoteInteractionScreen(note) },
        { navController.popBackStack() },
        paddingValues
    )
}