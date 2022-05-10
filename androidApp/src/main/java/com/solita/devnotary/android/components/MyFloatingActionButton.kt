package com.solita.devnotary.android.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solita.devnotary.android.R
import com.solita.devnotary.android.domain.Screen
import com.solita.devnotary.android.ui.LocalColors

@Composable
fun MyFloatingActionButton(navController: NavController) {
    FloatingActionButton(backgroundColor = LocalColors.current.LightBlue,
        onClick = { navController.navigate(Screen.AddNoteScreen.route) }) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.fab_desc)
        )
    }
}