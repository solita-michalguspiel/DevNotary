package com.solita.devnotary.android.feature_notes.localNotesScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.solita.devnotary.database.Note

@Composable
fun LocalNotesContent(notesState: State<List<Note>>, paddingValues: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(notesState.value) {
                NotePreview(note = it)
            }
        }
    }
}