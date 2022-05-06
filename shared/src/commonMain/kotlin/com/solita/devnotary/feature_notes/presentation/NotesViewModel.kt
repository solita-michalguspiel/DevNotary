package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class NotesViewModel(dependencyInjection: DI = di) : ViewModel() {

    private val useCases: LocalNotesUseCases by dependencyInjection.instance()

    private val _noteModificationStatus: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val noteModificationStatus: StateFlow<Response<Boolean>> = _noteModificationStatus

    val getNotes get() =  useCases.getNotes.invoke()

    fun addNote(note: Note) {
        viewModelScope.launch {
            useCases.addNote.invoke(note).collect { response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun editNote(note: Note){
        viewModelScope.launch {
            useCases.editNote.invoke(note = note).collect{ response ->
                _noteModificationStatus.value = response
            }
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            useCases.deleteNote.invoke(noteId).collect{response ->
                _noteModificationStatus.value = response
            }
        }
    }

}