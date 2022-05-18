package com.solita.devnotary.utils


import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.Clock
import java.util.*


actual fun formatIso8601ToString(iso8601: String,timeZone : TimeZone): String {
    if(iso8601.isBlank()) return ""
    val localTime = iso8601.toInstant().toLocalDateTime(timeZone).toString()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS", Locale.getDefault())
    val date = sdf.parse(localTime)
    val userPattern = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    return  userPattern.format(date!!)
}