package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.ByDate
import com.solita.devnotary.feature_notes.presentation.ByName
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import com.solita.devnotary.feature_notes.presentation.Order
import org.kodein.di.instance

@Composable
fun LocalNotesContent(paddingValues: PaddingValues, navController: NavController) {
    val viewModel : NotesViewModel by androidDi.instance()

    val localNotesState = viewModel.localNotes.collectAsState()
    val remoteNotesState = viewModel.sharedNotes.collectAsState()
    viewModel.joinNoteLists(localNotesState.value,
        remoteNotesState.value)
    val lazyListState  = rememberLazyListState()
    if(lazyListState.isScrollingUp()) viewModel.showFab() else viewModel.hideFab()
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(Modifier.fillMaxSize(), state = lazyListState) {
            items(viewModel.notes.value)
                 {
                NotePreview(note = it,navController)
            }
        }
    }
}
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
