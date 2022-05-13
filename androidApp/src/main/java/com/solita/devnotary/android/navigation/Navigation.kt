package com.solita.devnotary.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.feature_notes.LocalNotesScreen
import com.solita.devnotary.android.feature_notes.NoteScreen
import com.solita.devnotary.android.feature_notes.SharedNotesScreen
import com.solita.devnotary.android.utils.Constants.NOTE_COLOR
import com.solita.devnotary.android.utils.Constants.NOTE_CONTENT
import com.solita.devnotary.android.utils.Constants.NOTE_ID
import com.solita.devnotary.android.utils.Constants.NOTE_TIME_DATE
import com.solita.devnotary.android.utils.Constants.NOTE_TITLE

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
            route =
            Screen.NoteScreen.route +
                    "?$NOTE_ID={$NOTE_ID}&$NOTE_TITLE={$NOTE_TITLE}&$NOTE_CONTENT={$NOTE_CONTENT}&$NOTE_TIME_DATE={$NOTE_TIME_DATE}&$NOTE_COLOR={$NOTE_COLOR}",
            arguments = listOf(
                navArgument(NOTE_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(NOTE_TITLE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(NOTE_CONTENT) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(NOTE_TIME_DATE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(NOTE_COLOR) {
                    type = NavType.StringType
                    nullable = true
                },

                )
        ) {
            NoteScreen(
                navController,
                it.arguments?.getString(NOTE_ID).toString(),
                it.arguments?.getString(NOTE_TITLE).toString(),
                it.arguments?.getString(NOTE_CONTENT).toString(),
                it.arguments?.getString(NOTE_TIME_DATE).toString(),
                it.arguments?.getString(NOTE_COLOR).toString(),
            )
        }
    }
}