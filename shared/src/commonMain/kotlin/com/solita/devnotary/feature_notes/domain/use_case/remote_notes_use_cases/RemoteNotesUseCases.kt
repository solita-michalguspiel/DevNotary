package com.solita.devnotary.feature_notes.domain.use_case.remote_notes_use_cases

data class RemoteNotesUseCases(
    val getSharedNotes: GetSharedNotes,
    val shareNote: ShareNote,
    val unshareNote: UnshareNote,
    val deleteSharedNote: DeleteSharedNote,
    val editSharedNote: EditSharedNote
    )