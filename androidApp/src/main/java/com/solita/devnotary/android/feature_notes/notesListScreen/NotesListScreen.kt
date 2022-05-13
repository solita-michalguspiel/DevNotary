package com.solita.devnotary.android.feature_notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes._sharedUtils.showScaffold
import com.solita.devnotary.android.feature_notes.notesListScreen.components.LocalNotesContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NoteScreenState
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNotesScreen(navController: NavController) {

    val notesViewModel: NotesViewModel by androidDi.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val isFabVisible = notesViewModel.isFabVisible.collectAsState()

    Scaffold(
        bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible.value,
                enter = fadeIn(), exit = fadeOut()
            ) {
                MyFloatingActionButton(
                    onClick = {
                        notesViewModel.changeNoteScreenState(NoteScreenState.NewNote)
                        navController.navigate(Screen.NoteScreen.route)
                    })
            }
        },
        scaffoldState = scaffoldState
    )
    { paddingValues ->
        when (val noteModificationStatus =
            notesViewModel.noteModificationStatus.collectAsState().value) {
            is Response.Success<Operation> -> {
                notesViewModel.resetNoteModificationStatus()
                if(noteModificationStatus.data is Operation.Delete)scaffoldState.showScaffold(noteModificationStatus.data.message, coroutineScope)
            }
            else -> {
            }
        }
        LocalNotesContent( paddingValues = paddingValues, navController)
    }
}


