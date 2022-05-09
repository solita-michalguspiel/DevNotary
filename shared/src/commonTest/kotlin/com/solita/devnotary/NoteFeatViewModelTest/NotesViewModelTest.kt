package com.solita.devnotary.NoteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.database.Note
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
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
                viewModel.addNote(firstNote.note_id,firstNote.title,firstNote.content,firstNote.color)
            }
            advanceTimeBy(30)
            viewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            viewModel.noteModificationStatus.value shouldBe Response.Success(true)
        }

    @Test
    fun givenNoteIsAdded_AndGetNotesIsCalled_NoteShouldBeRetrieved(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote.note_id,firstNote.title,firstNote.content,firstNote.color)
        }
        advanceUntilIdle()
        viewModel.getNotes.collectLatest {

            it.first().note_id shouldBe firstNote.note_id
            it.first().title shouldBe firstNote.title
            it.first().content shouldBe firstNote.content
        }
    }

    @Test
    fun givenNoteIsEdited_NoteShouldGetEditedAndStateOfNoteModShouldChangeToLoadingAndThenToSuccess(): TestResult =
        runTest {
            val changedFirstNote = firstNote.copy(content = "ChangedContent")
            launch {
                viewModel.addNote(firstNote.note_id,firstNote.title,firstNote.content,firstNote.color)
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
            viewModel.addNote(thirdNote.note_id,thirdNote.title,thirdNote.content,thirdNote.color)
            viewModel.addNote(secondNote.note_id,secondNote.title,secondNote.content,secondNote.color)
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
            it.map { it.note_id } shouldBe listOf(thirdNote.note_id)
        }
    }

    @Test
    fun removingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            viewModel.addNote(firstNote.note_id,firstNote.title,firstNote.content,firstNote.color)
            viewModel.addNote(secondNote.note_id,secondNote.title,secondNote.content,secondNote.color)
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
            viewModel.addNote(firstNote.note_id,firstNote.title,firstNote.content,firstNote.color)
            viewModel.addNote(secondNote.note_id,secondNote.title,secondNote.content,secondNote.color)
        }
        advanceUntilIdle()
        launch {
            viewModel.editNote(thirdNote)
        }
        advanceUntilIdle()
        viewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }


    @Test
    fun givenUserIdIsNull_AndGetSharedNotesIsCalled_SharedNotesStateShouldBeError() : TestResult = runTest{
        remoteNotesRepository.currentUserId = null
        launch {   viewModel.getSharedNotes() }
        advanceUntilIdle()
        viewModel.sharedNotesState.value shouldBe Response.Error("User is not logged in")
    }

    @Test
    fun givenThereIsSharedNoteAndGetSharedNotesIsCalled_SharedNoteShouldAppearInSharedNotesState() : TestResult = runTest {
        launch {
            viewModel.shareNote(remoteNotesRepository.appUser1, firstNote)
        }
        advanceUntilIdle()
        remoteNotesRepository.currentUserId = remoteNotesRepository.appUser1 // SIMULATING THAT USER 1 IS USING APP
        launch { viewModel.getSharedNotes() }
        advanceUntilIdle()
        viewModel.sharedNotesState.value shouldBe Response.Success(mutableListOf(SharedNote(firstNote.note_id,remoteNotesRepository.appUser3,
            remoteNotesRepository.currentUserId!!,firstNote.title,firstNote.content,"TODAY",firstNote.color))
        )
    }

}



