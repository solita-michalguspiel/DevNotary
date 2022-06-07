package com.solita.devnotary.utils

import kotlinx.datetime.TimeZone

actual fun formatIso8601ToString(iso8601: String,timeZone : TimeZone): String {
    if(iso8601.isBlank()) return ""

    return iso8601
}