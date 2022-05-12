package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Operation
import com.solita.devnotary.domain.Response
import kotlinx.coroutines.flow.Flow

interface LocalNotesRepository {

    suspend fun addNote(note: Note): Flow<Response<Operation>>

    suspend fun deleteNote(noteId: String): Flow<Response<Operation>>

    suspend fun editNote(note: Note): Flow<Response<Operation>>

    fun getNotes():Flow<List<Note>>

}