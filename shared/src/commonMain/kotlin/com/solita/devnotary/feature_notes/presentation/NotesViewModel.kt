package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.Constants
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.utils.CommonFlow
import com.solita.devnotary.utils.SharedViewModel
import com.solita.devnotary.utils.asCommonFlow
import com.solita.devnotary.utils.formatIso8601ToString
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class NotesViewModel(dependencyInjection: DI = di) : SharedViewModel() {

    private val clearNote = Note("", null, "", "", "", Constants.WHITE_COLOR)
    private val auth: FirebaseAuth by dependencyInjection.instance()
    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()
    private val usersUseCases: UsersUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Operation>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: CommonFlow<Response<Operation>> =
        _noteModificationStatus.asCommonFlow()

    private val _sharedNotesState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val sharedNotesState: StateFlow<Response<Boolean>> = _sharedNotesState

    private val _noteSharingState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteSharingState: StateFlow<Response<Boolean>> = _noteSharingState

    private val _localNotes: MutableStateFlow<List<Note>> = MutableStateFlow(listOf())

    private val _notesSharedByOtherUsers: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val notesSharedByOtherUsers: StateFlow<List<Note>> = _notesSharedByOtherUsers

    private val _notes: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val notes: CommonFlow<List<Note>> = _notes.asCommonFlow()

    private val _usersWithAccess: MutableStateFlow<Response<List<User>>> =
        MutableStateFlow(Response.Empty)
    val usersWithAccess: StateFlow<Response<List<User>>> = _usersWithAccess

    private val _selectedSort: MutableStateFlow<Sort> =
        MutableStateFlow(SortOptions.BY_NAME_ASC.sort)
    val selectedSort: StateFlow<Sort> = _selectedSort

    private val _noteOwnerUser: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Empty)
    val noteOwnerUser: StateFlow<Response<User>> = _noteOwnerUser

    fun changeSortSelection(sort: Sort) {
        _selectedSort.value = sort
    }

    private fun getNotes() {
        getLocalNotes()
        getSharedNotes()
    }

    init {
        getNotes()
    }

    private fun getLocalNotes() {
        sharedScope.launch {
            localUseCases.getNotes.invoke().collect { response ->
                val notes = response.map { localNote ->
                    localNote.changeToNote()
                }
                _localNotes.value = notes
            }
        }
    }

    var noteSearchPhrase = MutableStateFlow("")

    private var _displayedNote = MutableStateFlow(clearNote)
    val displayedNote: CommonFlow<Note> = _displayedNote.asCommonFlow()

    fun changeTitleInput(newTitle: String) {
        _displayedNote.value = _displayedNote.value.copy(title = newTitle)
    }

    fun changeContentInput(newContent: String) {
        _displayedNote.value = _displayedNote.value.copy(content = newContent)
    }

    fun changeNoteColor(newColor: String) {
        _displayedNote.value = _displayedNote.value.copy(color = newColor)
    }

    var anotherUserEmailAddress = MutableStateFlow("")
    var isScrollingUp = MutableStateFlow(true)
    var isConfirmDeleteLocalNoteDialogOpen = MutableStateFlow(false)
    var isConfirmDeleteAccessFromSharedNoteDialogOpen = MutableStateFlow(false)
    var isShareDialogOpen = MutableStateFlow(false)
    var isSortOptionDropdownOpen = MutableStateFlow(false)
    var isRefreshing = MutableStateFlow(false)
    var isEditEnabled = MutableStateFlow(false)

    fun addNote(providedId: String? = null) {
        val note =
            createNewLocalNote(
                providedId,
                _displayedNote.value.title,
                _displayedNote.value.content,
                _displayedNote.value.color
            )
        sharedScope.launch {
            localUseCases.addNote.invoke(note).collect { response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun editNote() {
        sharedScope.launch {
            localUseCases.editNote.invoke(
                _displayedNote.value.title,
                _displayedNote.value.content,
                _displayedNote.value.color,
                _displayedNote.value.noteId
            ).collect { response ->
                _noteModificationStatus.value = response
                if (response is Response.Success) {
                    isEditEnabled.value = false
                    editSharedNote()
                }
            }
        }
    }

    fun deleteNoteAndCloseDialog() {
        deleteNote()
        isConfirmDeleteLocalNoteDialogOpen.value = false
    }

    fun deleteNote() {
        sharedScope.launch {
            localUseCases.deleteNote.invoke(_displayedNote.value.noteId).collect { response ->
                _noteModificationStatus.value = response
                if (response is Response.Success) deleteSharedNote(_displayedNote.value.noteId)
            }
        }
    }

    fun getSharedNotes() {
        sharedScope.launch {
            remoteUseCases.getSharedNotes.invoke().collect { response ->
                when (response) {
                    is Response.Success -> {
                        val noteList = (response.data).map {
                            it.changeToNote()
                        }
                        _sharedNotesState.value = Response.Success(true)
                        _notesSharedByOtherUsers.value = noteList
                        isRefreshing.value = false
                    }
                    is Response.Loading -> {
                        _sharedNotesState.value = response
                    }
                    is Response.Error -> {
                        _sharedNotesState.value = response
                        isRefreshing.value = false
                    }
                    else -> {
                        _sharedNotesState.value = Response.Empty
                    }
                }
            }
        }
    }

    fun shareNote() {
        sharedScope.launch {
            remoteUseCases.shareNote.invoke(anotherUserEmailAddress.value, _displayedNote.value)
                .collect { response ->
                    _noteSharingState.value = response
                    if (response is Response.Success) anotherUserEmailAddress.value = ""
                }
        }
    }

    private fun deleteSharedNote(noteId: String) {
        sharedScope.launch {
            remoteUseCases.deleteSharedNote.invoke(noteId = noteId).collect { response ->
                _noteSharingState.value = response
                restartNoteSharingState()
            }
        }
    }

    private fun editSharedNote() {
        sharedScope.launch {
            remoteUseCases.editSharedNote.invoke(
                _displayedNote.value.noteId,
                _displayedNote.value.title,
                _displayedNote.value.content,
                _displayedNote.value.color
            ).collect()
        }
    }

    fun unShareNote(sharedUserId: String) {
        sharedScope.launch {
            remoteUseCases.unshareNote.invoke(sharedUserId, _displayedNote.value.noteId)
                .collect { response ->
                    if (response is Response.Success) getUsersWithAccess()
                }
        }
    }

    fun deleteOwnAccessFromSharedNote() {
        sharedScope.launch {
            remoteUseCases.unshareNote.invoke(auth.currentUser!!.uid, _displayedNote.value.noteId)
                .collect { response ->
                    _noteSharingState.value = response
                }
        }
    }

    fun getUsersWithAccess() {
        sharedScope.launch {
            usersUseCases.getUsersWithAccess.invoke(_displayedNote.value.noteId)
                .collect { response ->
                    _usersWithAccess.value = response
                }
        }
    }

    fun formatDateTime(date: String): String {
        return formatIso8601ToString(date)
    }

    fun setNoteModificationError(errorMessage: String) {
        _noteModificationStatus.value = Response.Error(errorMessage)
    }

    fun resetNoteModificationStatus() {
        _noteModificationStatus.value = Response.Empty
    }

    fun listenToNoteListChanges() {
        sharedScope.launch {
            _selectedSort.collect { prepareLists() }
        }
        sharedScope.launch {
            noteSearchPhrase.collect { prepareLists() }
        }
        sharedScope.launch {
            _notesSharedByOtherUsers.collect { prepareLists() }
        }
        sharedScope.launch {
            _localNotes.collect { prepareLists() }
        }
    }

    private fun prepareLists() {
        _notes.value = prepareLists(
            _localNotes.value,
            _notesSharedByOtherUsers.value,
            _selectedSort.value,
            noteSearchPhrase.value
        )
    }

    private fun getNoteOwnerEmailAddress(noteOwnerUserId: String) {
        sharedScope.launch {
            usersUseCases.getUser.invoke(noteOwnerUserId).collect { response ->
                _noteOwnerUser.value = response
            }
        }
    }

    fun prepareNoteScreen(note: Note?) {
        if (note == null) {
            this._displayedNote.value = clearNote
            return
        } else {
            this._displayedNote.value = note
            if (note.isOwnedByCurrentUser()) getUsersWithAccess()
            else if(note.ownerUserId != null) getNoteOwnerEmailAddress(note.ownerUserId)
        }
    }

    private fun Note.isOwnedByCurrentUser() = this.ownerUserId == auth.currentUser?.uid

    fun closeShareDialog() {
        isShareDialogOpen.value = false
    }

    fun restartNoteSharingState() {
        _noteSharingState.value = Response.Empty
    }
}