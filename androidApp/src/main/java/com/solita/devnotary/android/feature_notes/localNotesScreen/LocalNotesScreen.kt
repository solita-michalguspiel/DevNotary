package com.solita.devnotary.android.feature_notes

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.solita.devnotary.android.navigation.MyBottomNavigationDrawer

@Composable
fun LocalNotesScreen(navController: NavController) {

        Scaffold (bottomBar = { MyBottomNavigationDrawer(navController = navController) }){

        }
}