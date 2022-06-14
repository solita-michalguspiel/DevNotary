package com.solita.devnotary.NoteFeatViewModelTest

import com.solita.devnotary.Constants.ERROR_MESSAGE
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.use_case.local_notes_use_cases.*
import com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases.*
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.SortOptions
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class NotesViewModelsTest {

    private lateinit var noteDetailViewModel: NoteDetailViewModel
    private lateinit var notesListsViewModel: NotesListViewModel
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
        content = "Content!!!",
        dateTime = "dateAndTime",
        color = "pink"
    )
    private val secondNote = firstNote.copy("fou23", color = "purple")
    private val thirdNote = firstNote.copy("bue2", color = "purple")

    @BeforeTest
    fun setup() {

        Dispatchers.setMain(testDispatcher)
        noteDetailViewModel = NoteDetailViewModel(testDI)
        notesListsViewModel = NotesListViewModel(testDI)
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
                noteDetailViewModel.changeTitleInput(firstNote.title)
                noteDetailViewModel.changeContentInput(firstNote.content)
                noteDetailViewModel.changeNoteColor(firstNote.color)
                noteDetailViewModel.addNote(firstNote.noteId)
            }
            advanceTimeBy(30)
            noteDetailViewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            noteDetailViewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
            (noteDetailViewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Add::class)
        }

    @Test
    fun givenNoteIsEdited_NoteShouldGetEditedAndStateOfNoteModShouldChangeToLoadingAndThenToSuccess(): TestResult =
        runTest {
            launch {
                setNote(firstNote)
                noteDetailViewModel.addNote(firstNote.noteId)
                noteDetailViewModel.changeNoteId(noteId = firstNote.noteId)
                noteDetailViewModel.changeContentInput("ChangedContent")
                noteDetailViewModel.editNote()
            }
            advanceTimeBy(50)
            noteDetailViewModel.noteModificationStatus.value shouldBe Response.Loading
            advanceUntilIdle()
            noteDetailViewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
            (noteDetailViewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Edit::class)        }


    @Test
    fun givenNoteIsDeleted_NoteModStateShouldChangeToLoadingThenSuccess(): TestResult = runTest {
        launch {
            setNote(thirdNote)
            noteDetailViewModel.addNote(thirdNote.noteId)
            setNote(secondNote)
            noteDetailViewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            noteDetailViewModel.changeNoteId(noteId = secondNote.noteId)
            noteDetailViewModel.deleteNote()
        }
        advanceTimeBy(50)
        noteDetailViewModel.noteModificationStatus.value shouldBe Response.Loading
        advanceUntilIdle()
        noteDetailViewModel.noteModificationStatus.value should beInstanceOf(Response.Success::class)
        (noteDetailViewModel.noteModificationStatus.value as Response.Success).data should beInstanceOf(Operation.Delete::class)
    }

    @Test
    fun removingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            setNote(firstNote)
            noteDetailViewModel.addNote(firstNote.noteId)
            setNote(secondNote)
            noteDetailViewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            noteDetailViewModel.changeNoteId(noteId = thirdNote.noteId)
            noteDetailViewModel.deleteNote()
        }
        advanceUntilIdle()
        noteDetailViewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }

    @Test
    fun editingNoteIdThatNotExists_ShouldChangeNoteModStateToError(): TestResult = runTest {
        launch {
            noteDetailViewModel.addNote(firstNote.noteId)
            noteDetailViewModel.addNote(secondNote.noteId)
        }
        advanceUntilIdle()
        launch {
            noteDetailViewModel.changeNoteId(noteId = "Some uuid that does not exists")
            noteDetailViewModel.editNote()
        }
        advanceUntilIdle()
        noteDetailViewModel.noteModificationStatus.value shouldBe Response.Error(ERROR_MESSAGE)
    }

    @Test
    fun givenUserIdIsNull_AndGetSharedNotesIsCalled_SharedNotesStateShouldBeError() : TestResult = runTest{
        remoteNotesRepository.currentUserId = null
        launch {   notesListsViewModel.getSharedNotes() }
        advanceUntilIdle()
        notesListsViewModel.sharedNotesState.value shouldBe Response.Error("User is not logged in")
    }

    @Test
    fun givenThereIsSharedNoteAndGetSharedNotesIsCalled_SharedNoteShouldAppearInNotes() : TestResult = runTest {
            notesListsViewModel.listenToNoteListChanges()
            noteDetailViewModel.changeNoteId(firstNote.noteId)
            noteDetailViewModel.changeTitleInput(firstNote.title)
            noteDetailViewModel.changeContentInput(firstNote.content)
            noteDetailViewModel.changeNoteColor(firstNote.color)
        launch {
            noteDetailViewModel.anotherUserEmailAddress.value = remoteNotesRepository.appUser1
            noteDetailViewModel.shareNote()
        }
        advanceUntilIdle()
        remoteNotesRepository.currentUserId = remoteNotesRepository.appUser1 // SIMULATING THAT USER 1 IS USING APP
        launch { notesListsViewModel.getSharedNotes() }
        advanceUntilIdle()
        notesListsViewModel.sharedNotesState.value shouldBe Response.Success(true)
        notesListsViewModel.notes.value shouldContain Note(firstNote.noteId,remoteNotesRepository.appUser3,
            firstNote.title,firstNote.content,"TODAY",firstNote.color)
    }

    @Test
    fun givenTheSearchPhraseIsUsed_OnlyTheCorrectNoteShouldBeInNotes() : TestResult = runTest{
        setNote(firstNote)
        launch {
            notesListsViewModel.listenToNoteListChanges()
            noteDetailViewModel.addNote("first_note")
            setNote(secondNote.copy(title = "another title"))
            noteDetailViewModel.addNote("second_note")
            setNote(thirdNote.copy(title = "and another title"))
            noteDetailViewModel.addNote("third_note")
        }
        advanceUntilIdle()
        notesListsViewModel.getNotes()
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 3
        notesListsViewModel.noteSearchPhrase.value = "fresh"
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 1
        notesListsViewModel.notes.value.first().noteId shouldBe "first_note"
    }

    private fun addABC_TitledNotes(){
        setNote(firstNote.copy(title = "a"))
        noteDetailViewModel.addNote()
        setNote(firstNote.copy(title = "b"))
        noteDetailViewModel.addNote()
        setNote(firstNote.copy(title = "c"))
        noteDetailViewModel.addNote()
    }

    @Test
    fun givenTheSelectedSortIsByNameInDescendingOrder_NotesShouldBeSortedAccordingly() : TestResult = runTest{
        launch{
            notesListsViewModel.listenToNoteListChanges()
            addABC_TitledNotes()
            notesListsViewModel.getNotes()
            notesListsViewModel.changeSortSelection(SortOptions.BY_NAME_DESC.sort)
        }
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 3
        notesListsViewModel.notes.value[0].title shouldBe "c"
        notesListsViewModel.notes.value[1].title shouldBe "b"
        notesListsViewModel.notes.value[2].title shouldBe "a"
    }

    @Test
    fun givenTheSelectedSortIsByNameInAscendingOrder_NotesShouldBeSortedAccordingly() : TestResult = runTest{
        launch{
            notesListsViewModel.listenToNoteListChanges()
            addABC_TitledNotes()
            notesListsViewModel.getNotes()
            notesListsViewModel.changeSortSelection(SortOptions.BY_NAME_ASC.sort)
        }
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 3
        notesListsViewModel.notes.value[0].title shouldBe "a"
        notesListsViewModel.notes.value[1].title shouldBe "b"
        notesListsViewModel.notes.value[2].title shouldBe "c"
    }

    @Test
    fun givenTheSelectedSortIsByDateInAscendingOrder_NotesShouldBeSortedAccordingly() : TestResult = runTest{
        launch{
            notesListsViewModel.listenToNoteListChanges()
            addABC_TitledNotes()
            notesListsViewModel.getNotes()
            notesListsViewModel.changeSortSelection(SortOptions.BY_DATE_ASC.sort)
        }
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 3
        notesListsViewModel.notes.value[0].title shouldBe "a"
        notesListsViewModel.notes.value[1].title shouldBe "b"
        notesListsViewModel.notes.value[2].title shouldBe "c"
    }

    @Test
    fun givenTheSelectedSortIsByDateInDescendingOrder_NotesShouldBeSortedAccordingly() : TestResult = runTest{
        launch{
            notesListsViewModel.listenToNoteListChanges()
            addABC_TitledNotes()
            notesListsViewModel.getNotes()
            notesListsViewModel.changeSortSelection(SortOptions.BY_DATE_DESC.sort)
        }
        advanceUntilIdle()
        notesListsViewModel.notes.value.size shouldBe 3
        notesListsViewModel.notes.value[0].title shouldBe "c"
        notesListsViewModel.notes.value[1].title shouldBe "b"
        notesListsViewModel.notes.value[2].title shouldBe "a"
    }




    private fun setNote(note: Note){
        noteDetailViewModel.changeTitleInput(note.title)
        noteDetailViewModel.changeContentInput(note.content)
        noteDetailViewModel.changeNoteColor(note.color)
    }


}



