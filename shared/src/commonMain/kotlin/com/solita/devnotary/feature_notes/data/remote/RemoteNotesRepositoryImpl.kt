package com.solita.devnotary.feature_notes.data.remote

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.Constants.NOTE_ID
import com.solita.devnotary.Constants.OWNER_USER_ID
import com.solita.devnotary.Constants.SHARED_NOTES_FIREBASE
import com.solita.devnotary.Constants.SHARED_NOTES_REF_FIREBASE
import com.solita.devnotary.Constants.SHARED_USER_ID
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.model.SharedNoteRef
import com.solita.devnotary.feature_notes.domain.repository.RemoteNotesRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.ServerTimestampBehavior
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import org.kodein.di.instance

class RemoteNotesRepositoryImpl : RemoteNotesRepository {

    private val auth: FirebaseAuth by di.instance()
    private val sharedNotes: CollectionReference by di.instance(tag = SHARED_NOTES_FIREBASE)
    private val sharedNotesRefs: CollectionReference by di.instance(tag = SHARED_NOTES_REF_FIREBASE)

    private val userId get() = auth.currentUser?.uid

    override suspend fun getSharedNotes() = channelFlow {
        if (userId == null) send(Response.Error("User is not logged in"))
        else {
            try {
                send(Response.Loading)
                val fetchedSharedNotesRefs = sharedNotesRefs.where(SHARED_USER_ID, userId)
                    .get().documents.map { it.data<SharedNoteRef>(ServerTimestampBehavior.NONE) }
                val fetchedSharedNotes = fetchedSharedNotesRefs.map { sharedNoteRef ->
                    sharedNotes
                        .where(NOTE_ID, sharedNoteRef.noteId)
                        .where(OWNER_USER_ID, sharedNoteRef.ownerUserId).get().documents.first()
                        .data<SharedNote>()
                }
                send(Response.Success(fetchedSharedNotes))
            } catch (e: Exception) {
                send(Response.Error(e.message ?: ERROR_MESSAGE))
            }
        }
    }

    override suspend fun shareNote(sharedUserId: String, note: Note): Flow<Response<Boolean>> =
        channelFlow {
            try {
                send(Response.Loading)
                val sharedNotesSnapshot = sharedNotes
                    .where(OWNER_USER_ID, auth.currentUser!!.uid)
                    .where(NOTE_ID, note.noteId).get()
                val fetchedSharedNotes =
                    sharedNotesSnapshot.documents.map { it.data<SharedNote>(ServerTimestampBehavior.NONE) }
                if (fetchedSharedNotes.isEmpty()) {
                    val sharedNote = SharedNote(
                        note.noteId, userId!!, note.title, note.content,
                        Clock.System.now().toString(), note.color
                    )
                    sharedNotes.document.set(sharedNote)
                }
                val sharedNoteRef = SharedNoteRef(note.noteId, userId!!, sharedUserId)
                sharedNotesRefs.document.set(sharedNoteRef)
                send(Response.Success(true))

            } catch (e: Exception) {
                send(Response.Error(e.message ?: ERROR_MESSAGE))
            }
        }

    override suspend fun deleteSharedNote(noteId: String): Flow<Response<Boolean>> = channelFlow {
        try {
            send(Response.Loading)
            deleteDocuments(sharedNotesRefs, noteId)
            deleteDocuments(sharedNotes, noteId)
            send(Response.Success(true))
        } catch (e: Exception) {
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    private suspend fun deleteDocuments(reference: CollectionReference, noteId: String) {
        reference
            .where(NOTE_ID, noteId)
            .where(OWNER_USER_ID, userId)
            .get().documents.forEach { doc ->
                reference.document(doc.id).delete()
            }
    }

    override suspend fun unshareNote(
        sharedUserId: String,
        noteId: String
    ): Flow<Response<Boolean>> = channelFlow {
        try {
            send(Response.Loading)
            sharedNotesRefs
                .where(SHARED_USER_ID, sharedUserId)
                .where(NOTE_ID, noteId).get().documents.forEach {
                    sharedNotesRefs.document(it.id).delete()
                }
            send(Response.Success(true))
        } catch (e: Exception) {
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun editSharedNote(noteId: String, newNote: Note): Flow<Response<Boolean>> =
        channelFlow {
            try {
                send(Response.Loading)
                val fetchedSharedNotes = sharedNotes
                    .where(NOTE_ID, noteId)
                    .where(OWNER_USER_ID, userId)
                    .get().documents
                if (fetchedSharedNotes.isEmpty()) throw Exception("Didn't find anything with that ID")
                fetchedSharedNotes.forEach { document ->
                    val oldDoc = document.data<SharedNote>(ServerTimestampBehavior.NONE)
                    val newDoc = SharedNote(
                        noteId,
                        oldDoc.ownerUserId,
                        newNote.title,
                        newNote.content,
                        oldDoc.sharedDate,
                        newNote.color
                    )
                    sharedNotes.document(document.id).set(newDoc)
                }
                send(Response.Success(true))
            } catch (e: Exception) {
                send(Response.Error(e.message ?: ERROR_MESSAGE))
            }
        }

}