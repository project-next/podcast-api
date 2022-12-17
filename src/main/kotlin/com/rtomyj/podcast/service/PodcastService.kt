package com.rtomyj.podcast.service

import com.rtomyj.podcast.dao.Dao
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastData
import com.rtomyj.podcast.model.RssFeed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service


@Service
class PodcastService {
	@Autowired
	@Qualifier("jdbc")
	private lateinit var dao: Dao

	fun getRssFeedForPodcast(podcastId: String): RssFeed {
		val podcastInfo = dao.getPodcastInfo(podcastId)
		val podcastEpisodes = dao.getPodcastEpisodes(podcastId)

		return RssFeed(podcastInfo, podcastEpisodes)
	}

	fun getPodcastData(podcastId: String): PodcastData {
		val podcastInfo = dao.getPodcastInfo(podcastId)
		val podcastEpisodes = dao.getPodcastEpisodes(podcastId)

		return PodcastData(podcastInfo, podcastEpisodes)
	}

	fun storeNewPodcast(podcast: Podcast) {
		dao.storeNewPodcast(podcast)
	}
}