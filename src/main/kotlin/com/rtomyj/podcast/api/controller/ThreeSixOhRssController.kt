package com.rtomyj.three_six_oh.controller

import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.io.ITunesGenerator
import com.rtomyj.three_six_oh.component.ThreeSixOhRssComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.View;


@RestController
class ThreeSixOhRssController
{

    @Autowired
    lateinit var rssView: ThreeSixOhRssComponent

    @GetMapping
    fun getFeed(): View
    {
        return rssView
    }

}