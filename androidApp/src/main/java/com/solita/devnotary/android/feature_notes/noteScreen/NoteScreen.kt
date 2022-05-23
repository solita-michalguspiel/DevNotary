package com.solita.devnotary.android.feature_notes

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.feature_notes.noteScreen.components.ConfirmDeleteDialog
import com.solita.devnotary.android.feature_notes.noteScreen.components.ShareNoteDialog
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.LocalNoteEditContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.NewNoteContent
import com.solita.devnotary.android.feature_notes.noteScreen.contents.SharedNoteContent
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.navigation.navigateToNoteScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreen(
    navController: NavController,
    note: Note?
) {
    val viewModel: NotesViewModel by di.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val noteModification = viewModel.noteModificationStatus.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.prepareNoteScreen(note)
    }

    LaunchedEffect(noteModification) {
        if (noteModification is Response.Error) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(noteModification.message)
                viewModel.resetNoteModificationStatus()
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState, contentColor = MaterialTheme.colors.primary) {
        when (noteModification) {
            is Response.Success<Operation> -> {
                when (noteModification.data) {
                    is Operation.Edit -> {
                        navController.popBackStack(
                            Screen.NotesListScreen.route,
                            false
                        )
                    }
                    is Operation.Delete -> navController.popBackStack(
                        Screen.NotesListScreen.route,
                        false
                    )
                    is Operation.Add -> navController.navigateToNoteScreen(noteModification.data.note)
                    is Operation.Share -> {}
                }
                viewModel.resetNoteModificationStatus()
            }
            else -> {}
        }

        if (viewModel.isConfirmDeleteLocalNoteDialogOpen.collectAsState().value) {
            ConfirmDeleteDialog({viewModel.deleteNoteAndCloseDialog()},{viewModel.isConfirmDeleteLocalNoteDialogOpen.value = false})
        }

        if(viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.collectAsState().value){
            ConfirmDeleteDialog(deleteNote = { viewModel.deleteOwnAccessFromSharedNote() }) {viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = false
            }
        }

        if (viewModel.isShareDialogOpen.collectAsState().value) {
            ShareNoteDialog()
        }

        when {
            note == null -> NewNoteContent()
            note.ownerUserId != null -> SharedNoteContent { navController.popBackStack() }
            else -> {
                if (viewModel.isEditEnabled.collectAsState().value) LocalNoteEditContent {
                    navController.navigateToNoteScreen()
                }
                else LocalNoteContent({ navController.navigate(Screen.UsersWithAccessScreen.route) },
                    { navController.navigateToNoteScreen() })
            }
        }
    }
}