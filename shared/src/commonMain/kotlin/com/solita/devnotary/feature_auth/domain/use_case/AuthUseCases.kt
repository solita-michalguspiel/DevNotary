package com.solita.devnotary.feature_auth.domain.use_case

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val sendEmailLink: SendEmailLink,
    val signOut: SignOut,
    val getCurrentUserDocument: GetCurrentUserDocument,
    val signInWithEmailLink: SignInWithEmailLink
)