package com.solita.devnotary.android.feature_notes._sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.feature_notes.addNoteScreen.components.ColorBall
import com.solita.devnotary.android.ui.LocalSpacing
import com.solita.devnotary.android.utils.getAvailableColors
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun ColorBallsRow() {
    val viewModel: NotesViewModel by androidDi.instance()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = LocalSpacing.current.small),
        horizontalArrangement = Arrangement.Center

    ) {
        getAvailableColors.forEach {
            IconButton(
                onClick = {
                    viewModel.noteColor.value = it.colorName
                },
                modifier = Modifier.padding(horizontal = LocalSpacing.current.xSmall)
            ) {
                if (viewModel.noteColor.value == it.colorName) ColorBall(
                    it.color,
                    true
                ) else ColorBall(color = it.color, false)
            }
        }
    }
}