package com.solita.devnotary.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.feature_auth.ProfileScreen
import com.solita.devnotary.android.feature_auth.SignInScreen
import com.solita.devnotary.android.feature_notes.AddNoteScreen
import com.solita.devnotary.android.feature_notes.LocalNotesScreen
import com.solita.devnotary.android.feature_notes.NoteDetailsScreen
import com.solita.devnotary.android.feature_notes.SharedNotesScreen
import com.solita.devnotary.android.utils.Constants.NOTE_COLOR
import com.solita.devnotary.android.utils.Constants.NOTE_CONTENT
import com.solita.devnotary.android.utils.Constants.NOTE_ID
import com.solita.devnotary.android.utils.Constants.NOTE_TIME_DATE
import com.solita.devnotary.android.utils.Constants.NOTE_TITLE
import com.solita.devnotary.android.utils.Constants.NOTE_TYPE

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {

        composable(Screen.SignInScreen.route) {
            SignInScreen(navController)
        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }

        composable(Screen.LocalNotesScreen.route) {
            LocalNotesScreen(navController)
        }
        composable(Screen.SharedNotesScreen.route) {
            SharedNotesScreen(navController)
        }
        composable(
            Screen.NoteDetailsScreen.withArgs(
                NOTE_ID,
                NOTE_TITLE,
                NOTE_CONTENT,
                NOTE_TIME_DATE,
                NOTE_COLOR,
                NOTE_TYPE
            ), arguments = listOf(
                navArgument(NOTE_ID) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(NOTE_TITLE) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(NOTE_CONTENT) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(NOTE_TIME_DATE) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(NOTE_COLOR) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(NOTE_TYPE) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            NoteDetailsScreen(navController)
        }
        composable(Screen.AddNoteScreen.route) {
            AddNoteScreen(navController)
        }

    }
}