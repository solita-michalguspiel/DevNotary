package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
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
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.feature_notes.domain.model.Note


@Composable
fun NotePreview(note: Note, formattedDateTime: String, navigateToNoteScreen: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
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
                Text(text = formattedDateTime, style = Typography.caption)
                TextButton(
                    onClick = {
                        navigateToNoteScreen()
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
        noteId = "testid",
        title = "Database plan",
        content = "Internal Database - For the private notes -  SQL,   Note(  ID,  Title,  Desc,  TimeDate,  Color,)" +
                "External Database - For shared notes -  Firebase  SharedNote(  ownerUserID,  sharedUserID,  Title,  Desc,  SharedDate,  Color)",
        dateTime = "2015-12-31T12:30:00Z",
        color = "green",
    )

    NotePreview(note = testNote,"2015.12.31 16:55") {}
}