package com.solita.devnotary.android.feature_notes.addNoteScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.feature_notes._sharedComponents.AddButton
import com.solita.devnotary.android.feature_notes._sharedComponents.ColorBallsRow
import com.solita.devnotary.android.feature_notes._sharedComponents.LocalNoteButtons
import com.solita.devnotary.android.feature_notes._sharedComponents.SaveNoteLocallyButton
import com.solita.devnotary.android.feature_notes.domain.NoteType
import com.solita.devnotary.android.feature_notes.domain.getNoteType
import com.solita.devnotary.android.ui.Colors
import com.solita.devnotary.android.ui.LocalElevation
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Shape
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.android.utils.Constants.NEW_NOTE
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import com.solita.devnotary.utils.formatIso8601ToString
import org.kodein.di.instance

@Composable
fun NoteContent(
    noteId: String? = null,
    noteTitle: String? = null,
    noteContent: String? = null,
    noteDateTime: String? = null,
    noteColor: String? = null,
    noteType: String = NEW_NOTE,
    editEnabled: Boolean = true
) {
    val thisNoteType = getNoteType(noteType)
    val viewModel: NotesViewModel by androidDi.instance()

    LaunchedEffect(Unit) {
        if (thisNoteType is NoteType.NewNote) viewModel.resetInputs()
        viewModel.isEditEnabled.value = editEnabled
        viewModel.fillContent(noteId, noteTitle, noteContent, noteDateTime, noteColor)
    }
    val isEditEnabledState = viewModel.isEditEnabled.collectAsState()
    val titleInputState = viewModel.titleInput.collectAsState()
    val contentInputState = viewModel.contentInput.collectAsState()
    val noteColorState = viewModel.noteColor.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(noteColorState.value).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                TextField(
                    value = titleInputState.value,
                    onValueChange = { if (it.length <= 30) viewModel.titleInput.value = it },
                    textStyle = Typography.h4,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White.copy(alpha = 0.0f),
                        disabledTextColor = Color.Black,
                    ), label = {
                        if (isEditEnabledState.value) {
                            Text(text = stringResource(id = R.string.note_title_label))
                        }
                    },
                    enabled = isEditEnabledState.value
                )
                TextField(
                    value = contentInputState.value,
                    onValueChange = { viewModel.contentInput.value = it },
                    shape = Shape().bitRoundedCornerShape,
                    textStyle = Typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White.copy(alpha = 0.0f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.0f),
                        focusedIndicatorColor = Color.White.copy(alpha = 0.0f),
                        disabledIndicatorColor = Color.White.copy(alpha = 0.0f),
                        disabledTextColor = Color.Black,
                    ),
                    label = {
                        if (isEditEnabledState.value) {
                            Text(text = stringResource(id = R.string.note_content_label))
                        }
                    },
                    enabled = isEditEnabledState.value
                )

                if (isEditEnabledState.value) ColorBallsRow()
                if (thisNoteType !is NoteType.NewNote) Text(
                    text = formatIso8601ToString(
                        noteDateTime.toString()
                    ), textAlign = TextAlign.End, style = Typography.body2,
                    color = Colors().LightBlack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = LocalSpacing.current.small,
                            bottom = LocalSpacing.current.xSmall
                        )
                )
            }
        }
        when (thisNoteType) {
            is NoteType.NewNote -> AddButton(modifier = Modifier.align(Alignment.End))
            is NoteType.LocalNote -> LocalNoteButtons(
                modifier = Modifier.align(Alignment.End),
                isEditEnabledState
            )
            is NoteType.SharedNote -> SaveNoteLocallyButton(modifier = Modifier.align(Alignment.End))
        }
    }
}

fun NotesViewModel.fillContent(
    noteId: String?,
    noteTitle: String?,
    noteContent: String?,
    noteDateTime: String?,
    noteColor: String?
) {
    if (noteId != null) this.noteId = noteId
    if (noteTitle != null) this.titleInput.value = noteTitle
    if (noteContent != null) this.contentInput.value = noteContent
    if (noteDateTime != null) this.noteDateTime = noteDateTime
    if (noteColor != null) this.noteColor.value = noteColor
}

@Preview
@Composable
fun PreviewAddNoteScreenContent() {
    NoteContent()
}