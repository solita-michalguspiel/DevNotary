package com.solita.devnotary.android.feature_auth.profileScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.android.utils.TestTags.SIGN_OUT_BUTTON_TAG
import com.solita.devnotary.domain.User


@Composable
fun ProfileScreenContent(user: User, signOut : () -> Unit, innerPaddingValues: PaddingValues){
    val spacing = LocalSpacing.current.default
    val largeSpacing = LocalSpacing.current.large
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(innerPaddingValues)) {
        val (welcomeText, logoutButton) = createRefs()
        Column(Modifier.constrainAs(welcomeText){
            top.linkTo(parent.top, margin = spacing)
            start.linkTo(parent.start, margin = spacing)
        }.padding(LocalSpacing.current.default)){
            Text(text = "Hello!", style = Typography.h3)
            Text(text = user.userEmail,style = Typography.h4)
        }
        Button(
            onClick = { signOut() },
            modifier = Modifier.constrainAs(logoutButton){
                bottom.linkTo(parent.bottom, margin = largeSpacing)
                start.linkTo(parent.start, margin = spacing)
                end.linkTo(parent.end, margin = spacing)
            }.testTag(SIGN_OUT_BUTTON_TAG)
        ) {
            Text(text = stringResource(id = R.string.sign_out))
        }
    }
}