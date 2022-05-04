package com.solita.devnotary.feature_auth.domain.use_case

import com.solita.devnotary.feature_auth.domain.repository.AuthRepository

class SendEmailLink(private val repository : AuthRepository) {

    suspend operator fun invoke(email: String) = repository.sendEmailLink(email)

}