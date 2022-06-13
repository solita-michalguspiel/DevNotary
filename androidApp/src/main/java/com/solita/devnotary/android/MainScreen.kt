package com.solita.devnotary.android

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.android.navigation.Navigation
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import org.kodein.di.instance

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val authViewModel: AuthViewModel by di.instance()
    val userAuthState = authViewModel.userAuthState.collectAsState()
    val navController = rememberAnimatedNavController()
    val scaffoldState = rememberScaffoldState()
    var isUserAuthenticated = authViewModel.isUserAuthenticated

    when (val userAuthStateData = userAuthState.value) {
        is Response.Success<Boolean> -> isUserAuthenticated = userAuthStateData.data
        else -> {}
    }
    if (isUserAuthenticated) {
        Scaffold(
            scaffoldState = scaffoldState,
            contentColor = MaterialTheme.colors.primary,
            bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        ) {
            Navigation(it, navController = navController,scaffoldState)
        }
    } else SignInScreen()
}