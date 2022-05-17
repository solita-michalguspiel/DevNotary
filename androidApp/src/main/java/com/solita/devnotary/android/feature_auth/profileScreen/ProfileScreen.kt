package com.solita.devnotary.android.feature_auth

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.feature_auth.profileScreen.components.ProfileScreenContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
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
        Scaffold(bottomBar = { MyBottomNavigationDrawer(navController = navController )}) { innerPadding ->
            if (authState.value == Response.Success(false)) {
                navController.popBackStack()
                navController.navigate(Screen.SignInScreen.route)
            }
            when (val user = userState.value) {
                is Response.Loading -> {
                    ProgressIndicator()
                }
                is Response.Success -> {
                  ProfileScreenContent(user.data, { authViewModel.signOut() },innerPadding)
                }
                else -> {}
            }
        }
    }
}
