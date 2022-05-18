package com.solita.devnotary.android.feature_notes.notesListScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.Colors
import com.solita.devnotary.android.theme.LocalColors
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import com.solita.devnotary.feature_notes.presentation.Sort
import com.solita.devnotary.feature_notes.presentation.SortOptions
import org.kodein.di.instance

@Composable
fun NotesListStickyHeader() {

    val viewModel: NotesViewModel by androidDi.instance()
    val notesSearchPhrase = viewModel.noteSearchPhrase.collectAsState()

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = LocalSpacing.current.xSmall),
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
                            disabledColor = LocalColors.current.LightGray,
                            selectedColor = LocalColors.current.Blue,
                            unselectedColor = LocalColors.current.Gray
                        )
                    )
                    Text(text = sortOption.sortName)
                }
            }
        }
    }
}