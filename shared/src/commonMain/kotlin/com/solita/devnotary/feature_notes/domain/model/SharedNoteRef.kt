package com.solita.devnotary.feature_notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SharedNoteRef(
    val noteId : String,
    val ownerUserId: String,
    val sharedUserId: String
)