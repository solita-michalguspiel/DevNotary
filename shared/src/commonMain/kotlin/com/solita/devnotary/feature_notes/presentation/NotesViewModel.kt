package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class NotesViewModel(dependencyInjection: DI = di) : ViewModel() {

    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: StateFlow<Response<Boolean>> = _noteModificationStatus

    val getNotes get() =  localUseCases.getNotes.invoke()

    fun addNote(note: Note) {
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

}