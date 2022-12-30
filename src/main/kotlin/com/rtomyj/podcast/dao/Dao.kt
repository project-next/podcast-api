package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode

interface Dao {
	fun getPodcastInfo(podcastId: String): Podcast
	fun getPodcastEpisodes(podcastId: String): ArrayList<PodcastEpisode>
	fun storeNewPodcast(podcast: Podcast)
	fun storeNewPodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
	fun updatePodcast(podcastId: String, podcast: Podcast)
}