package com.solita.devnotary.android.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    title: String,
    dismissButton: @Composable (() -> Unit)? = null,
    text: String,
) {

    AlertDialog(
        modifier = modifier, onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {Text(text = text)},
        confirmButton = {},
        dismissButton = dismissButton
    )

}