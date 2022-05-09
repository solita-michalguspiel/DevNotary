package com.solita.devnotary.android.feature_auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.LargeSpacer
import com.solita.devnotary.android.components.ProgressIndicator
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import org.kodein.di.instance

@Composable
fun ProfileScreen(navController: NavController) {
    val authViewModel: AuthViewModel by androidDi.instance()

    val authState = authViewModel.userAuthState.collectAsState()
    val userState = authViewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getCurrentUserDocument()
    }

    if (authViewModel.isUserAuthenticated) {
        Scaffold {
            if (authState.value == Response.Success(false)) {
                navController.popBackStack()
                navController.navigate(Screen.SignInScreen.route)
            }

            when (val user = userState.value) {
                is Response.Loading -> {
                    ProgressIndicator()
                }
                is Response.Success -> {
                    Column {
                        LargeSpacer()
                        Text(
                            text = "Hello! ${user.data.userEmail}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(
                                LocalSpacing.current.small
                            )
                        )
                        Button(
                            onClick = { authViewModel.signOut() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Log out")
                        }
                    }
                }
                else -> {}
            }


        }
    }
}