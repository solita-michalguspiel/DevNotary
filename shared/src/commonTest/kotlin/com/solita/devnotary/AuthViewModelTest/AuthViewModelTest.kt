package com.solita.devnotary.AuthViewModelTest

import com.russhwolf.settings.MockSettings
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.domain.use_case.*
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.randomUser
import io.kotest.matchers.shouldBe
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
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val testDispatcher = StandardTestDispatcher()

    private val testDI = DI {
        bindSingleton { AuthRepoTestImpl() }
        bindSingleton {
            AuthUseCases(
                getCurrentUserDocument = GetCurrentUserDocument(instance()),
                sendEmailLink = SendEmailLink(instance()),
                signOut = SignOut(instance()),
                isUserAuthenticated = IsUserAuthenticated(instance()),
                signInWithEmailLink = SignInWithEmailLink(instance())
            )
        }
        bindSingleton { MockSettings() }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun givenViewModelIntentIsDifferentThanNull_AuthStateShouldChangeToSuccessTrue(): TestResult =
        runTest {
            launch {
                viewModel = AuthViewModel(
                    "somerandomlinkdata",
                    dependencyInjection = testDI
                )
            }
            advanceUntilIdle()
            viewModel.userAuthState.value shouldBe Response.Success(true)
        }

    @Test
    fun givenViewModelIntentIsDifferentThanNullAndSignOutIsCalled_AuthStateShouldChangeToSuccessFalse(): TestResult =
        runTest {
            launch {
                viewModel = AuthViewModel("someRandomLinkData", dependencyInjection = testDI)
                viewModel.signOut()
            }
            advanceUntilIdle()
            viewModel.userAuthState.value shouldBe Response.Success(false)
        }

    @Test
    fun givenIntentIsEmptyAndSendEmailLinkIsCalled_SendLinkStateShouldChangeToSuccessTrue(): TestResult =
        runTest {
            launch {
                viewModel = AuthViewModel("someRandomLinkData", dependencyInjection = testDI)
                viewModel.sendLinkState.value shouldBe Response.Empty
                viewModel.sendEmailLink("RandomEmailAddress@example.com")
            }
            advanceUntilIdle()
            viewModel.sendLinkState.value shouldBe Response.Success(true)
        }

    @Test
    fun givenUserIsNotLoggedInAndIntentIsNull_GetCurrentUserDocument_ShouldChangeUserStatusIntoError(): TestResult =
        runTest {
            launch {
                viewModel = AuthViewModel("null", dependencyInjection = testDI)
                viewModel.getCurrentUserDocument()
            }
            advanceUntilIdle()
            viewModel.userState.value shouldBe Response.Empty
        }

    @Test
    fun givenUserIsNotLoggedInAndIntentIsNull_IsUserLoggedInShouldReturnFalse() = runTest {
        launch {
            viewModel = AuthViewModel("null", dependencyInjection = testDI)
        }
        advanceUntilIdle()
        viewModel.isUserAuthenticated shouldBe false
    }

    @Test
    fun givenUserIsNotLoggedInIntentIsNullButSignInWithLinkIsCalled_GetCurrentUserDocument_ShouldChangeUserStatusIntoSuccess() = runTest{
        launch {
            viewModel = AuthViewModel("null", dependencyInjection = testDI)
            viewModel.userAuthState.value shouldBe Response.Empty
            viewModel.signInWithLink()
        }
        advanceUntilIdle()
        viewModel.userAuthState.value shouldBe Response.Success(true)
    }

    @Test
    fun givenTheUserHasLoggedIn_ThereforeUserDocumentShouldBeAvailableInUserState() = runTest {
        launch {
            viewModel = AuthViewModel("null", dependencyInjection = testDI)
            viewModel.userState.value shouldBe Response.Empty
            viewModel.signInWithLink()
            viewModel.getCurrentUserDocument()
        }
        advanceUntilIdle()
        viewModel.userState.value shouldBe  Response.Success(randomUser)
    }

    @Test
    fun givenUserLogedInAndThenLogedOut_UserDocShouldBeUnavailableAndStatesShouldChangeAccordigly() = runTest{
        launch {
            viewModel = AuthViewModel("null", dependencyInjection = testDI)
            viewModel.userAuthState.value shouldBe Response.Empty
        }
        advanceUntilIdle()
        launch {
            viewModel.signInWithLink()
        }
        advanceUntilIdle()
        viewModel.userAuthState.value shouldBe Response.Success(true)
        launch {
            viewModel.signOut()
        }
        advanceUntilIdle()
        viewModel.userAuthState.value shouldBe Response.Success(false)
    }

    @Test
    fun givenUserWasLoggedInButLoggedOut_StateOfUserShouldChangeToEmpty() = runTest{
        launch {
            viewModel = AuthViewModel("null", dependencyInjection = testDI)
            viewModel.userAuthState.value shouldBe Response.Empty
            viewModel.signInWithLink()
            viewModel.getCurrentUserDocument()
        }
        advanceUntilIdle()
        viewModel.userAuthState.value shouldBe Response.Success(true)
        viewModel.userState.value shouldBe Response.Success(randomUser)
        launch {
            viewModel.signOut()
        }
        advanceUntilIdle()
        viewModel.userState.value shouldBe Response.Empty

    }

}

