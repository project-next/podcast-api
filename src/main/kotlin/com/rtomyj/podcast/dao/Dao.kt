package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode

interface Dao {
	fun storeNewPodcast(podcast: Podcast)
	fun storeNewPodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
	fun updatePodcast(podcastId: String, podcast: Podcast)
	fun updatePodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
}