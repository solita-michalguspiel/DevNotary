package com.solita.devnotary.di

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.USER_FIREBASE_REFERENCE
import com.solita.devnotary.feature_auth.data.AuthRepositoryImpl
import com.solita.devnotary.feature_auth.domain.use_case.*
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.*

val di = DI {
    val firebaseFirestore = Firebase.firestore

    bindSingleton {
        Firebase.auth
    }

    bindSingleton {
        firebaseFirestore
    }

    bindSingleton { AuthRepositoryImpl() }

    bindSingleton {
        AuthUseCases(
            getCurrentUserDocument = GetCurrentUserDocument(instance()),
            sendEmailLink = SendEmailLink(instance()),
            signOut = SignOut(instance()),
            isUserAuthenticated = IsUserAuthenticated(instance()),
            signInWithEmailLink = SignInWithEmailLink(instance())
        )
    }

    bindSingleton { Settings() }

    bindConstant(USER_FIREBASE_REFERENCE) { firebaseFirestore.collection("users") }
}
