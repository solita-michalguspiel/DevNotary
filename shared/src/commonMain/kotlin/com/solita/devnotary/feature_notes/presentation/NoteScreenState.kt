package com.solita.devnotary.feature_notes.presentation

sealed class NoteScreenState {
    object NewNote : NoteScreenState()
    object LocalNote : NoteScreenState()
    object LocalNoteEdit : NoteScreenState()
    object SharedNote : NoteScreenState()
}