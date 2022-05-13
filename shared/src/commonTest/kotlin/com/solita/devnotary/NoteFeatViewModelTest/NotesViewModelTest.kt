package com.solita.devnotary.NoteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private lateinit var viewModel: NotesViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val testDispatcher = StandardTestDispatcher()


    private val testDI = DI {
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

        bindSingleton { LocalNotesRepoTestImpl() }
        bindSingleton { RemoteNotesRepoTestImpl() }

    }
    private val remoteNotesRepository : RemoteNotesRepoTestImpl by testDI.instance()

    private val firstNote = Note(
        noteId = "fidhsbagoipngöklsagagew",
        title = "Fresh note!",
        content = "Description",
        dateTime = "dateAndTime",
        color = "pink"
    )
    private val secondNote = firstNote.copy("fou23", color = "purple")
    private val thirdNote = firstNote.copy("bue2", color = "purple")

    @BeforeTest
    fun setup() {

        Dispatchers.setMain(testDispatcher)
        viewModel = NotesViewModel(testDI)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    @Test
    fun givenNoteWasAdded_StateOfNoteModShouldChangeToLoadingAndThenToSuccess(): TestResult =
        runTest {
            launch {
                viewModel.titleInput.value = firstNote.title
                viewModel.contentInput.value = firstNote.content
                viewModel.noteColor.value = firstNote.color
                viewModel.addNote(firstNote.noteId)
            }
            advanceTimeBy(30)
            viewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            viewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
            (viewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Add::class)
        }

    @Test
    fun givenNoteIsEdited_NoteShouldGetEditedAndStateOfNoteModShouldChangeToLoadingAndThenToSuccess(): TestResult =
        runTest {
            launch {
                setNote(firstNote)
                viewModel.addNote(firstNote.noteId)
                viewModel.noteId = firstNote.noteId
                viewModel.contentInput.value = "ChangedContent"
                viewModel.editNote()
            }
            advanceTimeBy(50)
            viewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            viewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
            (viewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Edit::class)        }


    @Test
    fun givenNoteIsDeleted_NoteModStateShouldChangeToLoadingThenSuccess(): TestResult = runTest {
        launch {
            setNote(thirdNote)
            viewModel.addNote(thirdNote.noteId)
            setNote(secondNote)
            viewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            viewModel.noteId = secondNote.noteId
            viewModel.deleteNote()
        }
        advanceTimeBy(50)
        viewModel.noteModificationStatus.value shouldBe Response.Loading
        advanceUntilIdle()
        viewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
        (viewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Delete::class)
    }

    @Test
    fun removingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            setNote(firstNote)
            viewModel.addNote(firstNote.noteId)
            setNote(secondNote)
            viewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            viewModel.noteId = thirdNote.noteId
            viewModel.deleteNote()
        }
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }

    @Test
    fun editingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote.noteId)
            viewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            viewModel.noteId = "Some uuid that does not exists"
            viewModel.noteDateTime = "Some date"
            viewModel.editNote()
        }
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }


    private fun setNote(note: Note){
        viewModel.titleInput.value = note.title
        viewModel.contentInput.value = note.content
        viewModel.noteColor.value = note.color
    }


}



