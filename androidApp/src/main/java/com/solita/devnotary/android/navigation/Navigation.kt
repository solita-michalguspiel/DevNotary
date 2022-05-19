package com.solita.devnotary.android.navigation

import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.feature_notes.NoteScreen
import com.solita.devnotary.android.feature_notes.NotesListScreen
import com.solita.devnotary.android.feature_notes.usersWithAccessScreen.UsersWithAccessScreen
import com.solita.devnotary.android.utils.Constants.NOTE
import com.solita.devnotary.feature_notes.domain.model.Note

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController = navController, startDestination = Screen.SignInScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) }) {

        composable(Screen.SignInScreen.route) {
            SignInScreen(navController)
        }
        composable(Screen.ProfileScreen.route,
            enterTransition = {
                if (initialState.destination.route == Screen.NotesListScreen.route)
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(250)
                    )
                else null
            }, exitTransition = {
                if (targetState.destination.route == Screen.NotesListScreen.route) {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(250)
                    )
                } else null
            }
        ) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.NotesListScreen.route, enterTransition = {
            if (initialState.destination.route == Screen.ProfileScreen.route)
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            else null
        }, exitTransition = {
            if (targetState.destination.route == Screen.ProfileScreen.route) {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            } else null
        }) {
            NotesListScreen(navController)
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
            UsersWithAccessScreen()
        }
        composable(
            route = Screen.NoteScreen.route + "/{note}",
            arguments =
            listOf(
                navArgument(NOTE) {
                    type = NoteType()
                }
            )
        ) {
            val note = it.arguments?.getParcelable<Note>(NOTE)
            NoteScreen(navController = navController, note)
        }
        composable(route = Screen.NoteScreen.route) {
            NoteScreen(navController = navController, null)
        }
    }
}

fun NavController.navigateToNoteScreen(note: Note? = null) {
    if (note != null) {
        val noteJson = Uri.encode(Gson().toJson(note))
        this.navigate(Screen.NoteScreen.route + "/${noteJson}") {
            popUpTo(Screen.NotesListScreen.route)
        }
    } else this.navigate(Screen.NoteScreen.route) {
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