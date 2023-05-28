package com.rtomyj.podcast.controller

import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
class RetrievePodcastDataController {
	@Autowired
	private lateinit var podcastService: PodcastService

	@GetMapping(value = ["/podcast/{podcastId}", "/podcast/feed/{podcastId}"])
	fun getPodcastFeedUsingPodcastId(@PathVariable @Size(min = 36, max = 36) podcastId: String) = podcastService.getRssFeedForPodcast(podcastId)

	@GetMapping("/podcast/json/{podcastId}")
	fun getPodcastJSONUsingPodcastIdAsJson(@PathVariable @Size(min = 36, max = 36) podcastId: String) = podcastService.getPodcastData(podcastId)
}