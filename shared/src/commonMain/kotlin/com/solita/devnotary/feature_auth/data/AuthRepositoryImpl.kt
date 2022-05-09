package com.solita.devnotary.feature_auth.data

import com.solita.devnotary.Constants.ANDROID_PACKAGE_NAME
import com.solita.devnotary.Constants.APP_URL
import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.Constants.USER_FIREBASE_REFERENCE
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.AndroidPackageName
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.ServerTimestampBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.kodein.di.instance

class AuthRepositoryImpl : AuthRepository {

    private val auth: FirebaseAuth by di.instance()
    private val usersReference: CollectionReference by di.instance(tag = USER_FIREBASE_REFERENCE)

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun getCurrentUserDocument(): Flow<Response<User>> = channelFlow {
        try {
            if (!isUserAuthenticated()) {
                send(Response.Empty)
            } else {
                val userId = getCurrentUserId()!!
                usersReference.document(userId).snapshots.collectLatest { snap ->
                    val user = snap.data<User>(ServerTimestampBehavior.NONE)
                    send(Response.Success(user))
                }
            }
        } catch (e: Exception) {
            send(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun sendEmailLink(email: String): Flow<Response<Boolean>> = flow {
        val actionCodeSettings = ActionCodeSettings(
            url = APP_URL,
            canHandleCodeInApp = true,
            androidPackageName = AndroidPackageName(
                packageName = ANDROID_PACKAGE_NAME,
                installIfNotAvailable = true
            ),
        )
        try {
            emit(Response.Loading)
            auth.sendSignInLinkToEmail(email, actionCodeSettings)
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun signInWithEmailLink(
        email: String,
        emailLink: String
    ): Flow<Response<Boolean>> = channelFlow {
        if (!auth.isSignInWithEmailLink(emailLink)) {
            send(Response.Error(ERROR_MESSAGE))
        } else {
            try {
                send(Response.Loading)
                val authResult = auth.signInWithEmailLink(email, emailLink)
                createUserDocumentIfNotExist(email, authResult.user!!.uid).collect {
                    send(it)
                }
            } catch (e: Exception) {
                send(Response.Error(e.message ?: ERROR_MESSAGE))
            }
        }
    }


    override suspend fun createUserDocumentIfNotExist(
        email: String,
        uid: String
    ): Flow<Response<Boolean>> = channelFlow {
        val query = usersReference.document(uid)
        try {
            query.snapshots.collectLatest { snap ->
                val userIdRetrievedFromFirebase = snap.get<String?>("userId")
                if (userIdRetrievedFromFirebase == "" || userIdRetrievedFromFirebase == null) {
                    usersReference.document(uid).set(User(uid, email))
                    send(Response.Success(true))
                } else {
                    send(Response.Success(true))
                }
            }
        } catch (e: Exception) {
            val response = (Response.Error(e.message ?: ERROR_MESSAGE))
            send(response)
        }
    }

    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(false))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: ERROR_MESSAGE))
        }

    }

}
