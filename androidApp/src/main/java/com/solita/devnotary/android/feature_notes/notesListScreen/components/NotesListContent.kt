package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.solita.devnotary.android.navigation.navigateToNoteDetailsScreen
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesListContent(navController: NavController) {

    val notesListViewModel: NotesListViewModel by di.instance()
    val notesState = notesListViewModel.notes.collectAsState(listOf())

    val lazyListState = rememberLazyListState()
    notesListViewModel.isScrollingUp.value = lazyListState.isScrollingUp()

    LaunchedEffect(Unit) {
        notesListViewModel.listenToNoteListChanges()
    }

    Column {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = notesListViewModel.isRefreshing.collectAsState().value),
                onRefresh = {
                    notesListViewModel.getSharedNotes()
                    notesListViewModel.isRefreshing.value = true
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
                                visible = !lazyListState.isHavingMoreItemsThanDisplaying() || notesListViewModel.isScrollingUp.collectAsState().value,
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
                            modifier = Modifier.padding(horizontal = LocalSpacing.current.default),
                            note = it,
                            formattedDateTime = notesListViewModel.formatDateTime(it.dateTime),
                            isFirst = notesState.value.first() == it
                        ) {
                            navController.navigateToNoteDetailsScreen(it)
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