package com.rtomyj.podcast.controller

import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.Status
import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
@Validated
class UpdatePodcastDataController {
	@Autowired
	private lateinit var podcastService: PodcastService

	@PutMapping("/podcast/{podcastId}")
	fun updatePodcast(
		@PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String, @Valid @RequestBody podcast: Podcast
	): ResponseEntity<Status> {
		podcastService.updatePodcast(podcastId, podcast)
		return ResponseEntity.ok(Status("Successfully updated podcast!"))
	}

	@PutMapping("/podcast/{podcastId}/episode")
	fun updatePodcastEpisode(
		@PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String, @Valid @RequestBody podcastEpisode: PodcastEpisode
	): ResponseEntity<Status> {
		podcastService.updatePodcastEpisode(podcastId, podcastEpisode)
		return ResponseEntity.ok(Status("Successfully updated podcast episode!"))
	}
}