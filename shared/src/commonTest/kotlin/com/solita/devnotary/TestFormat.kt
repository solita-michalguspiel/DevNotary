package com.solita.devnotary

import com.solita.devnotary.utils.formatIso8601ToString
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlinx.datetime.serializers.TimeZoneSerializer
import kotlin.test.Test


class TestFormat {

    private val dateTime1 = "2022-05-10T13:26:57.271Z"
    private val dateTime2 = "2022-05-10T10:11:57.271Z"
    private val dateTime3 = "2022-05-10T12:48:57.271Z"

    @Test
    fun formatIsoDate_ShouldReturnCorrect_String() {
        formatIso8601ToString(dateTime1, TimeZone.of("Europe/Helsinki")) shouldBe "2022.05.10 16:26"
        formatIso8601ToString(dateTime2, TimeZone.of("Europe/Helsinki")) shouldBe "2022.05.10 13:11"
        formatIso8601ToString(dateTime3, TimeZone.of("Europe/Helsinki")) shouldBe "2022.05.10 15:48"
    }

    @Test
    fun formatIsoDateForDifferentTimeZone_ShouldReturnCorrectString(){
        formatIso8601ToString(dateTime1, TimeZone.of("Europe/Warsaw")) shouldBe "2022.05.10 15:26"
        formatIso8601ToString(dateTime1, TimeZone.of("Europe/London")) shouldBe "2022.05.10 14:26"
    }


}