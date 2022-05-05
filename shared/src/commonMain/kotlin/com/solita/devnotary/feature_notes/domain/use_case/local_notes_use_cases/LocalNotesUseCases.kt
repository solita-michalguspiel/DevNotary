package com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases

data class LocalNotesUseCases(
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val editNote: EditNote,
    val getNotes: GetNotes
)
