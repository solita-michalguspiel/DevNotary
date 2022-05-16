package com.solita.devnotary.feature_notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SharedNote(
    val noteId: String,
    val ownerUserId : String,
    val title: String,
    val content: String,
    val dateTime: String,
    val color: String
)