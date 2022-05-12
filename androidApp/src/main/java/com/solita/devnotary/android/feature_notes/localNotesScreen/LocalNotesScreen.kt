package com.solita.devnotary.android.feature_notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
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

    val isFabVisible = notesViewModel.isFabVisible.collectAsState()


    val density = LocalDensity.current


    Scaffold(
        bottomBar = { MyBottomNavigationDrawer(navController = navController) },
        floatingActionButton = {
            AnimatedVisibility(visible = isFabVisible.value,
            enter = fadeIn(), exit = fadeOut()){
                println("This is called!")
                MyFloatingActionButton(navController = navController)
            }
                               },
        scaffoldState = scaffoldState
    )
    { paddingValues ->
        when(val noteModificationStatus = notesViewModel.noteModificationStatus.collectAsState().value){
           is Response.Success<Operation> -> {
               notesViewModel.resetNoteModificationStatus()
               scaffoldState.showScaffold(noteModificationStatus.data.message,coroutineScope)
           }
            else -> {
            }
        }
        LocalNotesContent(notesState = notesState, paddingValues = paddingValues,navController)
    }
}


