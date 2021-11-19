package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.service.RssService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View;
import javax.websocket.server.PathParam


@RestController
class RssController
{

    @Autowired
    private lateinit var rssService: RssService

    @GetMapping
    fun getFeed(): View = rssService.getRssFeedForPodcast("03a0b05c-6e63-4e19-9311-e172071da997")

    @GetMapping("api/v1/podcast/{podcastId}")
    fun getPodcastInfoUsingPodcastId(@PathVariable podcastId: String) = rssService.getRssFeedForPodcast(podcastId)

}