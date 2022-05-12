package com.solita.devnotary.feature_notes.presentation

import com.benasher44.uuid.Uuid
import com.solita.devnotary.Constants
import com.solita.devnotary.Constants.BLANK_NOTE_ERROR
import com.solita.devnotary.Constants.NO_TITLE_ERROR
import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.utils.formatIso8601ToString
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.random.Random

class NotesViewModel(dependencyInjection: DI = di) : ComposableViewModel, ViewModel() {

    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()
    private val usersUseCases: UsersUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Operation>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: StateFlow<Response<Operation>> = _noteModificationStatus

    private val _sharedNotesState: MutableStateFlow<Response<List<SharedNote>>> =
        MutableStateFlow(Response.Empty)
    val sharedNotesState: StateFlow<Response<List<SharedNote>>> = _sharedNotesState

    private val _noteSharingState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteSharingState: StateFlow<Response<Boolean>> = _noteSharingState

    val getNotes get() = localUseCases.getNotes.invoke()

    var isEditEnabled = MutableStateFlow(true)
    var noteId :String = ""
    var noteDateTime :String = ""
    var titleInput = MutableStateFlow("")
    var contentInput = MutableStateFlow("")
    var noteColor = MutableStateFlow("")

    var isConfirmDeleteDialogOpen = MutableStateFlow(false)

    fun addNote(noteId: String? = null) {
        val id = noteId ?: Uuid(
            Random(Clock.System.now().epochSeconds).nextLong(),
            Random(Clock.System.now().epochSeconds).nextLong()
        ).toString()
        if (titleInput.value.isBlank()) {
            _noteModificationStatus.value = Response.Error(NO_TITLE_ERROR)
            return
        }
        if (contentInput.value.isBlank()) {
            _noteModificationStatus.value = Response.Error(BLANK_NOTE_ERROR)
            return
        }
        val note = Note(
            id,
            titleInput.value,
            contentInput.value,
            Clock.System.now().toString(),
            noteColor.value
        )
        viewModelScope.launch {
            localUseCases.addNote.invoke(note).collect { response ->
                _noteModificationStatus.value = response
            }
        }
    }


    fun editNote() {
       val note = Note(noteId,titleInput.value,contentInput.value, noteDateTime,noteColor.value)
        viewModelScope.launch {
            localUseCases.editNote.invoke(note = note).collect { response ->
                _noteModificationStatus.value = response
                if(response is Response.Success) isEditEnabled.value = false
            }
        }
    }

    fun deleteNoteAndCloseDialog(){
        deleteNote()
        isConfirmDeleteDialogOpen.value = false
    }

    fun deleteNote() {
        viewModelScope.launch {
            localUseCases.deleteNote.invoke(noteId).collect { response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun getSharedNotes() {
        viewModelScope.launch {
            remoteUseCases.getSharedNotes.invoke().collect { response ->
                _sharedNotesState.value = response
            }
        }
    }

    fun shareNote(sharedUserId: String, note: Note) {
        viewModelScope.launch {
            remoteUseCases.shareNote.invoke(sharedUserId, note).collect { response ->
                _noteSharingState.value = response
            }
        }
    }

    fun deleteSharedNote(noteId: String) {
        viewModelScope.launch {
            remoteUseCases.deleteSharedNote.invoke(noteId = noteId).collect { response ->
                _noteSharingState.value = response
            }
        }
    }

    fun editSharedNote(noteId: String, note: Note) {
        viewModelScope.launch {
            remoteUseCases.editSharedNote.invoke(noteId, note).collect { response ->
                _noteSharingState.value = response
            }
        }
    }

    fun unShareNote(sharedUserId: String, noteId: String) {
        viewModelScope.launch {
            remoteUseCases.unshareNote.invoke(sharedUserId, noteId).collect { response ->
                _noteSharingState.value = response
            }
        }
    }

    fun formatDateTime(date: String): String {
        return formatIso8601ToString(date)
    }

    fun resetNoteModificationStatus(){
        _noteModificationStatus.value = Response.Empty
    }

    fun resetInputs(){
        titleInput.value = ""
        contentInput.value = ""
        noteColor.value = Constants.WHITE_COLOR
    }




}