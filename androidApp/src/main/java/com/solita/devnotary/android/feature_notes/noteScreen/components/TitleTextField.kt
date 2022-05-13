package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun TitleTextField(titleInputState: State<String>, isEditEnabled: Boolean) {
    val viewModel: NotesViewModel by androidDi.instance()
    TextField(
        value = titleInputState.value,
        onValueChange = { if (it.length <= 30) viewModel.titleInput.value = it },
        textStyle = Typography.h4,
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White.copy(alpha = 0.0f),
            disabledTextColor = Color.Black,
        ), label =
        { if (isEditEnabled) Text(text = stringResource(id = R.string.note_title_label)) },
        enabled = isEditEnabled
    )
}