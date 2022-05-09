package com.solita.devnotary.feature_auth.presentation

import com.russhwolf.settings.Settings
import com.solita.devnotary.Constants.CURRENT_EMAIL_KEY
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.Response
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.domain.use_case.AuthUseCases
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance

class AuthViewModel(val intent: String, dependencyInjection: DI = di) : ComposableViewModel,
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

    init {
        if (intent != "null" && !isUserAuthenticated) {
            signInWithLink()
        }
    }

    fun getCurrentUserDocument() {
        viewModelScope.launch {
            useCases.getCurrentUserDocument.invoke().collect { response ->
                _userState.value = response
                if (response is Response.Error) reactToError(response.message)
            }
        }
    }

    fun sendEmailLink(emailAddress: String) {
        settings.putString(CURRENT_EMAIL_KEY, emailAddress)
        viewModelScope.launch {
            useCases.sendEmailLink.invoke(emailAddress).collect { response ->
                _sendLinkState.value = response
                if (response is Response.Error) reactToError(response.message)
                println(response)
            }
        }
    }

    fun signInWithLink() {
        val email = settings.getString(CURRENT_EMAIL_KEY)
        viewModelScope.launch {
            useCases.signInWithEmailLink(email = email, emailLink = intent).collect { response ->
                _userAuthState.value = response
                if (response is Response.Error) reactToError(response.message)

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

    fun reactToError(error : String){
        errorMessageState.value = error
        openDialogState.value = true

    }

    fun resetError(){
        openDialogState.value = false
        errorMessageState.value = ""
    }

}