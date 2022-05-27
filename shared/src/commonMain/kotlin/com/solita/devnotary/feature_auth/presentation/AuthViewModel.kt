package com.solita.devnotary.feature_auth.presentation

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.CURRENT_EMAIL_KEY
import com.solita.devnotary.Constants.RESEND_EMAIL_TIME
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.use_case.AuthUseCases
import com.solita.devnotary.utils.CommonFlow
import com.solita.devnotary.utils.SharedViewModel
import com.solita.devnotary.utils.Timer
import com.solita.devnotary.utils.asCommonFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class AuthViewModel(dependencyInjection: DI = di) :
    SharedViewModel() {

    private val useCases: AuthUseCases by dependencyInjection.instance()
    private val settings: Settings by dependencyInjection.instance()

    private val _userAuthState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val userAuthState: CommonFlow<Response<Boolean>> = _userAuthState.asCommonFlow()

    private val _sendLinkState: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Empty)
    val sendLinkState: CommonFlow<Response<Boolean>> = _sendLinkState.asCommonFlow()

    private val _userState: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Empty)
    val userState: CommonFlow<Response<User>> = _userState.asCommonFlow()

    val isUserAuthenticated get() = useCases.isUserAuthenticated.invoke()

    private val _resendEmailTimer = MutableStateFlow(0)
    val resendEmailTimer: CommonFlow<Int> = _resendEmailTimer.asCommonFlow()

    private val _emailAddressInput = MutableStateFlow("")
    val emailAddressInput: CommonFlow<String> = _emailAddressInput.asCommonFlow()


    fun changeEmailAddress(newEmailAddress: String) {
        _emailAddressInput.value = newEmailAddress
    }

    fun getCurrentUserDocument() {
        sharedScope.launch {
            useCases.getCurrentUserDocument.invoke().collect { response ->
                _userState.value = response
                if (response is Response.Error) setError(response.message)
                println(response)
            }
        }
    }

    fun sendEmailLink() {
        _userAuthState.value = Response.Empty
        settings.putString(CURRENT_EMAIL_KEY, _emailAddressInput.value)
        println(settings.getString(CURRENT_EMAIL_KEY))
        sharedScope.launch {
            useCases.sendEmailLink.invoke(_emailAddressInput.value).collect { response ->
                if (response == Response.Success(true)) startTimer()
                _sendLinkState.value = response
            }
        }
    }

    fun signInWithLink(intent: String) {
        val email = settings.getString(CURRENT_EMAIL_KEY)
        sharedScope.launch {
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
        sharedScope.launch {
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

    private fun startTimer() {
        sharedScope.launch {
            timer.startTimer().collect {
                _resendEmailTimer.value = it
            }
        }
    }

    private fun stopTimer() {
        timer.stopTimer()
        _resendEmailTimer.value = 0
        _sendLinkState.value = Response.Empty
    }

    fun resetSendLinkState() {
        _sendLinkState.value = Response.Empty
    }
}