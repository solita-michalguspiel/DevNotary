package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.android.theme.Colors
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.utils.Constants.NOTE_INDEX
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import com.solita.devnotary.feature_notes.presentation.Sort
import com.solita.devnotary.feature_notes.presentation.SortOptions
import org.kodein.di.instance

@Composable
fun NotesListContent(paddingValues: PaddingValues, navController: NavController) {
    val viewModel: NotesViewModel by androidDi.instance()

    val localNotesState = viewModel.localNotes.collectAsState()
    val sharedNotesState = viewModel.sharedNotes.collectAsState()
    val notesSearchPhrase = viewModel.noteSearchPhrase.collectAsState()

    val isRefreshing = viewModel.isRefreshing.collectAsState()

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
            verticalAlignment = CenterVertically
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
            Box(modifier = Modifier.weight(0.1f)) {
                IconButton(
                    onClick = { viewModel.isSortOptionDropdownOpen.value = true },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = stringResource(id = R.string.sort)
                    )
                }
                DropdownMenu(
                    expanded = viewModel.isSortOptionDropdownOpen.collectAsState().value,
                    modifier = Modifier.padding(0.dp),
                    onDismissRequest = { viewModel.isSortOptionDropdownOpen.value = false }) {
                    DropdownMenuItem(onClick = {}) {
                        SortingRadioButtons(
                            currentlySelectedSort = viewModel.getSelectedSort()
                                .collectAsState().value,
                            selectSort = { viewModel.changeSortSelection(it) },
                            closeDropdown = { viewModel.isSortOptionDropdownOpen.value = false }
                        )
                    }
                }
            }
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
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
}

@Composable
private fun SortingRadioButtons(
    modifier: Modifier = Modifier,
    currentlySelectedSort: Sort,
    selectSort: (Sort) -> Unit,
    closeDropdown: () -> Unit
) {
    fun Sort.click() {
        selectSort(this)
        closeDropdown()
    }

    val radioOptions = SortOptions.values()
    Column(modifier = modifier) {
        radioOptions.forEach { sortOption ->
            TextButton(onClick = {
                sortOption.sort.click()
            }, modifier = Modifier.height(50.dp)) {
                Row(verticalAlignment = CenterVertically) {
                    RadioButton(
                        selected = (currentlySelectedSort::class == (sortOption.sort::class)),
                        onClick = {
                            sortOption.sort.click()
                        },
                        colors = RadioButtonDefaults.colors(
                            disabledColor = com.solita.devnotary.android.theme.LocalColors.current.LightGray,
                            selectedColor = com.solita.devnotary.android.theme.LocalColors.current.Blue,
                            unselectedColor = com.solita.devnotary.android.theme.LocalColors.current.Gray
                        )
                    )
                    Text(text = sortOption.sortName)
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
