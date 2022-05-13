package com.solita.devnotary.NoteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.repository.LocalNotesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalNotesRepoTestImpl : LocalNotesRepository {

    private var tempList = mutableListOf<Note>()

    override suspend fun addNote(note: Note): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            tempList.add(note)
            delay(70)//simulate some delay
            emit(Response.Success(Operation.Add()))
        }catch (e : Exception){
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun deleteNote(noteId: String): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            val indexOfRemovedNote = tempList.map { it.note_id }.indexOf(noteId)
            tempList.remove(tempList[indexOfRemovedNote])
            delay(200)
            emit(Response.Success(Operation.Delete()))
        }catch (e : Exception){
            emit(Response.Error(ERROR_MESSAGE))
        }
    }

    override suspend fun editNote(note: Note): Flow<Response<Operation>> = flow{
        try {
            emit(Response.Loading)
            delay(200) // to simulate some delay
            val indexOfChangedNote = tempList.map { it.note_id }.indexOf(note.note_id)
            tempList[indexOfChangedNote] = note
            emit(Response.Success(Operation.Edit()))
        }catch (e : Exception){
            emit(Response.Error(ERROR_MESSAGE))
        }
    }

    override  fun getNotes(): Flow<List<Note>> = flow {
    emit(tempList)
    }
}