package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.database.Local_note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import kotlinx.coroutines.flow.Flow

interface LocalNotesRepository {

    suspend fun addNote(note: Local_note): Flow<Response<Operation>>

    suspend fun deleteNote(noteId: String): Flow<Response<Operation>>

    suspend fun editNote(note: Local_note): Flow<Response<Operation>>

    fun getNotes():Flow<List<Local_note>>

}