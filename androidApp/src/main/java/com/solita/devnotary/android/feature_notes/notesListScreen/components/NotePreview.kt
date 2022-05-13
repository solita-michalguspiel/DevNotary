package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Shape
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.android.utils.Constants.NOTE_COLOR
import com.solita.devnotary.android.utils.Constants.NOTE_CONTENT
import com.solita.devnotary.android.utils.Constants.NOTE_ID
import com.solita.devnotary.android.utils.Constants.NOTE_TIME_DATE
import com.solita.devnotary.android.utils.Constants.NOTE_TITLE
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.database.Note
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance


@Composable
fun NotePreview(note: Note, navController: NavController) {
    val viewModel: NotesViewModel by androidDi.instance()


    Card(
        shape = Shape().bitRoundedCornerShape,
        backgroundColor = NoteColor(note.color).getColor().copy(alpha = 0.9f),
        elevation = LocalElevation.current.medium,
        modifier = Modifier.padding(
            horizontal = LocalSpacing.current.xxSmall,
            vertical = LocalSpacing.current.default
        )
    ) {
        Column {
            Text(
                text = note.title,
                style = Typography.h5,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(
                    start =
                    LocalSpacing.current.small, top = LocalSpacing.current.small
                )
            )
            Text(
                text = note.content,
                style = Typography.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(
                        LocalSpacing.current.default
                    )
                    .heightIn(0.dp, 30.dp)
            )
            Row(
                Modifier
                    .padding(
                        horizontal = LocalSpacing.current.default,
                        vertical = LocalSpacing.current.xSmall
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = viewModel.formatDateTime(note.date_time), style = Typography.caption)
                TextButton(
                    onClick = {
                        viewModel.changeNoteScreenState(NoteScreenState.LocalNote)
                        navController
                            .navigate(
                                Screen.NoteScreen.route +
                                        "?$NOTE_ID=${note.note_id}&$NOTE_TITLE=${note.title}&$NOTE_CONTENT=${note.content}&$NOTE_TIME_DATE=${note.date_time}&$NOTE_COLOR=${note.color}")
                    },
                    modifier = Modifier
                ) {
                    Text(text = stringResource(id = R.string.view_details))
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewNotePreview() {
    val testNote = Note(
        "testid",
        "Database plan",
        "Internal Database - For the private notes -  SQL,   Note(  ID,  Title,  Desc,  TimeDate,  Color,)" +
                "External Database - For shared notes -  Firebase  SharedNote(  ownerUserID,  sharedUserID,  Title,  Desc,  SharedDate,  Color)",
        "2015-12-31T12:30:00Z", "green",
    )

    NotePreview(note = testNote, rememberNavController())

}