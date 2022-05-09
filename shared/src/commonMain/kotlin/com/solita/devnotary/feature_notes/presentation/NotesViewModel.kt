package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.kodein.di.DI
import org.kodein.di.instance

class NotesViewModel(dependencyInjection: DI = di) : ComposableViewModel, ViewModel() {

    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()
    private val usersUseCases: UsersUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: StateFlow<Response<Boolean>> = _noteModificationStatus

    private val _sharedNotesState : MutableStateFlow<Response<List<SharedNote>>> = MutableStateFlow(Response.Empty)
    val sharedNotesState : StateFlow<Response<List<SharedNote>>> = _sharedNotesState

    private val _noteSharingState : MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteSharingState: StateFlow<Response<Boolean>> = _noteSharingState

    val getNotes get() =  localUseCases.getNotes.invoke()

    fun addNote(noteId: String,title: String,content :String,color:String) {
        val note = Note(noteId,title,content,Clock.System.now().toString(),color)
        viewModelScope.launch {
            localUseCases.addNote.invoke(note).collect { response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun editNote(note: Note){
        viewModelScope.launch {
            localUseCases.editNote.invoke(note = note).collect{ response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            localUseCases.deleteNote.invoke(noteId).collect{ response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun getSharedNotes(){
        viewModelScope.launch {
            remoteUseCases.getSharedNotes.invoke().collect { response ->
            _sharedNotesState.value = response
            }
        }
    }

    fun shareNote(sharedUserId : String,note : Note){
        viewModelScope.launch {
            remoteUseCases.shareNote.invoke(sharedUserId,note).collect{ response ->
                _noteSharingState.value = response
            }
        }
    }

    fun deleteSharedNote(noteId: String){
        viewModelScope.launch {
            remoteUseCases.deleteSharedNote.invoke(noteId = noteId).collect{ response ->
                _noteSharingState.value = response
            }
        }
    }

    fun editSharedNote(noteId: String, note: Note){
        viewModelScope.launch {
            remoteUseCases.editSharedNote.invoke(noteId,note).collect{ response ->
                _noteSharingState.value = response
            }
        }
    }

    fun unShareNote(sharedUserId: String,noteId: String){
        viewModelScope.launch {
            remoteUseCases.unshareNote.invoke(sharedUserId, noteId).collect{ response ->
                _noteSharingState.value = response
            }
        }
    }

}