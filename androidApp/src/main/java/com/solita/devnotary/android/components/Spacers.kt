package com.solita.devnotary.android.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.solita.devnotary.android.ui.LocalSpacing

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(LocalSpacing.current.default))
}

@Composable
fun LargeSpacer(){
    Spacer(modifier = Modifier.size(LocalSpacing.current.large))
}