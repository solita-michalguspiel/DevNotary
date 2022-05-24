package com.solita.devnotary.feature_auth.domain.use_case

import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SendEmailLink(private val repository : AuthRepository) {
    suspend operator fun invoke(email: String): Flow<Response<Boolean>> = repository.sendEmailLink(email)

}