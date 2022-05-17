package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.Shape
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun ContentTextField(
    contentInput: String,
    modifier: Modifier,
    isEditEnabled: Boolean,
    onValueChange: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = contentInput,
        onValueChange = { onValueChange(it) },
        shape = Shape().bitRoundedCornerShape,
        textStyle = Typography.body1,
        modifier = modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White.copy(alpha = 0.0f),
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.0f),
            focusedIndicatorColor = Color.White.copy(alpha = 0.0f),
            disabledIndicatorColor = Color.White.copy(alpha = 0.0f),
            disabledTextColor = Color.Black,
        ),
        label = {
            if (isEditEnabled) Text(text = stringResource(id = R.string.note_content_label))
        },
        enabled = isEditEnabled,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}
