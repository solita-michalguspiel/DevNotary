package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import kotlinx.coroutines.flow.Flow

interface LocalNotesRepository {

    suspend fun addNote(note: Note): Flow<Response<Boolean>>

    suspend fun deleteNote(noteId: String): Flow<Response<Boolean>>

    suspend fun editNote(note: Note): Flow<Response<Boolean>>

    suspend fun getNotes():Flow<Response<List<Note>>>

}