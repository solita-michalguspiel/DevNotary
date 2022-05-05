package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import kotlinx.coroutines.flow.Flow

interface RemoteNotesRepository {

    suspend fun getSharedNotes() : Flow<Response<List<Note>>>

    suspend fun shareNote(sharedUserId: String , note: Note) : Flow<Response<Boolean>>

    suspend fun saveSharedNote(noteId: String): Flow<Response<Boolean>>

    suspend fun deleteSharedNote(noteId: String) : Flow<Response<Boolean>>

    suspend fun unshareNote(sharedUserId: String) : Flow<Response<Boolean>>

    suspend fun editSharedNote(noteId : String ,newNote: Note) : Flow<Response<Boolean>>
}