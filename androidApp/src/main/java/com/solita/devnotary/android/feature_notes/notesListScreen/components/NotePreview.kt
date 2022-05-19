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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.*
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.feature_notes.domain.model.Note


@Composable
fun NotePreview(
    note: Note,
    formattedDateTime: String,
    isFirst: Boolean = false,
    navigateToNoteScreen: () -> Unit
) {
    Card(
        shape = LocalShapes.current.mediumRoundedCornerShape,
        backgroundColor = NoteColor(note.color).getColor(),
        elevation = LocalElevation.current.medium,
        modifier = Modifier.padding(
            start = LocalSpacing.current.xxSmall,
            end = LocalSpacing.current.xxSmall,
            bottom = LocalSpacing.current.default,
            top = if (!isFirst) LocalSpacing.current.default else LocalSpacing.current.xSmall
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
                ),
                color = LocalColors.current.Black
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
                    .heightIn(0.dp, 30.dp),
                color = LocalColors.current.Black
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
                Text(
                    text = formattedDateTime,
                    style = Typography.caption,
                    color = LocalColors.current.Black
                )
                TextButton(
                    onClick = {
                        navigateToNoteScreen()
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = stringResource(id = R.string.view_details),
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
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
        content = "SOME RANDOM CONTENT",
        dateTime = "2015-12-31T12:30:00Z",
        color = "red",
    )

    NotePreview(note = testNote, "2015.12.31 16:55") {}
}