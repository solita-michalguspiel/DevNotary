package com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository

class DeleteNote(private val repository: LocalNotesRepository) {
    suspend operator fun invoke(noteId: String) = repository.deleteNote(noteId)
}