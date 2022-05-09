package com.solita.devnotary.di

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.SHARED_NOTES_FIREBASE_REFERENCE
import com.solita.devnotary.Constants.USER_FIREBASE_REFERENCE
import com.solita.devnotary.dev_notary_db
import com.solita.devnotary.feature_auth.data.AuthRepositoryImpl
import com.solita.devnotary.feature_auth.domain.use_case.*
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.data.local.DbArgs
import com.solita.devnotary.feature_notes.data.local.LocalNotesRepositoryImpl
import com.solita.devnotary.feature_notes.data.local.getSqlDriver
import com.solita.devnotary.feature_notes.data.remote.RemoteNotesRepositoryImpl
import com.solita.devnotary.feature_notes.data.remote.UsersRepositoryImpl
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.GetUsers
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.DI
import org.kodein.di.bindConstant
import org.kodein.di.bindSingleton
import org.kodein.di.instance

lateinit var dbArgs: DbArgs

val di = DI {
    /**Firebase*/
    val firebaseFirestore = Firebase.firestore

    bindConstant(USER_FIREBASE_REFERENCE) { firebaseFirestore.collection("users") }

    bindConstant(SHARED_NOTES_FIREBASE_REFERENCE){firebaseFirestore.collection("shared_notes")}

    bindSingleton {
        Firebase.auth
    }

    bindSingleton {
        firebaseFirestore
    }

    /**Repositories*/
    bindSingleton { AuthRepositoryImpl() }

    bindSingleton { LocalNotesRepositoryImpl(instance()) }

    bindSingleton { RemoteNotesRepositoryImpl() }

    bindSingleton { UsersRepositoryImpl() }

    /**UseCases*/

    bindSingleton {
        AuthUseCases(
            getCurrentUserDocument = GetCurrentUserDocument(instance()),
            sendEmailLink = SendEmailLink(instance()),
            signOut = SignOut(instance()),
            isUserAuthenticated = IsUserAuthenticated(instance()),
            signInWithEmailLink = SignInWithEmailLink(instance())
        )
    }

    bindSingleton {
        LocalNotesUseCases(
            addNote = AddNote(instance()),
            deleteNote = DeleteNote(instance()),
            editNote = EditNote(instance()),
            getNotes = GetNotes(instance())
        )
    }
    bindSingleton {
        RemoteNotesUseCases(
            shareNote = ShareNote(instance()),
            unshareNote = UnshareNote(instance()),
            deleteSharedNote = DeleteSharedNote(instance()),
            getSharedNotes = GetSharedNotes(instance()),
            editSharedNote = EditSharedNote(instance())
        )
    }

    bindSingleton {
        UsersUseCases(
            getUsers = GetUsers(instance())
        )
    }

    /**Settings*/
    bindSingleton { Settings() }

    /**Database*/

    bindSingleton { getSqlDriver(dbArgs) }

    bindSingleton { dev_notary_db(instance()) }

}
