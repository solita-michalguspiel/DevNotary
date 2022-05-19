package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.Constants
import com.solita.devnotary.Constants.BLANK_NOTE_ERROR
import com.solita.devnotary.Constants.NO_TITLE_ERROR
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.utils.formatIso8601ToString
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class NotesViewModel(dependencyInjection: DI = di) : ViewModel() {

    private val auth: FirebaseAuth by dependencyInjection.instance()
    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()
    private val usersUseCases: UsersUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Operation>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: StateFlow<Response<Operation>> = _noteModificationStatus

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
    val notes: StateFlow<List<Note>> = _notes

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
        viewModelScope.launch {
            localUseCases.getNotes.invoke().collect { response ->
                val notes = response.map { localNote ->
                    localNote.changeToNote()
                }
                _localNotes.value = notes
            }
        }
    }

    var noteSearchPhrase = MutableStateFlow("")
    var noteId = MutableStateFlow("")
    var noteDateTime = MutableStateFlow("")
    var titleInput = MutableStateFlow("")
    var contentInput = MutableStateFlow("")
    var noteColor = MutableStateFlow("")
    var anotherUserEmailAddress = MutableStateFlow("")

    var isShareDropdownExpanded = MutableStateFlow(false)
    var isScrollingUp = MutableStateFlow(true)
    var isConfirmDeleteDialogOpen = MutableStateFlow(false)
    var isShareDialogOpen = MutableStateFlow(false)
    var isSortOptionDropdownOpen = MutableStateFlow(false)
    var isRefreshing = MutableStateFlow(false)
    var isEditEnabled = MutableStateFlow(false)

    fun addNote(providedId: String? = null) {
        if (titleInput.value.isBlank()) {
            _noteModificationStatus.value = Response.Error(NO_TITLE_ERROR)
            return
        }
        if (contentInput.value.isBlank()) {
            _noteModificationStatus.value = Response.Error(BLANK_NOTE_ERROR)
            return
        }
        val note =
            createNewLocalNote(providedId, titleInput.value, contentInput.value, noteColor.value)
        viewModelScope.launch {
            localUseCases.addNote.invoke(note).collect { response ->
                if (response is Response.Success)
                    _noteModificationStatus.value = response
            }
        }
    }

    fun editNote() {
        viewModelScope.launch {
            localUseCases.editNote.invoke(
                titleInput.value,
                contentInput.value,
                noteColor.value,
                noteId.value
            ).collect { response ->
                _noteModificationStatus.value = response
                if (response is Response.Success) {
                    isEditEnabled.value = false
                    editSharedNote(
                        noteId.value,
                        titleInput.value,
                        contentInput.value,
                        noteColor.value
                    )
                }
            }
        }
    }

    fun deleteNoteAndCloseDialog() {
        deleteNote()
        isConfirmDeleteDialogOpen.value = false
    }

    fun deleteNote() {
        viewModelScope.launch {
            localUseCases.deleteNote.invoke(noteId.value).collect { response ->
                _noteModificationStatus.value = response
                if (response is Response.Success) deleteSharedNote(noteId.value)
            }
        }
    }

    fun getSharedNotes() {
        viewModelScope.launch {
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
        val note = Note(
            noteId = noteId.value,
            title = titleInput.value,
            content = contentInput.value,
            dateTime = noteDateTime.value,
            color = noteColor.value
        )
        viewModelScope.launch {
            remoteUseCases.shareNote.invoke(anotherUserEmailAddress.value, note)
                .collect { response ->
                    _noteSharingState.value = response
                    if (response is Response.Success) anotherUserEmailAddress.value = ""
                }
        }
    }

    private fun deleteSharedNote(noteId: String) {
        viewModelScope.launch {
            remoteUseCases.deleteSharedNote.invoke(noteId = noteId).collect { response ->
                _noteSharingState.value = response
            }
        }
    }

    private fun editSharedNote(
        noteId: String,
        noteTitle: String,
        noteContent: String,
        noteColor: String
    ) {
        viewModelScope.launch {
            remoteUseCases.editSharedNote.invoke(noteId, noteTitle, noteContent, noteColor)
                .collect()
        }
    }

    fun unShareNote(sharedUserId: String) {
        viewModelScope.launch {
            remoteUseCases.unshareNote.invoke(sharedUserId, noteId.value).collect { response ->
                _noteSharingState.value = response
                if (response is Response.Success) getUsersWithAccess()
            }
        }
    }

    fun deleteOwnAccessFromSharedNote() {
        viewModelScope.launch {
            remoteUseCases.unshareNote.invoke(auth.currentUser!!.uid, noteId.value)
                .collect { response ->
                    _noteSharingState.value = response
                }
        }
    }

    fun getUsersWithAccess() {
        viewModelScope.launch {
            usersUseCases.getUsersWithAccess.invoke(noteId.value).collect { response ->
                _usersWithAccess.value = response
            }
        }
    }

    fun formatDateTime(date: String): String {
        return formatIso8601ToString(date)
    }

    fun resetNoteModificationStatus() {
        _noteModificationStatus.value = Response.Empty
    }

    fun listenToNoteListChanges() {
        viewModelScope.launch {
            _selectedSort.collect { prepareLists() }
        }
        viewModelScope.launch {
            noteSearchPhrase.collect { prepareLists() }
        }
        viewModelScope.launch {
            _notesSharedByOtherUsers.collect { prepareLists() }
        }
        viewModelScope.launch {
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
        viewModelScope.launch {
            usersUseCases.getUser.invoke(noteOwnerUserId).collect { response ->
                _noteOwnerUser.value = response
            }
        }
    }

    fun prepareNoteScreen(note: Note?) {
        this.noteId.value = note?.noteId ?: ""
        this.titleInput.value = note?.title ?: ""
        this.contentInput.value = note?.content ?: ""
        this.noteDateTime.value = note?.dateTime ?: ""
        this.noteColor.value = note?.color ?: Constants.WHITE_COLOR
        if ((note == null) || (note.ownerUserId == null)) return
        if (note.ownerUserId == auth.currentUser?.uid) getUsersWithAccess()
        else getNoteOwnerEmailAddress(note.ownerUserId)
    }

    fun closeShareDialog() {
        isShareDialogOpen.value = false
    }

    fun restartNoteSharingState() {
        _noteSharingState.value = Response.Empty
    }
}