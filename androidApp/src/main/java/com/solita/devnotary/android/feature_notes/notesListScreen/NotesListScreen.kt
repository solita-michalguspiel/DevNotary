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
import androidx.navigation.NavController
import com.solita.devnotary.android.composables.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes.notesListScreen.components.NotesListContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.android.navigation.navigateToNoteInteractionScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import org.kodein.di.instance

@Composable
fun NotesListScreen(navController: NavController) {

    val notesListViewModel: NotesListViewModel by di.instance()
    val scaffoldState = rememberScaffoldState()
    val isScrollingUp = notesListViewModel.isScrollingUp.collectAsState()

    LaunchedEffect(Unit) {
        notesListViewModel.getSharedNotes()
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
                        navController.navigateToNoteInteractionScreen()
                    })
            }
        },
        scaffoldState = scaffoldState
    )
    { paddingValues ->
        NotesListContent(paddingValues = paddingValues, navController)
    }
}


