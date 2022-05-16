package com.solita.devnotary.feature_notes.domain.repository

import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    suspend fun getUsers(): Flow<Response<List<User>>>

    suspend fun getUsersWithAccess( noteId: String) : Flow<Response<List<User>>>

}