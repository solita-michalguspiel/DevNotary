package com.solita.devnotary.feature_notes.data.remote

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.Constants.USER_FIREBASE_REFERENCE
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_notes.domain.repository.UsersRepository
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.channelFlow
import org.kodein.di.instance

class UsersRepositoryImpl : UsersRepository {

    private val usersReference: CollectionReference by di.instance(USER_FIREBASE_REFERENCE)

    override suspend fun getUsers() = channelFlow {
    try{
    send(Response.Loading)
        usersReference.snapshots.collect{ snap ->
            val users = snap.documents.map { it.data<User>() }
            send(Response.Success(users))
        }
    }
    catch(e: Exception){
    send(Response.Error(e.message ?: ERROR_MESSAGE))
    }

    }
}