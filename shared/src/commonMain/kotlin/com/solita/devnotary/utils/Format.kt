package com.solita.devnotary.utils

import kotlinx.datetime.TimeZone

expect fun formatIso8601ToString(iso8601: String , timeZone: TimeZone = TimeZone.currentSystemDefault()):String