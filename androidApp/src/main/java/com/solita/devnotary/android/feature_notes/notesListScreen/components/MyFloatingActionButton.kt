package com.solita.devnotary.android.composables

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.theme.LocalColors
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun MyFloatingActionButton(onClick : (() -> Unit)) {
    FloatingActionButton(backgroundColor = LocalColors.current.LightBlue,
        onClick = onClick) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.fab_desc)
        )
    }
}