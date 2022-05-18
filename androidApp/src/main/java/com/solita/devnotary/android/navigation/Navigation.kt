package com.solita.devnotary.android.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.feature_notes.NotesListScreen
import com.solita.devnotary.android.feature_notes.NoteScreen
import com.solita.devnotary.android.feature_notes.usersWithAccessScreen.UsersWithAccessScreen
import com.solita.devnotary.android.utils.Constants.NOTE_INDEX
import com.solita.devnotary.feature_notes.domain.model.Note

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {

        composable(Screen.SignInScreen.route) {
            SignInScreen(navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.NotesListScreen.route) {
            NotesListScreen(navController)
        }
        composable(route = Screen.UsersWithAccessScreen.route) {
            UsersWithAccessScreen()
        }
        composable(
            route = Screen.NoteScreen.route + "/{note}",
            arguments =
        listOf(
            navArgument("note") {
                type = NoteType()
            }
        )
        ){val note = it.arguments?.getParcelable<Note>("note")
            NoteScreen(navController = navController, note )
        }
        composable(route = Screen.NoteScreen.route){
            NoteScreen(navController = navController, null )
        }
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