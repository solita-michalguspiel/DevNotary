package com.solita.devnotary.android.feature_notes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.solita.devnotary.android.feature_notes.addNoteScreen.components.NoteContent
import com.solita.devnotary.android.utils.Constants

@Composable
fun LocalNoteDetailsScreen(
    navController: NavController,
    noteId: String,
    noteTitle: String,
    noteContent: String,
    noteDateTime: String,
    noteColor: String,
) {

    NoteContent(
        noteId = noteId,
        noteTitle = noteTitle,
        noteContent = noteContent,
        noteColor = noteColor,
        noteDateTime = noteDateTime,
        noteType = Constants.LOCAL_NOTE,
        editEnabled = false
    )
}