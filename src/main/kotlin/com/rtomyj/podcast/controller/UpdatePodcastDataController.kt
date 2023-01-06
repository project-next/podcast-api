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
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
@Validated
class UpdatePodcastDataController {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@Autowired
	private lateinit var podcastService: PodcastService

	@PutMapping("/podcast/{podcastId}")
	@Throws(PodcastException::class)
	fun updatePodcast(
		@PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String, @Valid @RequestBody podcast: Podcast
	): ResponseEntity<Status> {
		log.info("Updating info of an existing podcast using ID {}", podcastId)
		podcastService.updatePodcast(podcastId, podcast)

		return ResponseEntity.ok(Status("Successfully updated podcast!"))
	}

	@PutMapping("/podcast/{podcastId}/episode")
	@Throws(PodcastException::class)
	fun updatePodcastEpisode(
		@PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String, @Valid @RequestBody podcastEpisode: PodcastEpisode
	): ResponseEntity<String> {
		log.info(
			"Attempting to update episode w/ name [{}]. ID of podcast is {} and the episode ID is {}",
			podcastEpisode.title,
			podcastEpisode.podcastId,
					podcastEpisode.episodeId
		)
		podcastService.updatePodcastEpisode(podcastId, podcastEpisode)

		log.info("Successfully added new episode!")
		return ResponseEntity.ok("Successfully updated podcast.")
	}
}