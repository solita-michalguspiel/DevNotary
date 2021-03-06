package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.Typography

@Composable
fun TitleTextField(
    modifier: Modifier = Modifier, titleInput: String,
    isEditEnabled: Boolean,
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        value = titleInput,
        onValueChange = { onValueChange(it) },
        textStyle = Typography.h4,
        modifier = modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White.copy(alpha = 0.0f),
            disabledTextColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black
        ), label =
        { if (isEditEnabled) Text(text = stringResource(id = R.string.note_title_label)) },
        enabled = isEditEnabled,
        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)

    )
}