package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.service.PodcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RetrievePodcastDataController {
	@Autowired
	private lateinit var podcastService: PodcastService

	@GetMapping("api/v1/podcast/{podcastId}")
	fun getPodcastInfoUsingPodcastId(@PathVariable podcastId: String) = podcastService.getRssFeedForPodcast(podcastId)

	@GetMapping("api/v1/podcast/json/{podcastId}")
	fun getPodcastInfoUsingPodcastIdAsJson(@PathVariable podcastId: String) = podcastService.getPodcastData(podcastId)
}