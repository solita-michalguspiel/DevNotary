package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

import com.solita.devnotary.database.Note
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository

class EditSharedNote(private val repository: RemoteNotesRepository) {
    suspend operator fun invoke(noteId: String, newNote : Note) = repository.editSharedNote(noteId = noteId , newNote = newNote )
}