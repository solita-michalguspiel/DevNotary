package com.solita.devnotary.domain

sealed class NoteScreenState {
    object NewNote : NoteScreenState()
    object LocalNote : NoteScreenState()
    object LocalNoteEdit : NoteScreenState()
    object SharedNote : NoteScreenState()
}