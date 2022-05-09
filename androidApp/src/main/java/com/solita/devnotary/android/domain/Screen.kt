package com.solita.devnotary.android.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector? = null,
    val title: String = "",
    val iconDesc: String = ""
) {

    object SignInScreen : Screen("sign_in_screen")
    object LocalNotesScreen : Screen("local_notes_screen",icon = Icons.Default.Home, title = "Home","Home button")
    object SharedNotesScreen : Screen("shared_notes_screen",icon = Icons.Default.Share, title = "Shared", "Shared notes")
    object NoteDetailsScreen : Screen("note_details_screen")
    object AddNoteScreen : Screen("add_notes_screen")
    object ProfileScreen : Screen("profile_screen", icon = Icons.Default.Person,title = "Profile","Profile button")


    object BottomNavItems {
        val bottomNavItems = listOf(LocalNotesScreen, SharedNotesScreen)
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}