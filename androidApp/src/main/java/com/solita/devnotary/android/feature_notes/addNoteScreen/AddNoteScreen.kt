package com.solita.devnotary.android.feature_notes

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.ProgressIndicator
import com.solita.devnotary.android.feature_notes.addNoteScreen.components.NoteContent
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.instance

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddNoteScreen() {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val viewModel: NotesViewModel by androidDi.instance()
    Scaffold(scaffoldState = scaffoldState) {
        when (val noteModificationStatus = viewModel.noteModificationStatus.collectAsState().value) {
            is Response.Empty -> {}
            is Response.Loading -> {
                ProgressIndicator()
            }
            is Response.Success -> {
                scaffoldState.showScaffold(stringResource(id = R.string.note_added),scope)
            }
            is Response.Error -> {
                scaffoldState.showScaffold(noteModificationStatus.message,scope)
            }
        }
        NoteContent()
    }
}

fun ScaffoldState.showScaffold(message : String, coroutineScope: CoroutineScope){
    val viewModel: NotesViewModel by androidDi.instance()
    coroutineScope.launch {
        this@showScaffold.snackbarHostState.showSnackbar(message)
    }
    viewModel.resetNoteModificationStatus()
}