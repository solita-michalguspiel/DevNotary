package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository

class SaveSharedNote(private val repository: RemoteNotesRepository) {
    suspend operator fun invoke(noteId: String) = repository.saveSharedNote(noteId)
}