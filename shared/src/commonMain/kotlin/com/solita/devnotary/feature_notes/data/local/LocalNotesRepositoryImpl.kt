package com.solita.devnotary.feature_notes.data.local

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.database.Local_note
import com.solita.devnotary.dev_notary_db
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalNotesRepositoryImpl(private val database : dev_notary_db) : LocalNotesRepository {
    override suspend fun addNote(note: Local_note): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            database.notesQueries.insert(note.note_id,note.title,note.content,note.date_time,note.color)
            emit(Response.Success(Operation.Add(Note(note.note_id,null,note.title,note.content,note.date_time,note.color))))
        }catch (e : Exception){
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun deleteNote(noteId : String): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            database.notesQueries.delete(noteId)
            emit(Response.Success(Operation.Delete()))
        }catch (e : Exception){
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun editNote(newTitle:String,newContent:String,newColor:String,id:String): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            database.notesQueries.update(newTitle,newContent,newColor,id)
            emit(Response.Success(Operation.Edit()))
        }catch (e : Exception){
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override fun getNotes(): Flow<List<Local_note>> {
      return  database.notesQueries.selectAll().asFlow().mapToList()
    }
}