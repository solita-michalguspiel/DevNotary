package com.solita.devnotary.android.feature_notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.components.MyFloatingActionButton
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer
import com.solita.devnotary.database.Note
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import kotlinx.coroutines.flow.Flow
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
        LocalNotesContent(notesState = notesState, paddingValues = paddingValues)
    }
}


@Composable
fun LocalNotesContent(notesState: State<List<Note>>, paddingValues: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(notesState.value) {
                NotePreview(note = it)
            }
        }
    }
}

@Composable
fun NotePreview(note: Note) {

}