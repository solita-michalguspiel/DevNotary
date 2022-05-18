package com.solita.devnotary.feature_notes.data.remote

import com.solita.devnotary.Constants
import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.Constants.NOTE_ID
import com.solita.devnotary.Constants.OWNER_USER_ID
import com.solita.devnotary.Constants.USERS_FIREBASE
import com.solita.devnotary.Constants.USER_ID
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_notes.domain.model.SharedNoteRef
import com.solita.devnotary.feature_notes.domain.repository.UsersRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import org.kodein.di.instance

class UsersRepositoryImpl : UsersRepository {

    private val auth: FirebaseAuth by di.instance()
    private val usersReference: CollectionReference by di.instance(USERS_FIREBASE)
    private val sharedNotesRefsCollectionReference: CollectionReference by di.instance(tag = Constants.SHARED_NOTES_REF_FIREBASE)

    private val userId get() = auth.currentUser?.uid

    override suspend fun getUsers() = channelFlow {
        try {
            send(Response.Loading)
            usersReference.snapshots.collect { snap ->
                val users = snap.documents.map { it.data<User>() }
                send(Response.Success(users))
            }
        } catch (e: Exception) {
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun getUsersWithAccess(
        noteId: String
    ): Flow<Response<List<User>>> = flow {
        try {
            emit(Response.Loading)
            val sharedNotesRefs = sharedNotesRefsCollectionReference
                .where(NOTE_ID, noteId)
                .where(OWNER_USER_ID, userId)
                .get().documents.map { it.data<SharedNoteRef>() }
            val users = sharedNotesRefs.map { sharedNoteRef ->
                usersReference
                    .where(USER_ID, sharedNoteRef.sharedUserId)
                    .get().documents.map { it.data<User>() }.first()
            }
            emit(Response.Success(users))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun getUser(userId: String): Flow<Response<User>> = flow {
        try {
            emit(Response.Loading)
            val user = usersReference.where(USER_ID, userId).get().documents.first().data<User>()
            emit(Response.Success(user))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }
}