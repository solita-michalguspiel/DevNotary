package com.solita.devnotary.feature_auth.presentation

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.CURRENT_EMAIL_KEY
import com.solita.devnotary.Constants.RESEND_EMAIL_TIME
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.use_case.AuthUseCases
import com.solita.devnotary.utils.Timer
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val resendEmailTimer = MutableStateFlow(0)

    val emailAddressInput = MutableStateFlow("")

    fun getCurrentUserDocument() {
        viewModelScope.launch {
            useCases.getCurrentUserDocument.invoke().collect { response ->
                _userState.value = response
                if (response is Response.Error) setError(response.message)
            }
        }
    }

    fun sendEmailLink() {
        _userAuthState.value = Response.Empty
        settings.putString(CURRENT_EMAIL_KEY, emailAddressInput.value)
        viewModelScope.launch {
            useCases.sendEmailLink.invoke(emailAddressInput.value).collect { response ->
                if (response == Response.Success(true)) startTimer()
                _sendLinkState.value = response
            }
        }
    }

    fun signInWithLink(intent: String) {
        val email = settings.getString(CURRENT_EMAIL_KEY)
        viewModelScope.launch {
            useCases.signInWithEmailLink(email = email, intent = intent).collect { response ->
                _userAuthState.value = response
                when (response) {
                    is Response.Error -> setError(response.message)
                    is Response.Success -> stopTimer()
                    else -> {}
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            useCases.signOut.invoke().collect { response ->
                _userAuthState.value = response
                getCurrentUserDocument()
                if (response is Response.Error) setError(response.message)
            }
        }
    }

    val openDialogState = MutableStateFlow(false)
    val errorMessageState = MutableStateFlow("")

    private fun setError(error: String) {
        errorMessageState.value = error
        openDialogState.value = true
    }

    fun resetError() {
        openDialogState.value = false
        errorMessageState.value = ""
    }

    private val timer = Timer(RESEND_EMAIL_TIME)

    private fun startTimer(){
        viewModelScope.launch {
            timer.startTimer().collect {
                resendEmailTimer.value = it
            }
        }
    }

    private fun stopTimer(){
        timer.stopTimer()
        resendEmailTimer.value = 0
        _sendLinkState.value = Response.Empty
    }

    fun resetSendLinkState() {
        _sendLinkState.value = Response.Empty
    }
}