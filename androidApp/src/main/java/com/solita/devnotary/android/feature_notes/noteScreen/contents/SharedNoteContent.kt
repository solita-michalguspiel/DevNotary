package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.Constants.CLEAR_NOTE
import com.solita.devnotary.android.R
import com.solita.devnotary.android.feature_notes._sharedComponents.SharedNoteButtons
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance

@Composable
fun SharedNoteContent(popBackStack : () -> Unit) {

    val viewModel: NoteDetailViewModel by com.solita.devnotary.di.di.instance()
    val displayedNoteState = viewModel.displayedNote.collectAsState(initial = CLEAR_NOTE)

    val noteSharingState = viewModel.noteSharingState.collectAsState().value

    LaunchedEffect(noteSharingState){
        if(noteSharingState is Response.Success){
            popBackStack()
        }
        viewModel.restartNoteSharingState()
        viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = false
    }

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(displayedNoteState.value.color).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                when (val ownerUser = viewModel.noteOwnerUser.collectAsState().value) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        Text(
                            text = stringResource(
                                id = R.string.shared_by,
                                ownerUser.data.userEmail
                            ),
                            Modifier
                                .fillMaxWidth()
                                .padding(LocalSpacing.current.xSmall),
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                    else -> {}
                }
                TitleTextField(titleInput = displayedNoteState.value.title, isEditEnabled = false)

                ContentTextField(
                    contentInput = displayedNoteState.value.content,
                    modifier = Modifier.weight(1.0f),
                    isEditEnabled = false
                )
                Text(
                    text = stringResource(
                        R.string.note_time_date_stamp,
                        viewModel.formatDateTime(displayedNoteState.value.dateTime)
                    ),
                    modifier = Modifier
                        .align(End)
                        .padding(
                            end = LocalSpacing.current.default,
                            bottom = LocalSpacing.current.default
                        ),
                    style = Typography.body2,
                    color = Color.Black
                )
            }
        }
        SharedNoteButtons(modifier = Modifier.align(End))
    }

}