package com.solita.devnotary.feature_auth.domain.use_case

import com.solita.devnotary.feature_auth.domain.repository.AuthRepository

class SignInWithEmailLink(private val repository : AuthRepository) {
    suspend operator fun invoke(email: String, intent:String) = repository.signInWithEmailLink(email, intent)
}