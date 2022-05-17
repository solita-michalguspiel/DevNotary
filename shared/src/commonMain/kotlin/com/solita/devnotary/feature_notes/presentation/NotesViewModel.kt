package com.solita.devnotary.feature_notes.presentation

import com.benasher44.uuid.Uuid
import com.solita.devnotary.Constants
import com.solita.devnotary.Constants.BLANK_NOTE_ERROR
import com.solita.devnotary.Constants.NO_TITLE_ERROR
import com.solita.devnotary.database.Local_note
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.LocalNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.RemoteNotesUseCases
import com.solita.devnotary.feature_notes.domain.use_case.users_use_cases.UsersUseCases
import com.solita.devnotary.utils.formatIso8601ToString
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.random.Random

class NotesViewModel(dependencyInjection: DI = di) : ViewModel() {

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
    val localNotes: StateFlow<List<Note>> = _localNotes

    private val _sharedNotes: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val sharedNotes: StateFlow<List<Note>> = _sharedNotes

    private val _notes: MutableStateFlow<List<Note>> =
        MutableStateFlow(listOf())
    val notes: StateFlow<List<Note>> = _notes

    private val _usersWithAccess : MutableStateFlow<Response<List<User>>> = MutableStateFlow(Response.Empty)
    val usersWithAccess : StateFlow<Response<List<User>>> = _usersWithAccess

    private val _selectedSort: MutableStateFlow<Sort> = MutableStateFlow(ByName(Order.ASCENDING))

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

    private var _noteScreenState: MutableStateFlow<NoteScreenState?> = MutableStateFlow(null)
    val noteScreenState: StateFlow<NoteScreenState?> = _noteScreenState

    fun changeNoteScreenState(screenState: NoteScreenState) {
        _noteScreenState.value = screenState
    }

    var noteId: String = ""
    var noteDateTime: String = ""
    var titleInput = MutableStateFlow("")
    var contentInput = MutableStateFlow("")
    var noteColor = MutableStateFlow("")
    var isShareDropdownExpanded = MutableStateFlow(false)
    var isFabVisible = MutableStateFlow(true)
    var isConfirmDeleteDialogOpen = MutableStateFlow(false)
    var isShareDialogOpen = MutableStateFlow(false)
    var anotherUserEmailAddress = MutableStateFlow("")
    var isUsersWithAccessDialogOpen = MutableStateFlow(false)

    fun addNote(providedId: String? = null) {
        val id = providedId ?: Uuid(
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
        val note = Local_note(
            id,
            titleInput.value,
            contentInput.value,
            Clock.System.now().toString(),
            noteColor.value
        )
        viewModelScope.launch {
            localUseCases.addNote.invoke(note).collect { response ->
                if (response is Response.Success) {
                    noteDateTime = note.date_time
                    noteId = note.note_id
                    changeNoteScreenState(NoteScreenState.LocalNote)
                }
                _noteModificationStatus.value = response
            }
        }
    }

    fun editNote() {
        val note =
            Local_note(noteId, titleInput.value, contentInput.value, noteDateTime, noteColor.value)
        viewModelScope.launch {
            localUseCases.editNote.invoke(note = note).collect { response ->
                _noteModificationStatus.value = response
                if (response is Response.Success){
                    editSharedNote(note.note_id,titleInput.value,contentInput.value,noteColor.value)
                    changeNoteScreenState(NoteScreenState.LocalNote)
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
            localUseCases.deleteNote.invoke(noteId).collect { response ->
                _noteModificationStatus.value = response
                if(response is Response.Success) deleteSharedNote(noteId)
            }
        }
    }

    fun getSharedNotes() {
        viewModelScope.launch {
            remoteUseCases.getSharedNotes.invoke().collect { response ->
                when (response) {
                    is Response.Success -> {
                        _sharedNotesState.value = Response.Success(true)
                        val noteList = (response.data).map {
                            it.changeToNote()
                        }
                        _sharedNotes.value = noteList
                    }
                    is Response.Loading -> {
                        _sharedNotesState.value = response
                    }
                    is Response.Error -> {
                        _sharedNotesState.value = response
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
            noteId = noteId,
            title = titleInput.value,
            content = contentInput.value,
            dateTime = noteDateTime,
            color = noteColor.value
        )
        viewModelScope.launch {
            remoteUseCases.shareNote.invoke(anotherUserEmailAddress.value, note)
                .collect { response ->
                    _noteSharingState.value = response
                    if(response is Response.Success) anotherUserEmailAddress.value = ""
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

    private fun editSharedNote(noteId: String, noteTitle : String, noteContent: String, noteColor: String) {
        viewModelScope.launch {
            remoteUseCases.editSharedNote.invoke(noteId, noteTitle,noteContent,noteColor).collect()
        }
    }

    fun unShareNote(sharedUserId: String) {
        viewModelScope.launch {
            remoteUseCases.unshareNote.invoke(sharedUserId, noteId).collect { response ->
                _noteSharingState.value = response
                if(response is Response.Success)getUsersWithAccess()
            }
        }
    }

    fun getUsersWithAccess(){
        viewModelScope.launch {
            usersUseCases.getUsersWithAccess.invoke(noteId).collect{ response ->
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

    private fun Local_note.changeToNote(): Note {
        return Note(
            noteId = this.note_id,
            title = this.title,
            content = this.content,
            dateTime = this.date_time,
            color = this.color
        )
    }

    private fun SharedNote.changeToNote(): Note {
        return Note(
            noteId = this.noteId,
            ownerUserId = this.ownerUserId,
            title = this.title,
            content = this.content,
            dateTime = this.dateTime,
            color = this.color
        )
    }

    fun showFab() {
        if (!isFabVisible.value) isFabVisible.value = true
    }

    fun hideFab() {
        if (isFabVisible.value) isFabVisible.value = false
    }

    @Suppress("UNCHECKED_CAST")
    fun joinNoteLists(localNotesList: List<Note>, sharedNotesList: List<Note>) {
        val joinedNotes = localNotesList + sharedNotesList
        _notes.value = _selectedSort.value.sort(joinedNotes)
    }


    private fun setNoteScreenState(noteIndex: String?) {
        if (noteIndex == null) changeNoteScreenState(NoteScreenState.NewNote)
        else {
            val note = notes.value[noteIndex.toInt()]
            when (note.ownerUserId) {
                null -> changeNoteScreenState(NoteScreenState.LocalNote)
                else -> changeNoteScreenState(NoteScreenState.SharedNote)
            }
        }
    }

    private fun fillContent(
        noteIndex: Int
    ) {
        val note = notes.value[noteIndex]
        this.noteId = note.noteId
        this.titleInput.value = note.title
        this.contentInput.value = note.content
        this.noteDateTime = note.dateTime
        this.noteColor.value = note.color

    }

    fun clearContent() {
        this.noteId = ""
        this.titleInput.value = ""
        this.contentInput.value = ""
        this.noteDateTime = ""
        this.noteColor.value = Constants.WHITE_COLOR
    }

    fun prepareNoteScreen(noteIndex: String?) {
        if (noteIndex == null) {
            clearContent()
        } else {
            fillContent(noteIndex.toInt())
            getUsersWithAccess()
        }
        setNoteScreenState(noteIndex)
    }

    fun closeShareDialog() {
        isShareDialogOpen.value = false
        _noteSharingState.value = Response.Empty
    }


}