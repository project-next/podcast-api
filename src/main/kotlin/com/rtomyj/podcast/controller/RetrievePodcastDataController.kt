package com.rtomyj.podcast.controller

import com.rtomyj.podcast.service.PodcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RetrievePodcastDataController {
	@Autowired
	private lateinit var podcastService: PodcastService

	@GetMapping("/podcast/{podcastId}")
	fun getPodcastInfoUsingPodcastId(@PathVariable podcastId: String) = podcastService.getRssFeedForPodcast(podcastId)

	@GetMapping("/podcast/json/{podcastId}")
	fun getPodcastInfoUsingPodcastIdAsJson(@PathVariable podcastId: String) = podcastService.getPodcastData(podcastId)
}