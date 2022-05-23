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
import com.solita.devnotary.android.navigation.navigateToNoteScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesListContent(paddingValues: PaddingValues, navController: NavController) {

    val viewModel: NotesViewModel by di.instance()
    val notesState = viewModel.notes.collectAsState()

    val lazyListState = rememberLazyListState()
    viewModel.isScrollingUp.value = lazyListState.isScrollingUp()

    LaunchedEffect(Unit) {
        viewModel.listenToNoteListChanges()
    }

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
                        backgroundColor = com.solita.devnotary.android.theme.LocalColors.current.ThemeLightBlue
                    )
                })
            {
                LazyColumn(
                    Modifier
                        .fillMaxSize(), state = lazyListState
                ) {
                    stickyHeader {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = !lazyListState.isHavingMoreItemsThanDisplaying() || viewModel.isScrollingUp.collectAsState().value,
                                enter = slideInVertically(initialOffsetY = { -it }),
                                exit = slideOutVertically(targetOffsetY = { -it }),
                                modifier = Modifier.align(TopCenter)
                            ) {
                                NotesListStickyHeader()
                            }
                        }
                    items(notesState.value)
                    {
                        NotePreview(
                            note = it,
                            formattedDateTime = viewModel.formatDateTime(it.dateTime),
                            isFirst = notesState.value.first() == it
                        ) {
                            navController.navigateToNoteScreen(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListState.isHavingMoreItemsThanDisplaying(): Boolean {
    return layoutInfo.totalItemsCount > layoutInfo.visibleItemsInfo.size
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