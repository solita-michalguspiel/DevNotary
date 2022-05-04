package com.solita.devnotary.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.solita.devnotary.Greeting
import com.solita.devnotary.di.di
import com.solita.devnotary.feature_auth.presentation.AuthViewModel

fun greet(): String {
    return Greeting().greeting()
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        setContent {
            MaterialTheme{
                MainView(AuthViewModel(intent.data.toString()))
            }
        }
    }
}
@Composable
fun MainView(authViewModel: AuthViewModel) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = greet())
        Text(text = "Hello from compose!")
        Button(onClick = {
            authViewModel.sendEmailLink("guspielmichal@gmail.com")
        }) {
            Text(text = "click me")
        }
        Button(onClick = {
            println("Intent : ${authViewModel.intent}")
        }) {
            Text(text = "click me to check intent")
        }
        }
    }
