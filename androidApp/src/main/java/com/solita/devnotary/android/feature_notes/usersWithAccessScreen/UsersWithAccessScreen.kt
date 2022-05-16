package com.solita.devnotary.android.feature_notes.usersWithAccessScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.feature_notes.noteScreen.components.UserWithAccessItem
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun UsersWithAccessScreen(
    navController: NavController,
) {
    val viewModel: NotesViewModel by androidDi.instance()

    Column(Modifier.fillMaxWidth()) {

        Text(text = "Users with access to :${viewModel.titleInput.value}")

        when (val response = viewModel.usersWithAccess.collectAsState().value) {
            is Response.Loading -> ProgressIndicator()
            is Response.Error -> Text("Something went wrong. Sorry!")
            is Response.Success -> {
                LazyColumn {
                    items(response.data) {
                        UserWithAccessItem(
                            modifier = Modifier,
                            userEmailAddress = it.userEmail
                        ) {
                            viewModel.unShareNote(it.userId)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}