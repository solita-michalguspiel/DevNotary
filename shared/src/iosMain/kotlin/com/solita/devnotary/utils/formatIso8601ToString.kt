package com.solita.devnotary.utils

import kotlinx.datetime.TimeZone
import platform.Foundation.*

actual fun formatIso8601ToString(iso8601: String, timeZone: TimeZone): String {
    if (iso8601.isBlank()) return ""
    val formatter = NSISO8601DateFormatter()
    formatter.setFormatOptions(NSISO8601DateFormatWithDay +
            NSISO8601DateFormatWithMonth +
            NSISO8601DateFormatWithYear +
            NSISO8601DateFormatWithDashSeparatorInDate +
            NSISO8601DateFormatWithTime +
            NSISO8601DateFormatWithColonSeparatorInTime)
    val nsDate = formatter.dateFromString(iso8601)
    val outputFormatter = NSDateFormatter()
    outputFormatter.dateFormat = "yyyy.MM.dd HH:mm"
    return nsDate?.let { outputFormatter.stringFromDate(it) } ?: ""
}