package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.feature_notes._sharedComponents.LocalNoteButtons
import com.solita.devnotary.android.feature_notes._sharedComponents.SharedNoteButtons
import com.solita.devnotary.android.feature_notes.domain.NoteColor
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalColors
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.presentation.noteDetail.NoteDetailViewModel
import org.kodein.di.instance


@Composable
fun NoteDetailsContent(
    note: Note,
    navigateToUsersWithAccessScreen: () -> Unit,
    navigateToNewNote: () -> Unit,
    navigateToEditNote : () -> Unit,
    popBackStack : () -> Unit,
    paddingValues: PaddingValues
) {
    val viewModel: NoteDetailViewModel by di.instance()
    val displayedNoteState = viewModel.displayedNote.collectAsState()
    val noteSharingState = viewModel.noteSharingState.collectAsState().value
    val isLoading = remember{
        mutableStateOf(false)
    }
    LaunchedEffect(noteSharingState){
        when(noteSharingState){
            is Response.Success -> if (!viewModel.isShareDialogOpen.value){
                popBackStack()
                viewModel.restartNoteSharingState()
            }
            is Response.Loading -> {isLoading.value = true}
            else -> {isLoading.value = false}
        }

        viewModel.isConfirmDeleteAccessFromSharedNoteDialogOpen.value = false
    }
    Box(modifier = Modifier.padding(paddingValues)) {
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
                        is Response.Success -> {
                            if (!note.isLocal()) Text(
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

                    Box {
                        TitleTextField(
                            modifier = Modifier.padding(top = LocalSpacing.current.xSmall),
                            titleInput = displayedNoteState.value.title,
                            isEditEnabled = false
                        )
                        if (note.isLocal()) ShareRelatedButtonRow(
                            modifier = Modifier.align(TopEnd),
                            navigateToUsersWithAccessScreen = navigateToUsersWithAccessScreen,
                            openSharingDialog = { viewModel.isShareDialogOpen.value = true },
                            getUsersWithAccess = { viewModel.getUsersWithAccess() }
                        )
                    }
                    ContentTextField(
                        contentInput = displayedNoteState.value.content,
                        modifier = Modifier.weight(1.0f), isEditEnabled = false
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
            if (note.isLocal()) LocalNoteButtons(
                modifier = Modifier.align(End),
                navigateToNewNote,
                navigateToEditNote
            )
            else SharedNoteButtons(modifier = Modifier.align(End), isLoading.value)
        }
        if(isLoading.value && !viewModel.isShareDialogOpen.value) ProgressIndicator()
    }
}

@Composable
fun ShareRelatedButtonRow(
    modifier: Modifier,
    navigateToUsersWithAccessScreen: () -> Unit,
    openSharingDialog: () -> Unit,
    getUsersWithAccess: () -> Unit
) {
    Row(modifier) {
        IconButton(
            onClick = { openSharingDialog() },
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(
                    id = R.string.menu
                ), tint = LocalColors.current.Black


            )
        }
        IconButton(
            onClick = {
                getUsersWithAccess()
                navigateToUsersWithAccessScreen()
                      },
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = stringResource(
                    id = R.string.menu
                ),
                tint = LocalColors.current.Black
            )
        }
    }
}
