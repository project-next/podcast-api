package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.service.RssService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RssController {
    @Autowired
    private lateinit var rssService: RssService

    @GetMapping("api/v1/podcast/{podcastId}")
    fun getPodcastInfoUsingPodcastId(@PathVariable podcastId: String) = rssService.getRssFeedForPodcast(podcastId)
}