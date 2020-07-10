package com.rtomyj.podcast.api.service

import com.rtomyj.podcast.api.model.RssFeed
import com.rtomyj.podcast.api.dao.Dao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service


@Service
class RssService
{

    @Autowired
    @Qualifier("Jdbc")
    lateinit var dao: Dao

    fun getRssFeedForPodcast(podcastId: Int): RssFeed = RssFeed(dao.getPodcastInfo(podcastId), dao.getPodcastEpisodes(podcastId))

}