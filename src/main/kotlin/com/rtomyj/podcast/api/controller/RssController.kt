package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.service.RssService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View;


@RestController
class RssController
{

    @Autowired
    private lateinit var rssService: RssService

    @GetMapping
    fun getFeed(): View = rssService.getRssFeedForPodcast(1)

}