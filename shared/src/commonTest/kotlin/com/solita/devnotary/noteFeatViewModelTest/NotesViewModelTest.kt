package com.solita.devnotary.noteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
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
        bindSingleton { LocalNotesRepoTestImpl() }
    }


    private val firstNote = Note(
        "fidhsbagoipngöklsagagew",
        "Fresh note!",
        "Description",
        "dateAndTime",
        "Pink"
    )
    private val secondNote = firstNote.copy("fou23", color = "purple")
    private val thirdNote = firstNote.copy("bue2", color = "purple")
    private val noteList = listOf(firstNote, secondNote, thirdNote)

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
                viewModel.addNote(firstNote)
            }
            advanceTimeBy(30)
            viewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            viewModel.noteModificationStatus.value shouldBe Response.Success(true)
        }

    @Test
    fun givenNoteIsAdded_AndGetNotesIsCalled_NoteShouldBeRetrieved(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote)
        }
        advanceUntilIdle()
        viewModel.getNotes.collectLatest {
            it shouldBe listOf(firstNote)
        }
    }

    @Test
    fun givenNoteIsEdited_NoteShouldGetEditedAndStateOfNoteModShouldChangeToLoadingAndThenToSuccess(): TestResult =
        runTest {
            val changedFirstNote = firstNote.copy(content = "ChangedContent")
            launch {
                viewModel.addNote(firstNote)
                viewModel.editNote(changedFirstNote)
            }
            advanceTimeBy(50)
            viewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            viewModel.getNotes.collectLatest {
                it shouldBe listOf(changedFirstNote)
            }
            viewModel.noteModificationStatus.value shouldBe Response.Success(true)
        }

    @Test
    fun givenNoteIsDeleted_NoteModStateShouldChangeToLoadingThenSuccess(): TestResult = runTest {
        launch {
            viewModel.addNote(thirdNote)
            viewModel.addNote(secondNote)
        }
        advanceUntilIdle()
        launch {
            viewModel.deleteNote(secondNote.note_id)
        }
        advanceTimeBy(50)
        viewModel.noteModificationStatus.value shouldBe Response.Loading
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Success(true)
        viewModel.getNotes.collectLatest {
            it shouldBe listOf(thirdNote)
        }
    }

    @Test
    fun removingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote)
            viewModel.addNote(secondNote)
        }
        advanceUntilIdle()
        launch {
            viewModel.deleteNote(thirdNote.note_id)
        }
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }

    @Test
    fun editingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote)
            viewModel.addNote(secondNote)
        }
        advanceUntilIdle()
        launch {
            viewModel.editNote(thirdNote)
        }
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }





}



