package com.solita.devnotary.feature_notes.data.local

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository
import kotlinx.coroutines.flow.Flow

class LocalNotesRepositoryImpl : LocalNotesRepository {
    override suspend fun addNote(note: Note): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(noteId : String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun editNote(note: Note): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(): Flow<Response<List<Note>>> {
        TODO("Not yet implemented")
    }
}