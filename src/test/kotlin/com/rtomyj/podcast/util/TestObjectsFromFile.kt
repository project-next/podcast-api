package com.rtomyj.podcast.util

import com.rtomyj.podcast.model.PodcastData
import org.springframework.core.io.ClassPathResource

class TestObjectsFromFile {
	companion object {
		val podcastData1: PodcastData = Helpers.mapper.readValue(ClassPathResource("json-mock/PodcastDataRes1.json").file, PodcastData::class.java)
	}
}