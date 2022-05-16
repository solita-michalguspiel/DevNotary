package com.solita.devnotary.android.feature_notes.usersWithAccessScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.solita.devnotary.android.R
import com.solita.devnotary.android.androidDi
import com.solita.devnotary.android.composables.ProgressIndicator
import com.solita.devnotary.android.feature_notes.noteScreen.components.UserWithAccessItem
import com.solita.devnotary.android.theme.LocalSpacing
import com.solita.devnotary.android.theme.Typography
import com.solita.devnotary.domain.Response
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

@Composable
fun UsersWithAccessScreen(
) {
    val viewModel: NotesViewModel by androidDi.instance()

    Column(Modifier.fillMaxWidth()) {

        Text(
            text = stringResource(id = R.string.users_with_access),
            style = Typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = LocalSpacing.current.default)
        )


        when (val response = viewModel.usersWithAccess.collectAsState().value) {
            is Response.Loading -> ProgressIndicator()
            is Response.Error -> Text(stringResource(id = R.string.undefined_error))
            is Response.Success -> {
                if (response.data.isEmpty()) Text(
                    text = stringResource(id = R.string.note_not_shared_with_anybody),
                    style = Typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = LocalSpacing.current.xLarge)
                        .fillMaxWidth()
                )
                else {
                    LazyColumn {
                        items(response.data) {
                            Divider()
                            UserWithAccessItem(
                                modifier = Modifier.padding(
                                    vertical = LocalSpacing.current.xSmall,
                                    horizontal = LocalSpacing.current.small
                                ),
                                userEmailAddress = it.userEmail
                            ) {
                                viewModel.unShareNote(it.userId)
                            }
                        }
                    }
                    Divider()
                }
            }
            else -> {}
        }
    }
}