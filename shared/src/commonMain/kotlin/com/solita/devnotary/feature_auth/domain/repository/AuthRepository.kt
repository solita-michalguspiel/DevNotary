package com.solita.devnotary.feature_auth.domain.repository

import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    fun isUserAuthenticated(): Boolean

    fun getCurrentUserId() : String?

    suspend fun getCurrentUserDocument() : Flow<Response<User>>

    suspend fun sendEmailLink(email: String) : Flow<Response<Boolean>>

    suspend fun signInWithEmailLink(email: String, emailLink:String) : Flow<Response<Boolean>>

    suspend fun createUserDocumentIfNotExist(email: String, uid: String) : Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>

}