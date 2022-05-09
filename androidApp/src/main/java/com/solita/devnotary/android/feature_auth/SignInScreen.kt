package com.solita.devnotary.android.feature_auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.DefaultSpacer
import com.solita.devnotary.android.components.Dialog
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.ComposeViewModel
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import org.kodein.di.instance

@Composable
fun SignInScreen(navController: NavController) {

    val localContext = LocalContext.current
    BackHandler {
        (localContext as? Activity)?.finish()
    }

    val authViewModel: AuthViewModel by androidDi.instance()

    val openDialogState = authViewModel.openDialogState.collectAsState()

    Log.i("LOG", authViewModel.isUserAuthenticated.toString())

    LaunchedEffect(Unit) {
        if (authViewModel.isUserAuthenticated) navController.navigate(Screen.LocalNotesScreen.route) {
            popUpTo(Screen.LocalNotesScreen.route)
        }
    }
    if (!authViewModel.isUserAuthenticated) {

        if (openDialogState.value) {
            Dialog(onDismissRequest = { authViewModel.openDialogState.value = false },
                title = "Error",
                text = authViewModel.errorMessageState.value,
                dismissButton = {
                    Button(onClick = {authViewModel.resetError() }) {
                        Text(text = "Back")
                    }
                }
            )
        }

        Scaffold {
            SignInScreenContent(authViewModel)
        }
    }
}

@Preview
@Composable
fun previewSignInScreenContent() {
    SignInScreenContent(viewModel = ComposeViewModel())
}


@Composable
fun SignInScreenContent(viewModel: ComposableViewModel) {

    var emailAddressInput by remember {
        mutableStateOf("")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        DefaultSpacer()
        Text(
            text = "Dev Notary",
            style = Typography.h2,
            modifier = Modifier.padding(horizontal = LocalSpacing.current.small)
        )
        DefaultSpacer()
        Text(text = "Sign in with email", style = Typography.h6)
        DefaultSpacer()
        TextField(value = emailAddressInput, onValueChange = { emailAddressInput = it })
        DefaultSpacer()
        Button(onClick = { (viewModel as AuthViewModel).sendEmailLink(emailAddressInput) }) {
            Text(text = "Get email")
        }
    }
}