package com.solita.devnotary.android.feature_notes.localNotesScreen.components

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
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalElevation
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.ui.Shape
import com.solita.devnotary.android.ui.Typography
import com.solita.devnotary.android.utils.Constants.LOCAL_NOTE
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.database.Note
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
                        navController
                            .navigate(
                                Screen.LocalNoteDetailsScreen.withArgs(
                                    note.note_id,
                                    note.title,
                                    note.content,
                                    note.date_time,
                                    note.color)
                            )
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