package com.solita.devnotary.feature_auth.data

import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val fakeUser = User("vbdsnojgshnls21412","testEmail@example.com")

class AuthRepositoryTestImpl : AuthRepository {
    var _testIsUserAuthenticated = false

    override fun isUserAuthenticated(): Boolean {
        return _testIsUserAuthenticated
    }

    override fun getCurrentUserId(): String? {
        return if (isUserAuthenticated()) fakeUser.userId else null
    }

    override suspend fun getCurrentUserDocument(): Flow<Response<User>> = flow {
        if (isUserAuthenticated()) emit(Response.Success(fakeUser))
        else emit(Response.Empty)
    }

    override suspend fun sendEmailLink(email: String): Flow<Response<Boolean>> = flow {
        if(email.isBlank()) emit(Response.Error("Given string is empty or null"))
        else emit(Response.Success(true))
    }

    override suspend fun signInWithEmailLink(
        email: String,
        emailLink: String
    ): Flow<Response<Boolean>> = flow {
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