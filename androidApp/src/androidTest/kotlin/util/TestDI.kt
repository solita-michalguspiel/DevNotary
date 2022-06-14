package util

import com.solita.devnotary.di.DiConstants
import com.solita.devnotary.feature_auth.data.AuthRepositoryTestImpl
import com.solita.devnotary.feature_auth.domain.use_case.*
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.data.local.LocalNotesRepositoryTestImpl
import com.solita.devnotary.feature_notes.data.remote.RemoteNotesRepositoryTestImpl
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object TestDI {
    val diAuthFeatTestModule = DI.Module("auth_feat_test_module"){
        bindSingleton(DiConstants.AUTH_TAG) { AuthRepositoryTestImpl() }
        bindSingleton {
            AuthUseCases(
                getCurrentUserDocument = GetCurrentUserDocument(instance<AuthRepositoryTestImpl>(
                    DiConstants.AUTH_TAG
                )),
                sendEmailLink = SendEmailLink(instance<AuthRepositoryTestImpl>(DiConstants.AUTH_TAG)),
                signOut = SignOut(instance<AuthRepositoryTestImpl>(DiConstants.AUTH_TAG)),
                isUserAuthenticated = IsUserAuthenticated(instance<AuthRepositoryTestImpl>(
                    DiConstants.AUTH_TAG)),
                signInWithEmailLink = SignInWithEmailLink(instance<AuthRepositoryTestImpl>(
                    DiConstants.AUTH_TAG))
            )
        }
        bindSingleton { AuthViewModel() }
    }

    val diNotesFeatTestModule = DI.Module("notes_feat_test_module"){
        bindSingleton { LocalNotesRepositoryTestImpl() }
        bindSingleton { RemoteNotesRepositoryTestImpl() }

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
        bindSingleton { NotesListViewModel() }
        bindSingleton { NoteDetailViewModel()}

    }


}