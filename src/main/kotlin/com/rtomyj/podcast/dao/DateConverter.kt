package com.rtomyj.podcast.dao

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

@Converter(autoApply = true)
class DateConverter: AttributeConverter<LocalDateTime, String> {
    companion object {
        private val dbDateFormatter = DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter()
    }

    override fun convertToDatabaseColumn(localDateTime: LocalDateTime?): String {
        return dbDateFormatter.format(localDateTime)
    }

    override fun convertToEntityAttribute(dbDate: String?): LocalDateTime {
        return LocalDateTime.from(dbDateFormatter.parse(dbDate))
    }
}