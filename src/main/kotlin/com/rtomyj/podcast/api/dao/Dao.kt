package com.rtomyj.podcast.api.dao

import com.rtomyj.podcast.api.model.PodcastEpisode
import com.rtomyj.podcast.api.model.Podcast

interface Dao {
	fun getPodcastInfo(podcastId: String): Podcast?
	fun getPodcastEpisodes(podcastId: String): ArrayList<PodcastEpisode>
}