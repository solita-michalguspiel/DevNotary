package com.solita.devnotary.android.feature_notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.composables.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes.notesListScreen.components.NotesListContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.android.navigation.Screen
import com.solita.devnotary.di.di
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.domain.Operation
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.launch
import org.kodein.di.instance

@Composable
fun NotesListScreen(navController: NavController) {

    val notesViewModel: NotesViewModel by di.instance()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val isScrollingUp = notesViewModel.isScrollingUp.collectAsState()
    val noteModificationStatus =
        notesViewModel.noteModificationStatus.collectAsState().value

    LaunchedEffect(Unit) {
        notesViewModel.getSharedNotes()
    }
    notesViewModel.isEditEnabled.value = false

    LaunchedEffect(noteModificationStatus) {
        if (noteModificationStatus is Response.Success<Operation>) {
            notesViewModel.resetNoteModificationStatus()
            if (noteModificationStatus.data is Operation.Delete) {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(noteModificationStatus.data.message)
                    notesViewModel.resetNoteModificationStatus()
                }
            }
        }
    }

    Scaffold(
        bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isScrollingUp.value,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow)),
                exit = fadeOut(spring(stiffness = Spring.StiffnessLow))
            ) {
                MyFloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.NoteScreen.route)
                    })
            }
        },
        scaffoldState = scaffoldState
    )
    { paddingValues ->
        NotesListContent(paddingValues = paddingValues, navController)
    }
}


