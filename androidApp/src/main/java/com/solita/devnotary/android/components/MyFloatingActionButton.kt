package com.solita.devnotary.android.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalColors
import com.solita.devnotary.domain.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun MyFloatingActionButton(navController: NavController) {
    val viewModel : NotesViewModel by androidDi.instance()
    FloatingActionButton(backgroundColor = LocalColors.current.LightBlue,
        onClick = {
            viewModel.changeNoteScreenState(NoteScreenState.NewNote)
            navController.navigate(Screen.NoteScreen.route)
        }) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.fab_desc)
        )
    }
}