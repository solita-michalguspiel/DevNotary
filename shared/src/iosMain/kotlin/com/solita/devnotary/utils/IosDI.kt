package com.solita.devnotary.utils

import com.solita.devnotary.di.di
import com.solita.devnotary.feature_auth.presentation.AuthViewModel
import com.solita.devnotary.feature_notes.presentation.NotesViewModel
import org.kodein.di.instance

class iosDI {

    fun getAuthViewModel(): AuthViewModel {
        val authViewModel: AuthViewModel by di.instance()
        return authViewModel
    }

    fun getNotesViewModel() : NotesViewModel {
        val notesViewModel: NotesViewModel by di.instance()
        return notesViewModel
    }

}

