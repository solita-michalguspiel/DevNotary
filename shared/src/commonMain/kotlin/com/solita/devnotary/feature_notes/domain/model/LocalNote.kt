package com.solita.devnotary.feature_notes.domain.model

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize


data class LocalNote(
     val note_id: String,
     val title: String,
     val content: String,
     val date_time: String,
     val color: String
)