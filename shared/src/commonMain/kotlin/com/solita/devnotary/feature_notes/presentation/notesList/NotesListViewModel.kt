package com.solita.devnotary.feature_notes.presentation.notesList

import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.presentation.noteDetail.changeToNote
import com.solita.devnotary.utils.CommonViewModel
import com.solita.devnotary.utils.formatIso8601ToString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class NotesListViewModel(dependencyInjection: DI = di) : CommonViewModel() {

    private val localUseCases: LocalNotesUseCases by dependencyInjection.instance()
    private val remoteUseCases: RemoteNotesUseCases by dependencyInjection.instance()

    private val _selectedSort: MutableStateFlow<Sort> =
        MutableStateFlow(SortOptions.BY_NAME_ASC.sort)
    val selectedSort: StateFlow<Sort> = _selectedSort

    var noteSearchPhrase = MutableStateFlow("")
    var isScrollingUp = MutableStateFlow(true)
    var isSortOptionDropdownOpen = MutableStateFlow(false)
    var isRefreshing = MutableStateFlow(false)

    fun changeSortSelection(sort: Sort) {
        _selectedSort.value = sort
    }

    private val _localNotes: MutableStateFlow<List<Note>> = MutableStateFlow(listOf())
    private val _notesSharedByOtherUsers: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())

    private val _notes: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val notes: StateFlow<List<Note>> = _notes

    private val _sharedNotesState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val sharedNotesState: StateFlow<Response<Boolean>> = _sharedNotesState

    fun getNotes() {
        getLocalNotes()
        getSharedNotes()
    }

    init {
        getNotes()
        listenToNoteListChanges()
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

    fun formatDateTime(date: String): String {
        return formatIso8601ToString(date)
    }

    private fun prepareLists() {
        _notes.value = com.solita.devnotary.feature_notes.presentation.noteDetail.prepareLists(
            _localNotes.value,
            _notesSharedByOtherUsers.value,
            _selectedSort.value,
            noteSearchPhrase.value
        )
    }

}