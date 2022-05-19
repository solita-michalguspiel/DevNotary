package com.solita.devnotary.feature_notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
class SharedNote(
    val noteId: String,
    val ownerUserId : String,
    val title: Array<Int>,
    val content: Array<Int>,
    val dateTime: String,
    val color: String
)