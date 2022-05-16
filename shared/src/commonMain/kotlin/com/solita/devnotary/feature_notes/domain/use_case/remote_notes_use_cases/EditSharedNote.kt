package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository

class EditSharedNote(private val repository: RemoteNotesRepository) {
    suspend operator fun invoke(
        noteId: String, newNoteTitle: String,
        newNoteContent: String,
        newNoteColor: String
    ) = repository.editSharedNote(
        noteId = noteId, newNoteTitle = newNoteTitle,
        newNoteContent = newNoteContent,
        newNoteColor = newNoteColor
    )
}