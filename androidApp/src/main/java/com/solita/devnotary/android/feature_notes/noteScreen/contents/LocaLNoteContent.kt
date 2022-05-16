package com.solita.devnotary.android.feature_notes.noteScreen.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.feature_notes._sharedComponents.LocalNoteButtons
import com.solita.devnotary.android.feature_notes.noteScreen.components.ContentTextField
import com.solita.devnotary.android.feature_notes.noteScreen.components.TitleTextField
import com.solita.devnotary.android.theme.LocalElevation
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.NoteColor
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance


@Composable
fun LocalNoteContent(
    navigateToUsersWithAccessScreen : () -> Unit
) {
    val viewModel: NotesViewModel by androidDi.instance()
    val titleInputState = viewModel.titleInput.collectAsState()
    val contentInputState = viewModel.contentInput.collectAsState()
    val noteColorState = viewModel.noteColor.collectAsState()
    val menuOpenState = viewModel.isShareDropdownExpanded.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .weight(1.0f),
            backgroundColor = NoteColor(noteColorState.value).getColor(),
            elevation = LocalElevation.current.medium
        ) {
            Column {
                Box {
                    TitleTextField(titleInputState = titleInputState, false)
                    Box(Modifier.align(TopEnd)) {
                        IconButton(
                            onClick = { viewModel.isShareDropdownExpanded.value = true },
                        ) {
                            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = stringResource(
                                id = R.string.menu
                            ))
                        }
                        DropdownMenu(
                            expanded = menuOpenState.value,
                            onDismissRequest = { viewModel.isShareDropdownExpanded.value = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                viewModel.isShareDialogOpen.value = true
                                viewModel.isShareDropdownExpanded.value = false
                            }) {
                                Text(text = stringResource(id = R.string.share_note))
                            }
                            DropdownMenuItem(onClick = {
                                viewModel.getUsersWithAccess()
                                navigateToUsersWithAccessScreen()
                                viewModel.isShareDropdownExpanded.value = false
                                 }) {
                                Text(text = stringResource(id = R.string.users_with_access))
                            }
                        }
                        

                    }
                }
                ContentTextField(
                    contentInputState = contentInputState,
                    modifier = Modifier.weight(1.0f), false
                )
            }
        }
        LocalNoteButtons(modifier = Modifier.align(End), false)
    }
}
