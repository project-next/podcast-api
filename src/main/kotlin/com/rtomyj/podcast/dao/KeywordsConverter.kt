package com.rtomyj.podcast.dao

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.util.StringUtils

@Converter(autoApply = false)
class KeywordsConverter: AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(keywords: List<String>): String {
        return keywords.joinToString(separator = "|")
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return if (StringUtils.hasLength(dbData)) {
            dbData!!.split("|")
        } else {
            emptyList()
        }
    }
}