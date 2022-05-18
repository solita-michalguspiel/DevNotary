package com.solita.devnotary.feature_notes.domain.model

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
data class Note(
     val noteId: String,
     val ownerUserId : String? = null,
     val title: String,
     val content: String,
     val dateTime: String,
     val color: String,
) : Parcelable