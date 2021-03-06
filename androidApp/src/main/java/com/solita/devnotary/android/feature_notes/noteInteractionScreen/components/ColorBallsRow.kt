package com.solita.devnotary.android.feature_notes._sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.solita.devnotary.android.feature_notes.addNoteScreen.components.ColorBall
import com.solita.devnotary.android.feature_notes.domain.getAvailableColors
import com.solita.devnotary.android.theme.LocalSpacing

@Composable
fun ColorBallsRow(
    modifier: Modifier = Modifier,
    chosenNoteColor: String,
    onBallClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = LocalSpacing.current.small),
        horizontalArrangement = Arrangement.Center

    ) {
        getAvailableColors.forEach {
            IconButton(
                onClick = {
                    onBallClick(it.colorName)
                },
                modifier = Modifier.padding(horizontal = LocalSpacing.current.xSmall)
                    .testTag(it.colorName)
                    .semantics {
                    contentDescription = "Change note color for ${it.colorName}"
                }
            ) {
                if (chosenNoteColor == it.colorName) ColorBall(
                    color = it.color,
                    isChosen = true
                ) else ColorBall(color = it.color, isChosen = false)
            }
        }
    }
}