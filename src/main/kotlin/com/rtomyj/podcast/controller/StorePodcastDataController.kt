package com.rtomyj.podcast.controller

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
@Validated
class StorePodcastDataController {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@Autowired
	private lateinit var podcastService: PodcastService

	@PostMapping("/podcast")
	@Throws(PodcastException::class)
	fun storeNewPodcast(@Valid @RequestBody podcast: Podcast): ResponseEntity<String> {
		return ResponseEntity.ok(podcast.podcastTitle)
	}

	@PostMapping("/podcast/{podcastId}/episode")
	@Throws(PodcastException::class)
	fun storeNewPodcastEpisode(@PathVariable("podcastId") podcastId: String, @Valid @RequestBody podcastEpisode: PodcastEpisode): ResponseEntity<String> {
		return ResponseEntity.ok(podcastEpisode.episodeTitle)
	}
}