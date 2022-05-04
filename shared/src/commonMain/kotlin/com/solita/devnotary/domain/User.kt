package com.solita.devnotary.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val userEmail : String
)