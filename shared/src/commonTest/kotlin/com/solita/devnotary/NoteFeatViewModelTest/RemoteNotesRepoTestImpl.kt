package com.solita.devnotary.NoteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class RemoteNotesRepoTestImpl : RemoteNotesRepository {


    val appUser1 = "te156262nsd"
    val appUser2 = "x12jfghj262nsd"
    val appUser3 = "pifa+421knn1ofa2325ad5sa1s12"

    var currentUserId : String? = appUser3

    private val sharedNotesList = mutableListOf<SharedNote>()

    private val userId get() = currentUserId

    override suspend fun getSharedNotes() = channelFlow {
        if (userId == null)send(Response.Error("User is not logged in"))
        else{
            try {
                send(Response.Loading)
                val filteredListForCurrentUser = sharedNotesList.filter { it.sharedUserId == currentUserId }
                send(Response.Success(filteredListForCurrentUser))
            } catch (e: Exception) {
                send(Response.Error(e.message ?: ERROR_MESSAGE))
            }
        }
    }

    override suspend fun shareNote(sharedUserId: String, note: Note): Flow<Response<Boolean>> = flow{
        try {
            emit(Response.Loading)
            val sharedNote = SharedNote(note.note_id,userId!!,sharedUserId,note.title,note.content,"TODAY",note.color)
            sharedNotesList.add(sharedNote)
            emit(Response.Success(true))
        }catch (e:Exception){
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun deleteSharedNote(noteId: String): Flow<Response<Boolean>> = channelFlow{
        try  {
            send(Response.Loading)

        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun unshareNote(sharedUserId: String,noteId: String): Flow<Response<Boolean>> = channelFlow {
        try {
            send(Response.Loading)

        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun editSharedNote(noteId: String, newNote: Note): Flow<Response<Boolean>> = channelFlow {
        try {
            send(Response.Loading)
        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

}