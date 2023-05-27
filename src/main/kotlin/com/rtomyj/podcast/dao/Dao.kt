package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.PodcastEpisode

interface Dao {
	fun storeNewPodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
	fun updatePodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
}