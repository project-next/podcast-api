package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.Podcast

interface Dao {
	fun getPodcastInfo(podcastId: String): Podcast
	fun getPodcastEpisodes(podcastId: String): ArrayList<PodcastEpisode>
	fun storeNewPodcast(podcast: Podcast)
}