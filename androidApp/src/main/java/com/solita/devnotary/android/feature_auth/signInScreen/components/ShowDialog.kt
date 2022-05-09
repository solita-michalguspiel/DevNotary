package com.solita.devnotary.android.feature_auth.signInScreen.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.components.Dialog
import com.solita.devnotary.feature_auth.presentation.AuthViewModel

@Composable
fun ShowDialog(authViewModel: AuthViewModel) {
    Dialog(onDismissRequest = { authViewModel.openDialogState.value = false },
        title = stringResource(id = R.string.error),
        text = authViewModel.errorMessageState.value,
        dismissButton = {
            Button(onClick = { authViewModel.resetError() }) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    )
}