package com.solita.devnotary.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.feature_notes.LocalNotesScreen
import com.solita.devnotary.android.feature_notes.NoteScreen
import com.solita.devnotary.android.feature_notes.usersWithAccessScreen.UsersWithAccessScreen
import com.solita.devnotary.android.utils.Constants.NOTE_INDEX

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
            LocalNotesScreen(navController)
        }
        composable(
            route = Screen.NoteScreen.route +"?$NOTE_INDEX={$NOTE_INDEX}",
            arguments = listOf(
                navArgument(NOTE_INDEX){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            val index = it.arguments?.getString(NOTE_INDEX)
            NoteScreen(
                navController,
                index
                )
        }
        composable(route = Screen.UsersWithAccessScreen.route){
            UsersWithAccessScreen(navController = navController)
        }
    }
}