package com.solita.devnotary

import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepoTestImpl : AuthRepository {

    private var _testIsUserAuthenticated = false

    override fun isUserAuthenticated(): Boolean {
        return _testIsUserAuthenticated
    }

    override fun getCurrentUserId(): String? {
        return if (isUserAuthenticated()) randomUser.userId else null
    }

    override suspend fun getCurrentUserDocument(): Flow<Response<User>> = flow {
        if (isUserAuthenticated()) emit(Response.Success(randomUser))
        else emit(Response.Empty)
    }

    override suspend fun sendEmailLink(email: String): Flow<Response<Boolean>> = flow {
        emit(Response.Success(true))
    }

    override suspend fun signInWithEmailLink(
        email: String,
        emailLink: String
    ): Flow<Response<Boolean>> =flow {
        _testIsUserAuthenticated = true
       emit(Response.Success(true))
    }

    override suspend fun createUserDocumentIfNotExist(
        email: String,
        uid: String
    ): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        _testIsUserAuthenticated = false
        emit(Response.Success(false))
    }
}