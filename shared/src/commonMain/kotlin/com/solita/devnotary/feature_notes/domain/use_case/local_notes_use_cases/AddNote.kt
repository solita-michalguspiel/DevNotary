package com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases

import com.solita.devnotary.database.Note
import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository

class AddNote(private val repository: LocalNotesRepository) {

    suspend operator fun invoke(
        note: Note
    ) = repository.addNote(note)
}