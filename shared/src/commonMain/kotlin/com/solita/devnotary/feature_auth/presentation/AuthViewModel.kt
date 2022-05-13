package com.solita.devnotary.feature_auth.presentation

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.CURRENT_EMAIL_KEY
import com.solita.devnotary.Constants.RESEND_EMAIL_TIME
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.use_case.AuthUseCases
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class AuthViewModel(dependencyInjection: DI = di) :
    ViewModel() {

    private val useCases: AuthUseCases by dependencyInjection.instance()
    private val settings: Settings by dependencyInjection.instance()

    private val _userAuthState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val userAuthState: StateFlow<Response<Boolean>> = _userAuthState

    private val _sendLinkState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val sendLinkState: StateFlow<Response<Boolean>> = _sendLinkState

    private val _userState: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Empty)
    val userState: StateFlow<Response<User>> = _userState

    val isUserAuthenticated get() = useCases.isUserAuthenticated.invoke()

    val openDialogState = MutableStateFlow(false)

    val errorMessageState = MutableStateFlow("")

    val resendEmailTimer = MutableStateFlow(0)

    private var job: Job? = null

    fun getCurrentUserDocument() {
        viewModelScope.launch {
            useCases.getCurrentUserDocument.invoke().collect { response ->
                _userState.value = response
                if (response is Response.Error) reactToError(response.message)
            }
        }
    }

    fun sendEmailLink(emailAddress: String) {
        _userAuthState.value = Response.Empty
        settings.putString(CURRENT_EMAIL_KEY, emailAddress)
        viewModelScope.launch {
            useCases.sendEmailLink.invoke(emailAddress).collect { response ->
                if (response == Response.Success(true)) startTimer(RESEND_EMAIL_TIME)
                _sendLinkState.value = response
                if (response is Response.Error) reactToError(response.message)
            }
        }
    }

    fun signInWithLink(intent: String) {
        val email = settings.getString(CURRENT_EMAIL_KEY)
        viewModelScope.launch {
            useCases.signInWithEmailLink(email = email, intent = intent).collect { response ->
                _userAuthState.value = response
                if (response is Response.Error) reactToError(response.message)
                when (response) {
                    is Response.Error -> reactToError(response.message)
                    is Response.Success -> stopTimer()
                    else -> {}
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            useCases.signOut.invoke().collect { response ->
                println(response)
                _userAuthState.value = response
                getCurrentUserDocument()
                if (response is Response.Error) reactToError(response.message)

            }
        }
    }

    private fun reactToError(error: String) {
        errorMessageState.value = error
        openDialogState.value = true

    }

    fun resetError() {
        openDialogState.value = false
        errorMessageState.value = ""
    }

    private fun startTimer(seconds: Int) {
        var timer = seconds
        job = viewModelScope.launch {
            coroutineScope {
                for (i in 0 until seconds) {
                    delay(1000)
                    timer -= 1
                    println(timer)
                    resendEmailTimer.value = timer
                }
            }
        }
    }

    private fun stopTimer() {
        job?.cancel()
        resendEmailTimer.value = 0
        _sendLinkState.value = Response.Empty
    }

    fun resetSendLinkState() {
        _sendLinkState.value = Response.Empty
    }
}