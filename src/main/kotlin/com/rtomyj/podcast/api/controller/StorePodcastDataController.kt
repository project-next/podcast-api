package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.exception.PodcastException
import com.rtomyj.podcast.api.model.PodcastInfo
import com.rtomyj.podcast.api.service.PodcastService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
class StorePodcastDataController {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@Autowired
	private lateinit var podcastService: PodcastService

	@PostMapping("/new-podcast")
	@Throws(PodcastException::class)
	fun storeNewPodcast(@Valid @RequestBody podcastInfo: PodcastInfo): ResponseEntity<String> {
		return ResponseEntity.ok(podcastInfo.podcastTitle)
	}
}