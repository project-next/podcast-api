package com.rtomyj.podcast.util

import com.rtomyj.podcast.model.Podcast
import org.springframework.core.io.ClassPathResource

class TestObjectsFromFile {
    companion object {
        val podcastData1: Podcast =
            Helpers.mapper.readValue(ClassPathResource("json-mock/PodcastDataRes1.json").file, Podcast::class.java)
    }
}