package com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository

class GetNotes(private val repository: LocalNotesRepository) {
    suspend operator fun invoke() = repository.getNotes()
}