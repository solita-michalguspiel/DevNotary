package com.solita.devnotary.android.feature_notes.localNotesScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.database.Note
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNotesContent(notesState: State<List<Note>>, paddingValues: PaddingValues,navController: NavController) {
    val viewModel : NotesViewModel by androidDi.instance()
    val lazyListState  = rememberLazyListState()
    if(lazyListState.isScrollingUp()) viewModel.showFab() else viewModel.hideFab()
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(Modifier.fillMaxSize(), state = lazyListState) {
            items(notesState.value) {
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
