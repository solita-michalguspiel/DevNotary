package com.solita.devnotary.feature_notes.data.remote

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.Constants.SHARED_NOTES_FIREBASE_REFERENCE
import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.ShareNote
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.ServerTimestampBehavior
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import org.kodein.di.instance

class RemoteNotesRepositoryImpl : RemoteNotesRepository {

    private val auth : FirebaseAuth by di.instance()
    private val sharedNotesReference: CollectionReference by di.instance(tag = SHARED_NOTES_FIREBASE_REFERENCE)

    private val userId get() = auth.currentUser?.uid

    override suspend fun getSharedNotes() = channelFlow {
        if (userId == null)send(Response.Error("User is not logged in"))
        else{
        try {
            send(Response.Loading)
            sharedNotesReference.where("sharedUserId", userId).snapshots.collect { snap ->
                val result = snap.documents.map { it.data<SharedNote>(ServerTimestampBehavior.NONE) }
                send(Response.Success(result))
            }
        } catch (e: Exception) {
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }
    }

    override suspend fun shareNote(sharedUserId: String, note: Note): Flow<Response<Boolean>> = flow{
    try {
        emit(Response.Loading)
        println("User ID: $userId")
        val sharedNote = SharedNote(note.note_id,userId!!,sharedUserId,note.title,note.content,
            Clock.System.now().toString(),note.color)
        sharedNotesReference.document.set(sharedNote)
        emit(Response.Success(true))
    }catch (e:Exception){
        emit(Response.Error(e.message ?: ERROR_MESSAGE))
    }
    }

    override suspend fun deleteSharedNote(noteId: String): Flow<Response<Boolean>> = channelFlow{
        try  {
            send(Response.Loading)
            sharedNotesReference.where("noteId",noteId).snapshots.collectLatest{ snap ->
                snap.documents.forEach { doc ->
                    sharedNotesReference.document(doc.id).delete()
                }
                send(Response.Success(true))
            }
        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun unshareNote(sharedUserId: String,noteId: String): Flow<Response<Boolean>> = channelFlow {
        try {
        send(Response.Loading)
        sharedNotesReference
            .where("sharedUserId",sharedUserId)
            .where("noteId",noteId).snapshots.collectLatest { snap ->
                sharedNotesReference.document(snap.documents.first().id).delete()
                send(Response.Success(true))
            }
        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun editSharedNote(noteId: String, newNote: Note): Flow<Response<Boolean>> = channelFlow {
        try {
        send(Response.Loading)
            sharedNotesReference.where("noteId",noteId).snapshots.collectLatest{ snap ->
                if(snap.documents.isEmpty()) throw Exception("Didn't find anything with that ID")
                snap.documents.forEach { doc ->
                    val oldDoc =  doc.data<SharedNote>(ServerTimestampBehavior.NONE)
                    val newDoc = SharedNote(noteId,oldDoc.ownerUserId,oldDoc.sharedUserId,newNote.title,newNote.content,oldDoc.sharedDate,newNote.color)
                    sharedNotesReference.document(doc.id).set(newDoc)
                }
                send(Response.Success(true))
            }
        }catch (e:Exception){
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

}