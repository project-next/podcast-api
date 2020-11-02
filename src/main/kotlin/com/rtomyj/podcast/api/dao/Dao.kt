package com.rtomyj.podcast.api.dao

import com.rtomyj.podcast.api.model.PodcastEpisode
import com.rtomyj.podcast.api.model.PodcastInfo

interface Dao
{

    fun getPodcastInfo(podcastId: String): PodcastInfo?
    fun getPodcastEpisodes(podcastId: String): ArrayList<PodcastEpisode>

}