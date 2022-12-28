package com.rtomyj.podcast.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rtomyj.podcast.model.PodcastData
import org.springframework.core.io.ClassPathResource

class TestObjectsFromFile {
	companion object {
		private fun configureMapper(): ObjectMapper {
			val mapper = jacksonObjectMapper()
			mapper.registerModule(JavaTimeModule())

			return mapper
		}

		private val mapper = configureMapper()

		val podcastData1 = mapper.readValue(ClassPathResource("json-mock/PodcastDataRes1.json").file, PodcastData::class.java)
	}
}