package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.ShareNote
import kotlinx.coroutines.flow.Flow

interface RemoteNotesRepository {

    suspend fun getSharedNotes() : Flow<Response<List<SharedNote>>>

    suspend fun shareNote(sharedUserId: String , note: Note) : Flow<Response<Boolean>>

    suspend fun deleteSharedNote(noteId: String) : Flow<Response<Boolean>>

    suspend fun unshareNote(sharedUserId: String,noteId: String) : Flow<Response<Boolean>>

    suspend fun editSharedNote(noteId : String ,newNote: Note) : Flow<Response<Boolean>>
}