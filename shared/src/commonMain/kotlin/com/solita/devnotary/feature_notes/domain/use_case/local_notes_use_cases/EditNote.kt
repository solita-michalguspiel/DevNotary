package com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository

class EditNote(private val repository: LocalNotesRepository) {
    suspend operator fun invoke(
        newTitle: String,
        newContent: String,
        newColor: String,
        id: String
    ) = repository.editNote(newTitle, newContent, newColor, id)
}