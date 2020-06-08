package com.rtomyj.podcast.api.controller

import com.rtomyj.podcast.api.component.RssComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View;


@RestController
class RssController
{

    @Autowired
    lateinit var rssView: RssComponent

    @GetMapping
    fun getFeed(): View
    {
        return rssView
    }

}