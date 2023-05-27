package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.PodcastEpisode

interface Dao {
	fun updatePodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String)
}