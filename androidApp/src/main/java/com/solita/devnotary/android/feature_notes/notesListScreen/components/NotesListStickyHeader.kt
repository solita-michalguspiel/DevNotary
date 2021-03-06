package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solita.devnotary.android.R
import com.solita.devnotary.android.theme.Colors
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import com.solita.devnotary.feature_notes.presentation.notesList.Sort
import com.solita.devnotary.feature_notes.presentation.notesList.SortOptions
import org.kodein.di.instance

@Composable
fun NotesListStickyHeader(modifier: Modifier = Modifier) {

    val viewModel: NotesListViewModel by di.instance()
    val notesSearchPhrase = viewModel.noteSearchPhrase.collectAsState()
    val focusManager = LocalFocusManager.current

    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(vertical = LocalSpacing.current.xSmall)
            ,
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
            placeholderText = stringResource(id = R.string.search),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
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
                modifier = Modifier.padding(0.dp).background(MaterialTheme.colors.surface),
                onDismissRequest = { viewModel.isSortOptionDropdownOpen.value = false }) {
                DropdownMenuItem(onClick = {}) {
                    SortingRadioButtons(
                        currentlySelectedSort = viewModel.selectedSort
                            .collectAsState().value,
                        selectSort = { viewModel.changeSortSelection(it) },
                        closeDropdown = { viewModel.isSortOptionDropdownOpen.value = false }
                    )
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (currentlySelectedSort::class == (sortOption.sort::class)),
                        onClick = {
                            sortOption.sort.click()
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary,
                            unselectedColor = MaterialTheme.colors.onSurface
                        )
                    )
                    Text(text = sortOption.sortName, color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}