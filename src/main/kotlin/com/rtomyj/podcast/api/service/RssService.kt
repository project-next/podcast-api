package com.rtomyj.podcast.api.service

import com.rtomyj.podcast.api.model.RssFeed
import com.rtomyj.podcast.api.dao.Dao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.lang.Exception


@Service
class RssService
{

    @Autowired
    @Qualifier("jdbc")
    lateinit var dao: Dao

    fun getRssFeedForPodcast(podcastId: Int): RssFeed
    {

        val podcastInfo = dao.getPodcastInfo(podcastId) ?: throw Exception()
        val podcastEpisodes = dao.getPodcastEpisodes(podcastId)

        return RssFeed(podcastInfo, podcastEpisodes)

    }

}