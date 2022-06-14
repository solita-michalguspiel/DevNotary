package com.solita.devnotary.di

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.SHARED_NOTES_FIREBASE
import com.solita.devnotary.Constants.SHARED_NOTES_REF_FIREBASE
import com.solita.devnotary.Constants.USERS_FIREBASE
import com.solita.devnotary.dev_notary_db
import com.solita.devnotary.di.DiConstants.AUTH_TAG
import com.solita.devnotary.di.DiConstants.LOCAL_NOTES_TAG
import com.solita.devnotary.di.DiConstants.REMOTE_NOTES_TAG
import com.solita.devnotary.di.DiConstants.USERS_TAG
import com.solita.devnotary.feature_auth.data.AuthRepositoryImpl
import com.solita.devnotary.feature_auth.domain.use_case.*
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.data.local.LocalNotesRepositoryImpl
import com.solita.devnotary.feature_notes.data.local.getSqlDriver
import com.solita.devnotary.feature_notes.data.remote.RemoteNotesRepositoryImpl
import com.solita.devnotary.feature_notes.data.remote.UsersRepositoryImpl
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.GetUser
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.GetUsers
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.GetUsersWithAccess
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import com.solita.devnotary.utils.dbArgs
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.DI
import org.kodein.di.bindConstant
import org.kodein.di.bindSingleton
import org.kodein.di.instance


object DiConstants{
    const val AUTH_TAG = "auth"
    const val LOCAL_NOTES_TAG = "local_notes"
    const val REMOTE_NOTES_TAG = "remote_notes"
    const val USERS_TAG = "users"
}

var di = DI {
    /**Firebase*/
    val firebaseFirestore = Firebase.firestore

    bindConstant(USERS_FIREBASE) { firebaseFirestore.collection("users") }

    bindConstant(SHARED_NOTES_FIREBASE){firebaseFirestore.collection("shared_notes")}
    bindConstant(SHARED_NOTES_REF_FIREBASE){firebaseFirestore.collection("shared_notes_ref")}

    bindSingleton {
        Firebase.auth
    }

    bindSingleton {
        firebaseFirestore
    }

    /**Repositories*/
    bindSingleton(AUTH_TAG) { AuthRepositoryImpl() }

    bindSingleton(LOCAL_NOTES_TAG) { LocalNotesRepositoryImpl(instance()) }

    bindSingleton(REMOTE_NOTES_TAG) { RemoteNotesRepositoryImpl() }

    bindSingleton(USERS_TAG) { UsersRepositoryImpl() }

    /**UseCases*/

    bindSingleton {
        AuthUseCases(
            getCurrentUserDocument = GetCurrentUserDocument(instance<AuthRepositoryImpl>(AUTH_TAG)),
            sendEmailLink = SendEmailLink(instance<AuthRepositoryImpl>(AUTH_TAG)),
            signOut = SignOut(instance<AuthRepositoryImpl>(AUTH_TAG)),
            isUserAuthenticated = IsUserAuthenticated(instance<AuthRepositoryImpl>(AUTH_TAG)),
            signInWithEmailLink = SignInWithEmailLink(instance<AuthRepositoryImpl>(AUTH_TAG))
        )
    }

    bindSingleton {
        LocalNotesUseCases(
            addNote = AddNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            deleteNote = DeleteNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            editNote = EditNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            getNotes = GetNotes(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG))
        )
    }
    bindSingleton {
        RemoteNotesUseCases(
            shareNote = ShareNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            unshareNote = UnshareNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            deleteSharedNote = DeleteSharedNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            getSharedNotes = GetSharedNotes(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            editSharedNote = EditSharedNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG))
        )
    }

    bindSingleton {
        UsersUseCases(
            getUsers = GetUsers(instance<UsersRepositoryImpl>(USERS_TAG)),
            getUsersWithAccess = GetUsersWithAccess(instance<UsersRepositoryImpl>(USERS_TAG)),
            getUser = GetUser(instance<UsersRepositoryImpl>(USERS_TAG))
        )
    }

    /**Settings*/
    bindSingleton { Settings() }

    /**Database*/

    bindSingleton {
        println("Trying to bind driver!")
        if(dbArgs != null)getSqlDriver(dbArgs)
        else getSqlDriver(null)

    }

    bindSingleton { dev_notary_db(instance()) }


    /**ViewModels*/
    bindSingleton { NoteDetailViewModel() }

    bindSingleton { AuthViewModel() }

    bindSingleton { NotesListViewModel()}

}

val diCommonModule = DI.Module("di_common_module"){
    repeat(100) {
        println("DI COMMON MODULE INITIALIZED !!!")
    }
    val firebaseFirestore = Firebase.firestore
    bindConstant(USERS_FIREBASE) { firebaseFirestore.collection("users") }
    bindConstant(SHARED_NOTES_FIREBASE){firebaseFirestore.collection("shared_notes")}
    bindConstant(SHARED_NOTES_REF_FIREBASE){firebaseFirestore.collection("shared_notes_ref")}
    bindSingleton {
        firebaseFirestore
    }
}

val diUsersFeatModule = DI.Module("users_feat_module"){
    bindSingleton(USERS_TAG) { UsersRepositoryImpl() }
    bindSingleton {
        UsersUseCases(
            getUsers = GetUsers(instance<UsersRepositoryImpl>(USERS_TAG)),
            getUsersWithAccess = GetUsersWithAccess(instance<UsersRepositoryImpl>(USERS_TAG)),
            getUser = GetUser(instance<UsersRepositoryImpl>(USERS_TAG))
        )
    }
}

val diAuthFeatModule = DI.Module("auth_feat_module"){
    bindSingleton {
        Firebase.auth
    }
    bindSingleton(AUTH_TAG) { AuthRepositoryImpl() }

    bindSingleton {
        AuthUseCases(
            getCurrentUserDocument = GetCurrentUserDocument(instance<AuthRepositoryImpl>(AUTH_TAG)),
            sendEmailLink = SendEmailLink(instance<AuthRepositoryImpl>(AUTH_TAG)),
            signOut = SignOut(instance<AuthRepositoryImpl>(AUTH_TAG)),
            isUserAuthenticated = IsUserAuthenticated(instance<AuthRepositoryImpl>(AUTH_TAG)),
            signInWithEmailLink = SignInWithEmailLink(instance<AuthRepositoryImpl>(AUTH_TAG))
        )
    }
    bindSingleton { AuthViewModel() }
}

val diNotesFeatModule = DI.Module("notes_feat_module"){
    bindSingleton(LOCAL_NOTES_TAG) { LocalNotesRepositoryImpl(instance()) }

    bindSingleton(REMOTE_NOTES_TAG) { RemoteNotesRepositoryImpl() }

    bindSingleton {
        LocalNotesUseCases(
            addNote = AddNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            deleteNote = DeleteNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            editNote = EditNote(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG)),
            getNotes = GetNotes(instance<LocalNotesRepositoryImpl>(LOCAL_NOTES_TAG))
        )
    }
    bindSingleton {
        RemoteNotesUseCases(
            shareNote = ShareNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            unshareNote = UnshareNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            deleteSharedNote = DeleteSharedNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            getSharedNotes = GetSharedNotes(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG)),
            editSharedNote = EditSharedNote(instance<RemoteNotesRepositoryImpl>(REMOTE_NOTES_TAG))
        )
    }
    bindSingleton { NoteDetailViewModel() }
    bindSingleton { NotesListViewModel()}
}

val diDatabaseModule = DI.Module("di_database"){
    /**Database*/
    bindSingleton {
        if(dbArgs != null) getSqlDriver(dbArgs)
        else getSqlDriver(null)
    }
    bindSingleton { dev_notary_db(instance()) }
    /**Settings*/
    bindSingleton { Settings() }
}
