package com.solita.devnotary.android.feature_notes

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes._sharedUtils.showScaffold
import com.solita.devnotary.android.feature_notes.localNotesScreen.components.LocalNotesContent
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.domain.Operation
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun LocalNotesScreen(navController: NavController) {

    val notesViewModel: NotesViewModel by androidDi.instance()

    val notesState = notesViewModel.getNotes.collectAsState(listOf())
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        floatingActionButton = {
            if(notesViewModel.isFabVisible.collectAsState().value)MyFloatingActionButton(navController = navController)
                               },
        scaffoldState = scaffoldState
    )
    { paddingValues ->
        when(val noteModificationStatus = notesViewModel.noteModificationStatus.collectAsState().value){
           is Response.Success<Operation> -> {
               println("Got some response ${noteModificationStatus.data}")
               notesViewModel.resetNoteModificationStatus()
               scaffoldState.showScaffold(noteModificationStatus.data.message,coroutineScope)
           }
            else -> {
                println("Got some response ${noteModificationStatus}")
            }
        }
        LocalNotesContent(notesState = notesState, paddingValues = paddingValues,navController)
    }
}


