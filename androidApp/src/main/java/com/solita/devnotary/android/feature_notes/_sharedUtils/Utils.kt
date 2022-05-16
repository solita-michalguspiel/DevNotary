package com.solita.devnotary.android.feature_notes._sharedUtils

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.instance

fun ScaffoldState.showScaffold(message : String, coroutineScope: CoroutineScope){
    val viewModel: NotesViewModel by androidDi.instance()
    coroutineScope.launch {
        this@showScaffold.snackbarHostState.showSnackbar(message)
    }
    viewModel.resetNoteModificationStatus()
}
