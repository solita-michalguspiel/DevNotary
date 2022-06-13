package com.solita.devnotary.android.feature_notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.solita.devnotary.android.composables.MyFloatingActionButton
import com.solita.devnotary.android.feature_notes.notesListScreen.components.NotesListContent
import com.solita.devnotary.android.navigation.navigateToNoteInteractionScreen
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_notes.presentation.notesList.NotesListViewModel
import org.kodein.di.instance

@Composable
fun NotesListScreen(navController: NavController,paddingValues: PaddingValues) {

    val notesListViewModel: NotesListViewModel by di.instance()
    val isScrollingUp = notesListViewModel.isScrollingUp.collectAsState()

    LaunchedEffect(Unit) {
        notesListViewModel.getSharedNotes()
    }
    Scaffold(Modifier.padding(paddingValues),
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
    )
    { it.toString()
        NotesListContent(navController)
    }
}


