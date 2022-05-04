package com.solita.devnotary.di

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.USER_FIREBASE_REFERENCE
import com.solita.devnotary.feature_auth.data.AuthRepositoryImpl
import com.solita.devnotary.feature_auth.domain.use_case.*
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.DI
import org.kodein.di.bindConstant
import org.kodein.di.bindSingleton


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
            getCurrentUserDocument = GetCurrentUserDocument(),
            sendEmailLink = SendEmailLink(),
            signOut = SignOut(),
            isUserAuthenticated = IsUserAuthenticated(),
            signInWithEmailLink = SignInWithEmailLink()
        )
    }

    bindSingleton { Settings() }

    bindConstant(USER_FIREBASE_REFERENCE) { firebaseFirestore.collection("users") }


}