package com.solita.devnotary.android.feature_auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.feature_auth.signInScreen.SignInScreenContent
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.utils.Constants
import com.solita.devnotary.android.utils.signInIntent
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(navController: NavController) {

    val localContext = LocalContext.current
    BackHandler {
        (localContext as? Activity)?.finish()
    }

    val linkSentMessage = stringResource(id = R.string.link_sent)

    val authViewModel: AuthViewModel by di.instance()

    val userAuthState = authViewModel.userAuthState.collectAsState(Response.Empty)
    val sendEmailLinkState = authViewModel.sendLinkState.collectAsState(Response.Empty)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    if (signInIntent.data.toString() != Constants.NULL &&
        userAuthState.value !is Response.Success<*>
    ) {
        authViewModel.signInWithLink(signInIntent.data.toString())
        signInIntent = Intent(Constants.NULL)
    }

    LaunchedEffect(Unit) {
        if (authViewModel.isUserAuthenticated) navController.navigateToProfileScreen()
    }

    LaunchedEffect(sendEmailLinkState.value) {
        if (sendEmailLinkState.value is Response.Success) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(linkSentMessage)
            }
            keyboardController?.hide()
            authViewModel.resetSendLinkState()
        }
    }

    if (!authViewModel.isUserAuthenticated) {
        if (authViewModel.openDialogState.collectAsState().value) {
            ShowErrorDialog(authViewModel = authViewModel)
        }
        Scaffold(scaffoldState = scaffoldState) {
            if (sendEmailLinkState.value == Response.Loading) ProgressIndicator()
            if (userAuthState.value == Response.Success(true)) navController.navigateToProfileScreen()
            SignInScreenContent()
        }
    }
}

fun NavController.navigateToProfileScreen() {
    this.navigate(Screen.ProfileScreen.route) {
        popUpTo(Screen.ProfileScreen.route)
    }
}

@Composable
fun ShowErrorDialog(authViewModel: AuthViewModel){
    AlertDialog(
        onDismissRequest = { authViewModel.openDialogState.value = false },
        title = { Text(text = stringResource(id = R.string.error)) },
        text = { Text(text = authViewModel.errorMessageState.value) },
        confirmButton = {},
        dismissButton = {
            Button(onClick = { authViewModel.resetError() }) {
                Text(text = stringResource(id = R.string.back))
            }
        })
}