package com.solita.devnotary.feature_notes.data.remote

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository
import kotlinx.coroutines.flow.Flow

class RemoteNotesRepositoryImpl : RemoteNotesRepository {
    override suspend fun getSharedNotes(): Flow<Response<List<Note>>> {
        TODO("Not yet implemented")
    }

    override suspend fun shareNote(sharedUserId: String, note: Note): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSharedNote(noteId: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSharedNote(noteId: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun unshareNote(sharedUserId: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun editSharedNote(noteId: String, newNote: Note): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

}