package com.rtomyj.podcast.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KeywordsConverterTest {
    companion object {
        private val keywordsConverter = KeywordsConverter()
        private const val tagStr = "tag1|tag2"
        private val tagList = listOf("tag1", "tag2")
    }

    @Test
    fun `Test Convert To DatabaseColumn`() {
        assertEquals(tagStr, keywordsConverter.convertToDatabaseColumn(tagList))
    }

    @Test
    fun `Test Convert To Entity`() {
        assertEquals(tagList, keywordsConverter.convertToEntityAttribute(tagStr))
    }

    @Test
    fun `Test Convert To Entity - Null Tags From DB`() {
        assertEquals(emptyList<String>(), keywordsConverter.convertToEntityAttribute(null))
    }

    @Test
    fun `Test Convert To Entity - Empty Tags String`() {
        assertEquals(emptyList<String>(), keywordsConverter.convertToEntityAttribute(""))
    }
}