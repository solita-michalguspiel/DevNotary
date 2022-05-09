package com.solita.devnotary.feature_notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SharedNote(
    val noteId: String,
    val ownerUserId : String,
    val sharedUserId: String,
    val title: String,
    val content: String,
    val sharedDate: String,
    val color: String
)