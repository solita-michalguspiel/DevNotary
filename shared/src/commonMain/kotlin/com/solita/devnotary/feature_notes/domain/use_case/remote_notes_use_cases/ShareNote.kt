package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository

class ShareNote(private val repository: RemoteNotesRepository) {
    suspend operator fun invoke(sharedUserId : String,note: Note) = repository.shareNote(sharedUserId,note)
}