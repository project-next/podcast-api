package com.rtomyj.podcast.controller

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.Status
import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
	fun storeNewPodcast(@Valid @RequestBody podcast: Podcast): ResponseEntity<Status> {
		log.info("Attempting to store new podcast w/ name {}. ID of podcast will be {} if storage is successful", podcast.title, podcast.id)
		podcastService.storeNewPodcast(podcast)
		log.info("Successfully added new podcast!")
		return ResponseEntity(Status("Successfully stored new podcast!"), HttpStatus.CREATED)
	}

	@PostMapping("/podcast/{podcastId}/episode")
	@Throws(PodcastException::class)
	fun storeNewPodcastEpisode(
		@PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String, @Valid @RequestBody podcastEpisode: PodcastEpisode
	): ResponseEntity<Status> {
		log.info(
			"Attempting to store new episode w/ name {}. ID of episode will be {} if storage is successful. ID of podcast to associate episode is {}",
			podcastEpisode.title,
			podcastEpisode.episodeId,
			podcastEpisode.podcastId
		)
		podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)
		log.info("Successfully added new episode!")
		return ResponseEntity(Status("Successfully stored new podcast episode!"), HttpStatus.CREATED)
	}
}