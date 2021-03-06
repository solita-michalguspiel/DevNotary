package com.solita.devnotary.android.navigation

import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.gson.Gson
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_notes.NoteDetailsScreen
import com.solita.devnotary.android.feature_notes.NotesListScreen
import com.solita.devnotary.android.feature_notes.noteScreen.NoteInteractionScreen
import com.solita.devnotary.android.feature_notes.usersWithAccessScreen.UsersWithAccessScreen
import com.solita.devnotary.android.utils.Constants.NOTE
import com.solita.devnotary.feature_notes.domain.model.Note

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(paddingValues : PaddingValues,navController: NavHostController,scaffoldState: ScaffoldState) {
    AnimatedNavHost(navController = navController, startDestination = Screen.ProfileScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) }) {

        composable(Screen.ProfileScreen.route,
            enterTransition = {
                if (initialState.destination.route == Screen.NotesListScreen.route)
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(250)
                    )
                else null
            }, exitTransition = {
                if (targetState.destination.route == Screen.NotesListScreen.route) {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(250)
                    )
                } else null
            }
        ) {
            ProfileScreen(paddingValues)
        }
        composable(Screen.NotesListScreen.route, enterTransition = {
            if (initialState.destination.route == Screen.ProfileScreen.route)
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            else null
        }, exitTransition = {
            if (targetState.destination.route == Screen.ProfileScreen.route) {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            } else null
        }) {
            NotesListScreen(navController,paddingValues)
        }
        composable(route = Screen.UsersWithAccessScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            UsersWithAccessScreen(paddingValues)
        }
        composable(
            route = Screen.NoteDetailsScreen.route + "/{note}",
            arguments =
            listOf(
                navArgument(NOTE) {
                    type = NoteType()
                }
            )
        ) {
            val note = it.arguments?.getParcelable<Note>(NOTE)
            NoteDetailsScreen(navController = navController, note!!,paddingValues)
        }

        composable(
            route = Screen.NoteInteractionScreen.route + "/{note}",
            arguments =
            listOf(
                navArgument(NOTE) {
                    type = NoteType()
                }
            )
        ) {
            val note = it.arguments?.getParcelable<Note>(NOTE)
            NoteInteractionScreen(navController = navController, note,paddingValues,scaffoldState)
        }
        composable(route = Screen.NoteInteractionScreen.route) {
            NoteInteractionScreen(navController = navController, null,paddingValues,scaffoldState)
        }
    }
}

fun NavController.navigateToNoteInteractionScreen(note: Note? = null){
    if (note != null) {
        val noteJson = Uri.encode(Gson().toJson(note))
        this.navigate(Screen.NoteInteractionScreen.route + "/${noteJson}") {
            popUpTo(Screen.NotesListScreen.route)
        }
    } else this.navigate(Screen.NoteInteractionScreen.route) {
        popUpTo(Screen.NotesListScreen.route)
    }
}

fun NavController.navigateToNoteDetailsScreen(note: Note) {
        val noteJson = Uri.encode(Gson().toJson(note))
        this.navigate(Screen.NoteDetailsScreen.route + "/${noteJson}") {
            popUpTo(Screen.NotesListScreen.route)
        }
    }

class NoteType : NavType<Note>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Note? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Note {
        return Gson().fromJson(value, Note::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Note) {
        bundle.putParcelable(key, value)
    }
}