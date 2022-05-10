package com.solita.devnotary.android.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalColors

@Composable
fun MyBottomNavigationDrawer(navController: NavController) {
    BottomNavigation(backgroundColor = Color.Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        Screen.BottomNavItems.bottomNavItems.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination == screen.route,
                label ={ Text(text = screen.title)},
                selectedContentColor = LocalColors.current.White,
                unselectedContentColor = LocalColors.current.Gray,
                alwaysShowLabel = false,
                onClick = {
                    if(currentDestination != screen.route) {
                        navController.navigate(screen.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = screen.iconDesc
                    )
                })
        }
    }
}