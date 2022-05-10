package com.solita.devnotary.android.feature_auth.profileScreen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.solita.devnotary.android.R
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.domain.User
import com.solita.devnotary.feature_auth.presentation.AuthViewModel


@Composable
fun ProfileScreenContent(user: User, authViewModel: AuthViewModel, innerPaddingValues: PaddingValues){
    val spacing = LocalSpacing.current.default
    val largeSpacing = LocalSpacing.current.large
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(innerPaddingValues)) {
        val (welcomeText, logoutButton, callToAction) = createRefs()
        Text(
            text = stringResource(R.string.welcome_prompt,user.userEmail),
            modifier = Modifier.constrainAs(welcomeText){
                top.linkTo(parent.top, margin = spacing)
                start.linkTo(parent.start, margin = spacing)
                end.linkTo(parent.end, margin = spacing)
            }, style = Typography.h5
        )
        Text(text = stringResource(id = R.string.call_to_action_prompt),
            modifier = Modifier.constrainAs(callToAction){
                start.linkTo(parent.start, margin = spacing)
                end.linkTo(parent.end, margin = spacing)
                bottom.linkTo(logoutButton.top)
                top.linkTo(welcomeText.bottom)
            }
        )
        Button(
            onClick = { authViewModel.signOut() },
            modifier = Modifier.constrainAs(logoutButton){
                bottom.linkTo(parent.bottom, margin = largeSpacing)
                start.linkTo(parent.start, margin = spacing)
                end.linkTo(parent.end, margin = spacing)
            }
        ) {
            Text(text = stringResource(id = R.string.sign_out))
        }
    }
}