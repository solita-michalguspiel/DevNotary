package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.theme.Colors
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.Constants.NOTE_INDEX
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun NotesListContent(paddingValues: PaddingValues, navController: NavController) {
    val viewModel: NotesViewModel by androidDi.instance()

    val localNotesState = viewModel.localNotes.collectAsState()
    val sharedNotesState = viewModel.sharedNotes.collectAsState()
    val notesSearchPhrase = viewModel.noteSearchPhrase.collectAsState()
    viewModel.prepareLists(
        localNotesState.value,
        sharedNotesState.value
    )
    val lazyListState = rememberLazyListState()
    if (lazyListState.isScrollingUp()) viewModel.showFab() else viewModel.hideFab()
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = LocalSpacing.current.xSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                value = notesSearchPhrase.value,
                onValueChange = { viewModel.noteSearchPhrase.value = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = Colors().Gray
                    )
                },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = LocalSpacing.current.default),
                fontSize = 14.sp,
                placeholderText = stringResource(id = R.string.search)
            )
            IconButton(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .weight(0.1f)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = stringResource(id = R.string.sort)
                )
            }
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(Modifier.fillMaxSize(), state = lazyListState) {
                items(viewModel.notes.value)
                {
                    NotePreview(
                        note = it,
                        formattedDateTime = viewModel.formatDateTime(it.dateTime)
                    ) {
                        navController.navigate(
                            Screen.NoteScreen.route +
                                    "?$NOTE_INDEX=${viewModel.notes.value.indexOf(it)}"
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
