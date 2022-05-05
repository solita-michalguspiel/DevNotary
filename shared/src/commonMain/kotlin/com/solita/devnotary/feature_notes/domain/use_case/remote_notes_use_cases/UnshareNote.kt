package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository

class UnshareNote(private val repository: RemoteNotesRepository) {
    suspend operator fun invoke(sharedUserId: String) {repository.unshareNote(sharedUserId)}
}