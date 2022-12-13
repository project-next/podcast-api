package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.model.PodcastInfo
import com.rtomyj.podcast.api.service.PodcastService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StorePodcastDataController {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@Autowired
	private lateinit var podcastService: PodcastService

	@PostMapping("/new-podcast")
	fun storeNewPodcast(@RequestBody podcastInfo: PodcastInfo) {
		log.info(podcastInfo.podcastId)
	}
}