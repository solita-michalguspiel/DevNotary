package com.solita.devnotary.feature_auth.domain.use_case

import com.solita.devnotary.di.di
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import org.kodein.di.instance

class SignInWithEmailLink {
    private val repository: AuthRepository by di.instance()
    suspend operator fun invoke(email: String, emailLink:String) = repository.signInWithEmailLink(email, emailLink)
}