package com.solita.devnotary.android.feature_auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.feature_auth.profileScreen.components.ProfileScreenContent
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import org.kodein.di.instance

@Composable
fun ProfileScreen(paddingValues: PaddingValues) {
    val authViewModel: AuthViewModel by di.instance()

    val userState = authViewModel.userState.collectAsState(Response.Empty)

    LaunchedEffect(Unit) {
        authViewModel.getCurrentUserDocument()
    }
            when (val user = userState.value) {
                is Response.Loading -> {
                    ProgressIndicator()
                }
                is Response.Success -> {
                  ProfileScreenContent(user.data, { authViewModel.signOut() },paddingValues)
                }
                else -> {}
            }
}
