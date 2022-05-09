package com.solita.devnotary.android.feature_auth.signInScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.components.DefaultSpacer
import com.solita.devnotary.android.ui.LocalColors
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.domain.ComposableViewModel
import com.solita.devnotary.domain.ComposeViewModel
import com.solita.devnotary.feature_auth.presentation.AuthViewModel


@Preview
@Composable
fun PreviewSignInScreenContent() {
    SignInScreenContent(viewModel = ComposeViewModel())
}


@Composable
fun SignInScreenContent(viewModel: ComposableViewModel) {

    var emailAddressInput by remember {
        mutableStateOf("")
    }
    val authViewModel = viewModel as AuthViewModel

    val emailResendTimer = authViewModel.resendEmailTimer.collectAsState().value

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
            value = emailAddressInput,
            onValueChange = { emailAddressInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        DefaultSpacer()
        if (emailResendTimer != 0) {
            Text(
                text = stringResource(id = R.string.resend_email_prompt, emailResendTimer),
                style = Typography.caption,
                color = LocalColors.current.LightBlack
            )
        }
        Button(onClick = { authViewModel.sendEmailLink(emailAddressInput) }, enabled = emailResendTimer == 0) {
            Text(text = stringResource(id = R.string.get_email))
        }
    }
}