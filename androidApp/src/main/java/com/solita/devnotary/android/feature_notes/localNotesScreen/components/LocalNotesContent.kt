package com.solita.devnotary.android.feature_notes.localNotesScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.solita.devnotary.database.Note

@Composable
fun LocalNotesContent(notesState: State<List<Note>>, paddingValues: PaddingValues,navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(notesState.value) {
                NotePreview(note = it,navController)
            }
        }
    }
}