
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.solita.devnotary.android.MainActivity
import com.solita.devnotary.android.MainScreen
import com.solita.devnotary.android.theme.DevNotaryTheme
import com.solita.devnotary.android.utils.TestTags
import com.solita.devnotary.di.*
import com.solita.devnotary.feature_auth.data.AuthRepositoryTestImpl
import com.solita.devnotary.feature_auth.domain.repository.AuthRepository
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kodein.di.DI
import org.kodein.di.instance
import util.TestDI

@OptIn(ExperimentalCoroutinesApi::class)
class NotesFeatUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var _noteDetailViewModel: NoteDetailViewModel
    private lateinit var _noteListViewModel: NotesListViewModel
    private lateinit var _authViewModel: AuthViewModel


    @Before
    fun setup() {
        di = DI {
            import(diCommonModule)
            import(TestDI.diAuthFeatTestModule)
            import(TestDI.diNotesFeatTestModule)
            import(diUsersFeatModule)
            import(diDatabaseModule)
        }
        val authViewModel: AuthViewModel by di.instance()
        val noteDetailViewModel: NoteDetailViewModel by di.instance()
        val notesListViewModel: NotesListViewModel by di.instance()
        val authRepository: AuthRepository by di.instance<AuthRepositoryTestImpl>(DiConstants.AUTH_TAG)
        (authRepository as AuthRepositoryTestImpl)._testIsUserAuthenticated = true
        _authViewModel = authViewModel
        _noteDetailViewModel = noteDetailViewModel
        _noteListViewModel = notesListViewModel
        composeTestRule.setContent {
            DevNotaryTheme {
                MainScreen()
            }
        }
    }

    @Test
    fun givenTheAppJustStartedAndUserIsSignedIn_SignOutButtonAndWelcomeTextShouldExist() {
        composeTestRule.onNodeWithTag(TestTags.SIGN_OUT_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithText("testEmail@example.com")
            .assertExists()//FAKE USER EMAIL IN WELCOME TEXT
    }

    @Test
    fun givenAppJustStartedAndNotesTabIsClicked_NotesListShouldNotHaveAnyNote() {
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_TAG).assertDoesNotExist()
    }

    @Test
    fun givenOneNoteExist_NotesListShouldContainNotePreview(): TestResult = runTest {
        clickNotesTab()
        launch {
            addNote()
            _noteListViewModel.getNotes()
        }
        advanceUntilIdle()
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_TAG).assertExists()
    }

    @Test
    fun givenFABWasClicked_NoteInteractionScreenShouldAppear() {
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).performClick()
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD_TAG).assertExists()
        composeTestRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD_TAG).assertExists()
    }

    @Test
    fun givenThereAreSeveralNotes_WhenScreenIsScrolledDown_FABAndSearchBarShouldDisappear(): TestResult =
        runTest {
            clickNotesTab()
            launch {
                repeat(50) {
                    addNote()
                }
                _noteListViewModel.getNotes()
            }
            advanceUntilIdle()
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).assertExists()
            composeTestRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD_TAG).assertExists()
            composeTestRule.onRoot().performTouchInput { swipeUp(400f, 10f, 100) }
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).assertDoesNotExist()
            composeTestRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD_TAG).assertDoesNotExist()
        }

    @Test
    fun givenThereAreNotes_AndScreenScrollsDownAndUp_FABAndSearchBarShouldDisappearAndAppearAgain(): TestResult =
        runTest {
            clickNotesTab()
            launch {
                repeat(50) {
                    addNote()
                }
                _noteListViewModel.getNotes()
            }
            advanceUntilIdle()
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).assertExists()
            composeTestRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD_TAG).assertExists()
            composeTestRule.onRoot().performTouchInput { swipeUp(400f, 10f, 100) }
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).assertDoesNotExist()
            composeTestRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD_TAG).assertDoesNotExist()
            composeTestRule.onRoot().performTouchInput { swipeDown(10f, 400f, 100) }
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).assertExists()
            composeTestRule.onNodeWithTag(TestTags.SEARCH_TEXT_FIELD_TAG).assertExists()
        }

    @Test
    fun givenNoteWasAddedManually_AppShouldNavigateToNoteDetailsScreen() {
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).performClick()
        addNoteManually()
        composeTestRule.onNodeWithTag(TestTags.NOTE_DETAILS_SCREEN).assertExists()
    }

    @Test
    fun givenNoteWasAddedManually_AfterNavigatingBackNotePreviewShouldExist(): TestResult =
        runTest {
            clickNotesTab()
            composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_FAB_TAG).performClick()
            addNoteManually()
            composeTestRule.onNodeWithTag(TestTags.NOTE_DETAILS_SCREEN).assertExists()
            launch(Dispatchers.Main) {
                _noteListViewModel.getNotes()
                composeTestRule.activity.onBackPressed()
            }
            advanceUntilIdle()
            composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_TAG).assertExists()
        }

    @Test
    fun givenNewNoteButtonWasPressed_NoteInteractionScreenShouldBeVisible(): TestResult = runTest {
        launch {
            addNote()
            _noteListViewModel.getNotes()
        }
        advanceUntilIdle()
        clickNotesTab()
        openNoteDetailsAndClickNewNoteButton()
        composeTestRule.onNodeWithTag(TestTags.NOTE_INTERACTION_SCREEN).assertExists()
    }

    @Test
    fun givenNoteInteractionScreenWasOpenedFromAnotherNoteAndNewNoteIsAdded_MostRecentAddedNoteDetailsScreenShouldAppear(): TestResult =
        runTest {
            launch {
                addNote()
                _noteListViewModel.getNotes()
            }
            advanceUntilIdle()
            clickNotesTab()
            openNoteDetailsAndClickNewNoteButton()
            addNoteManually()
            composeTestRule.onNodeWithTag(TestTags.NOTE_DETAILS_SCREEN).assertExists()
            composeTestRule.onNodeWithTag(TestTags.NOTE_DETAIL_TITLE_TEXT_FIELD_TAG)
                .assertTextContains(manuallyAddedNoteTitle)
            composeTestRule.onNodeWithTag(TestTags.NOTE_DETAIL_CONTENT_TEXT_FIELD_TAG)
                .assertTextContains(manuallyAddedNoteContent)
        }

    @Test
    fun givenOneNoteExistAndItGetsManuallyDeleted_NoteListScreenShouldNotContainAnyNotePreview() {
        addNote()
        _noteListViewModel.getNotes()
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_VIEW_NOTE_DETAILS_BUTTON_TAG)
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.DELETE_NOTE_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_DELETE_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_DELETE_BUTTON_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.DELETE_NOTE_BUTTON_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.NOTES_LIST_SCREEN).assertExists()
        _noteListViewModel.getNotes()
        composeTestRule.mainClock.advanceTimeBy(2000)
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_TAG).assertDoesNotExist()
    }

    @Test
    fun givenNoteDetailsScreenIsShownAndEditButtonIsPressed_NoteInteractionScreenAppears(){
        addNote()
        _noteListViewModel.getNotes()
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_VIEW_NOTE_DETAILS_BUTTON_TAG)
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.EDIT_NOTE_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(TestTags.NOTE_INTERACTION_SCREEN).assertExists()
    }

    @Test
    fun givenNoteTitleWasEditedAndSaved_NotesListScreenShouldBeVisibleAndShouldContainNewNoteTitle(){
        addNote()
        _noteListViewModel.getNotes()
        clickNotesTab()
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_VIEW_NOTE_DETAILS_BUTTON_TAG)
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.EDIT_NOTE_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD_TAG).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD_TAG).performTextInput(editedNoteTitle)
        composeTestRule.onNodeWithTag(TestTags.SAVE_NOTE_BUTTON_TAG).performClick()
        composeTestRule.mainClock.advanceTimeBy(2000)
        composeTestRule.onNodeWithTag(TestTags.NOTES_LIST_SCREEN).assertExists()
        _noteListViewModel.getNotes()
        composeTestRule.onNodeWithText(editedNoteTitle).assertExists()
    }

    private val editedNoteTitle = "EditedNoteTitle"
    private fun openNoteDetailsAndClickNewNoteButton() {
        composeTestRule.onNodeWithTag(TestTags.NOTE_PREVIEW_VIEW_NOTE_DETAILS_BUTTON_TAG)
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.NEW_NOTE_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(TestTags.NEW_NOTE_BUTTON_TAG).performClick()
    }

    private fun clickNotesTab() {
        composeTestRule.onNodeWithTag(TestTags.NOTES_TAB_BUTTON_TAG).performClick()
    }

    private fun addNote() {
        _noteDetailViewModel.changeTitleInput("new note")
        _noteDetailViewModel.changeContentInput("content of the note")
        _noteDetailViewModel.addNote()
    }

    private fun addNoteManually() {
        composeTestRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD_TAG)
            .performTextInput(manuallyAddedNoteTitle)
        composeTestRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD_TAG)
            .performTextInput(manuallyAddedNoteContent)
        composeTestRule.onNodeWithTag(TestTags.ADD_NOTE_BUTTON_TAG).performClick()
    }

    private val manuallyAddedNoteTitle = "MANUALLY ADDED NOTE"
    private val manuallyAddedNoteContent = "MANUALLY ADDED NOTE CONTENT"

}