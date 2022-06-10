package com.solita.devnotary.android.feature_notes.noteInteractionScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.LocalSpacing

@Composable
fun SaveButton(modifier: Modifier, onClick : () -> Unit) {
    Button(
        onClick = {
            onClick()
        }, modifier = modifier.padding(LocalSpacing.current.small)
    ) {
        Text(text = stringResource(id = R.string.save_note))
    }
}
@Composable
fun AddButton(modifier: Modifier, onClick : () -> Unit) {
    Button(
        onClick = {
            onClick()
        }, modifier = modifier.padding(LocalSpacing.current.small)
    ) {
        Text(text = stringResource(id = R.string.add_note))
    }
}