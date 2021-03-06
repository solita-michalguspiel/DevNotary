package com.solita.devnotary.android.feature_auth.signInScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.DefaultSpacer
import com.solita.devnotary.android.composables.TextIndicatingError
import com.solita.devnotary.android.theme.LocalColors
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.android.utils.TestTags
import com.solita.devnotary.android.utils.TestTags.TIMER_TAG
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import org.kodein.di.instance


@Preview
@Composable
fun PreviewSignInScreenContent() {
    SignInScreenContent()
}

@Composable
fun SignInScreenContent() {
    val authViewModel: AuthViewModel by di.instance()
    val emailResendTimer = authViewModel.resendEmailTimer.collectAsState(0).value
    val sendLinkState = authViewModel.sendLinkState.collectAsState(Response.Empty).value
    val emailAddressInput = authViewModel.emailAddressInput.collectAsState("")

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        DefaultSpacer()
        Text(
            text = stringResource(id = R.string.dev_notary),
            style = Typography.h2,
            modifier = Modifier.padding(horizontal = LocalSpacing.current.small)
        )
        DefaultSpacer()
        Text(text = stringResource(id = R.string.sign_in_with_email_prompt), style = Typography.h6)
        DefaultSpacer()
        TextField(
            value = emailAddressInput.value,
            onValueChange = { authViewModel.changeEmailAddress(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            maxLines = 1,
            singleLine = true
        )
        if (sendLinkState is Response.Error) {
            TextIndicatingError(errorMessage = sendLinkState.message)
        }
        if (emailResendTimer != 0) {
            Text(
                text = stringResource(id = R.string.resend_email_prompt, emailResendTimer),
                style = Typography.caption,
                color = LocalColors.current.LightBlack,
                modifier = Modifier.testTag(TIMER_TAG)
            )
        }
        DefaultSpacer()
        Button(
            modifier = Modifier.testTag(TestTags.SEND_LINK_BUTTON_TAG),
            onClick = { authViewModel.sendEmailLink() },
            enabled = emailResendTimer == 0 && sendLinkState !is Response.Loading
        ) {
            Text(text = stringResource(id = R.string.get_email))
        }
    }
}