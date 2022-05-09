package com.solita.devnotary.android.feature_auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.Dialog
import com.solita.devnotary.android.components.ProgressIndicator
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.feature_auth.signInScreen.SignInScreenContent
import com.solita.devnotary.android.feature_auth.signInScreen.components.ShowDialog
import com.solita.devnotary.android.signInIntent
import com.solita.devnotary.android.utils.Constants
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInScreen(navController: NavController) {

    val localContext = LocalContext.current
    BackHandler {
        (localContext as? Activity)?.finish()
    }

    val linkSentMessage = stringResource(id = R.string.link_sent)

    val authViewModel: AuthViewModel by androidDi.instance()

    val openDialogState = authViewModel.openDialogState.collectAsState()
    val userAuthState = authViewModel.userAuthState.collectAsState()
    val sendEmailLinkState = authViewModel.sendLinkState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    if (signInIntent.data.toString() != Constants.NULL &&
        userAuthState.value !is Response.Success<*>
    ) {
        authViewModel.signInWithLink(signInIntent.data.toString())
        signInIntent = Intent(Constants.NULL)
    }

    LaunchedEffect(Unit) {
        if (authViewModel.isUserAuthenticated) navController.navigateToProfileScreen()
    }

    if (!authViewModel.isUserAuthenticated) {
        if (openDialogState.value) ShowDialog(authViewModel = authViewModel)

        Scaffold(scaffoldState = scaffoldState) {
            when (sendEmailLinkState.value) {
                is Response.Loading -> ProgressIndicator()
                is Response.Success -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(linkSentMessage)
                    }
                }
                else -> Log.e("TAG", "Error!")
            }
            if (sendEmailLinkState.value == Response.Loading) ProgressIndicator()
            if (userAuthState.value == Response.Success(true)) navController.navigateToProfileScreen()
            SignInScreenContent(authViewModel)
        }
    }
}

fun NavController.navigateToProfileScreen() {
    this.navigate(Screen.ProfileScreen.route) {
        popUpTo(Screen.ProfileScreen.route)
    }
}

