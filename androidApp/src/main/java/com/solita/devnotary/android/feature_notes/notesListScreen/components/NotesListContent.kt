package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.utils.Constants.NOTE_INDEX
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesListContent(paddingValues: PaddingValues, navController: NavController) {

    val viewModel: NotesViewModel by androidDi.instance()

    val lazyListState = rememberLazyListState()
    if (lazyListState.isScrollingUp()) viewModel.scrollingUp() else viewModel.scrollingDown()

    Column {

        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing.collectAsState().value),
                onRefresh = {
                    viewModel.getSharedNotes()
                    viewModel.isRefreshing.value = true
                },
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        contentColor = com.solita.devnotary.android.theme.LocalColors.current.Black,
                        backgroundColor = com.solita.devnotary.android.theme.LocalColors.current.LightBlue
                    )
                })
            {
                viewModel.prepareLists(
                    viewModel.localNotes.collectAsState().value,
                    viewModel.sharedNotes.collectAsState().value,
                    viewModel.noteSearchPhrase.collectAsState().value,
                    viewModel.getSelectedSort().collectAsState().value
                )
                LazyColumn(
                    Modifier
                        .fillMaxSize(), state = lazyListState
                ) {
                    stickyHeader { androidx.compose.animation.AnimatedVisibility(
                        visible = viewModel.isScrollingUp.collectAsState().value,
                        enter = slideInVertically(initialOffsetY = {-it} ),
                        exit = slideOutVertically(targetOffsetY = {-it}),
                        modifier = Modifier.align(TopCenter)
                    ) {
                        NotesListStickyHeader()
                    } }
                    items(viewModel.notes.value)

                    {
                        NotePreview(
                            note = it,
                            formattedDateTime = viewModel.formatDateTime(it.dateTime),
                            isFirst = viewModel.notes.value.first() == it
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
