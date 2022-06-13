package com.solita.devnotary.android.feature_notes.noteScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.Constants
import com.solita.devnotary.android.feature_notes.noteInteractionScreen.components.NoteInteractionContent
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.navigation.navigateToNoteDetailsScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@Composable
fun NoteInteractionScreen(
    navController: NavController,
    note: Note?,
    paddingValues: PaddingValues,
    scaffoldState: ScaffoldState
) {
    val viewModel: NoteDetailViewModel by di.instance()
    val displayedNoteState = viewModel.displayedNote.collectAsState(initial = Constants.CLEAR_NOTE)
    val noteModification = viewModel.noteModificationStatus.collectAsState(Response.Empty).value
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.prepareNoteScreen(note)
    }

    LaunchedEffect(noteModification){
        when(noteModification) {
            is Response.Success<Operation> -> {
                when (noteModification.data) {
                    is Operation.Edit -> {
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
            is Response.Error -> {
                coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(noteModification.message)  }
                viewModel.resetNoteModificationStatus()
            }
            else -> {}
        }
    }
    NoteInteractionContent(paddingValues, displayedNoteState, viewModel, note)
}