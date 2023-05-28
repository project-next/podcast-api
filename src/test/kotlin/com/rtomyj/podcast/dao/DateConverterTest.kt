package com.rtomyj.podcast.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Month

class DateConverterTest {
    companion object {
        private val dateConverter = DateConverter()
        private const val dateStr = "1991-07-27 00:00:00"
        private val date = LocalDateTime.of(1991, Month.JULY, 27, 0, 0)
    }

    @Test
    fun `Test Convert To Database Column`() {
        assertEquals(dateStr, dateConverter.convertToDatabaseColumn(date))
    }

    @Test
    fun `Test Convert To Entity`() {
        assertEquals(date, dateConverter.convertToEntityAttribute(dateStr))
    }
}