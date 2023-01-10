package com.rtomyj.podcast.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class Helpers {
	companion object {
		private fun configureMapper(): ObjectMapper {
			val mapper = jacksonObjectMapper()
			mapper.registerModule(JavaTimeModule())

			return mapper
		}

		val mapper = configureMapper()
	}
}