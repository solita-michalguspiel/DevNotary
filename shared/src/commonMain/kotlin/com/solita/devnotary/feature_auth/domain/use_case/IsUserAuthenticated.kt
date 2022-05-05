package com.solita.devnotary.feature_auth.domain.use_case

import com.solita.devnotary.feature_auth.domain.repository.AuthRepository

class IsUserAuthenticated(private val repository : AuthRepository) {
    operator fun invoke() = repository.isUserAuthenticated()
}