package com.solita.devnotary.android

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.solita.devnotary.android.navigation.Navigation
import com.solita.devnotary.android.ui.DevNotaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInIntent = intent
        setContent {
            DevNotaryTheme {
                Navigation()
            }
        }
    }
}
