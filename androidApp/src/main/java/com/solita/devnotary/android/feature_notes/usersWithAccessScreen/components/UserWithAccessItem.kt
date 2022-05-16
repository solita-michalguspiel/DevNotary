package com.solita.devnotary.android.feature_notes.noteScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.solita.devnotary.android.theme.LocalSpacing

@Composable
fun UserWithAccessItem(modifier: Modifier, userEmailAddress: String, onDeleteClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(LocalSpacing.current.small),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(userEmailAddress)
        IconButton(onClick = { onDeleteClick() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


@Preview
@Composable
fun previewUserWithAccessItem() {
    UserWithAccessItem(modifier = Modifier, userEmailAddress = "TestEmail@gmail.com") {

    }
}