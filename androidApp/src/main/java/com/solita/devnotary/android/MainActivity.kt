package com.solita.devnotary.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.solita.devnotary.android.theme.DevNotaryTheme
import com.solita.devnotary.android.utils.signInIntent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInIntent = intent
        setContent {
            DevNotaryTheme {
                MainScreen()
            }
        }
    }
}
