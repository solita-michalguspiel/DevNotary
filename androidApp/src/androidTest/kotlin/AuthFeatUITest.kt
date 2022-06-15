
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.solita.devnotary.android.MainActivity
import com.solita.devnotary.android.MainScreen
import com.solita.devnotary.android.theme.DevNotaryTheme
import com.solita.devnotary.android.utils.TestTags
import com.solita.devnotary.di.*
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
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
class AuthFeatUITest {
    private lateinit var _authViewModel: AuthViewModel

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup(){
        di = DI{
            import(diCommonModule)
            import(TestDI.diAuthFeatTestModule)
            import(diNotesFeatModule)
            import(diUsersFeatModule)
            import(diDatabaseModule)
        }
        val authViewModel: AuthViewModel by di.instance()
        _authViewModel = authViewModel
        composeTestRule.setContent {
            DevNotaryTheme {
                 MainScreen()
            }
        }
    }

    @Test
    fun givenTheAppHasLaunchedAndUserIsNotSignedIn_SendLinkButtonShouldBeVisible(){
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).assertExists()
    }

    @Test
    fun givenUserHasSignedIn_SendLinkButtonShouldNotBeVisible_ButSignOutShouldBe(): TestResult = runTest{
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).assertExists()
        launch {
            _authViewModel.signInWithLink("fakeIntent")
        }
        advanceUntilIdle()
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SIGN_OUT_BUTTON_TAG).assertExists()
    }

    @Test
    fun givenUserRequestedLink_TimerShouldAppearAnd_ButtonBeDisabled() : TestResult = runTest {
        launch {
            _authViewModel.changeEmailAddress("notEmptyEmail@gmail.com")
            composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).performClick()
        }
        advanceUntilIdle()
        composeTestRule.onNodeWithTag(TestTags.TIMER_TAG).assertExists()
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).assertIsNotEnabled()
    }

    @Test
    fun givenUserClicksSendLinkWithoutProvidingAnEmailAddress_ErrorShouldAppear_SendEmailLinkShouldStayEnabled(): TestResult = runTest{
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).performClick()
        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithTag(TestTags.SEND_LINK_BUTTON_TAG).assertIsEnabled()
    }

}