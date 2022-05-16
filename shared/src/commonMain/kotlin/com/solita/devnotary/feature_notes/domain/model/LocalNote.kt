package com.solita.devnotary.feature_notes.domain.model

data class Note(
     val noteId: String,
     val ownerUserId : String? = null,
     val shareUserId : String? = null,
     val title: String,
     val content: String,
     val dateTime: String,
     val color: String,
)