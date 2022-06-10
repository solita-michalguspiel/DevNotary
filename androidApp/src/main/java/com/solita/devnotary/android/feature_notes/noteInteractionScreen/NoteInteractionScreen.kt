package com.solita.devnotary.android.feature_notes.noteScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.solita.devnotary.Constants
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteInteractionScreen.components.AddButton
import com.solita.devnotary.android.feature_notes.noteInteractionScreen.components.SaveButton
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.navigation.navigateToNoteDetailsScreen
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance

@Composable
fun NoteInteractionScreen(
    navController : NavController,
    note: Note?
) {
    val viewModel: NoteDetailViewModel by di.instance()
    val displayedNoteState = viewModel.displayedNote.collectAsState(initial = Constants.CLEAR_NOTE)
    val scaffoldState = rememberScaffoldState()
    val noteModification = viewModel.noteModificationStatus.collectAsState(Response.Empty).value

    LaunchedEffect(Unit) {
        viewModel.prepareNoteScreen(note)
    }

    fun isNoteValid(): Boolean {
        if (displayedNoteState.value.title.isBlank()) {
            viewModel.setNoteModificationError(Constants.NO_TITLE_ERROR)
            return false
        }
        if (displayedNoteState.value.content.isBlank()) {
            viewModel.setNoteModificationError(Constants.BLANK_NOTE_ERROR)
            return false
        }
        return true
    }
    Scaffold(scaffoldState = scaffoldState, contentColor = MaterialTheme.colors.primary) {
        it.toString()
        when (noteModification) {
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
            else -> {}
        }
        Column(Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .weight(1.0f),
                backgroundColor = NoteColor(displayedNoteState.value.color).getColor(),
                elevation = LocalElevation.current.medium
            ) {
                Column {
                    TitleTextField(titleInput = displayedNoteState.value.title,
                        isEditEnabled = true,
                        onValueChange = { if (it.length <= 30) viewModel.changeTitleInput(it) })
                    ContentTextField(
                        contentInput = displayedNoteState.value.content,
                        modifier = Modifier.weight(1.0f),
                        isEditEnabled = true,
                        onValueChange = { viewModel.changeContentInput(it) }
                    )
                    ColorBallsRow(chosenNoteColor = displayedNoteState.value.color) {
                        viewModel.changeNoteColor(it)
                    }
                }
            }
            if (note == null) {
                AddButton(modifier = Modifier.align(Alignment.End)) {
                    if (isNoteValid()) viewModel.addNote()
                }
            } else {
                SaveButton(modifier = Modifier.align(Alignment.End)) {
                    if (isNoteValid()) viewModel.editNote()
                }
            }
        }
    }
}