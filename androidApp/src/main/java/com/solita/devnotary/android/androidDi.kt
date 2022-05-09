package com.solita.devnotary.android

import android.content.Intent
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton

lateinit var signInIntent : Intent

val androidDi  = DI{

    bindSingleton { NotesViewModel() }

    bindSingleton { AuthViewModel() }

}