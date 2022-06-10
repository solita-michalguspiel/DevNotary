package com.solita.devnotary.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector? = null,
    val title: String = "",
    val iconDesc: String = ""
) {

    object SignInScreen : Screen("sign_in_screen")
    object NotesListScreen : Screen("local_notes_screen",icon = Icons.Default.Home, title = "Home","Home button")
    object NoteDetailsScreen : Screen("note_screen")
    object UsersWithAccessScreen : Screen("users_with_access_screen")
    object ProfileScreen : Screen("profile_screen", icon = Icons.Default.Person,title = "Profile","Profile button")
    object NoteInteractionScreen : Screen("note_interaction_screen")

    object BottomNavItems {
        val bottomNavItems = listOf(NotesListScreen, ProfileScreen)
    }
}