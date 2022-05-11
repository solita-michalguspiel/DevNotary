package com.solita.devnotary.android.feature_notes

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes.localNotesScreen.components.LocalNotesContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNotesScreen(navController: NavController) {

    val notesViewModel: NotesViewModel by androidDi.instance()

    val notesState = notesViewModel.getNotes.collectAsState(listOf())

    Scaffold(
        bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        floatingActionButton = { MyFloatingActionButton(navController = navController) }
    )
    { paddingValues ->
        LocalNotesContent(notesState = notesState, paddingValues = paddingValues,navController)
    }
}


