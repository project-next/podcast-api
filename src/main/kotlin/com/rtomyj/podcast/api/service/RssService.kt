package com.rtomyj.podcast.api.service

import com.rtomyj.podcast.api.dao.Dao
import com.rtomyj.podcast.api.model.RssFeed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service


@Service
class RssService {
	@Autowired
	@Qualifier("jdbc")
	private lateinit var dao: Dao


	fun getRssFeedForPodcast(podcastId: String): RssFeed {
		val podcastInfo = dao.getPodcastInfo(podcastId) ?: throw Exception()
		val podcastEpisodes = dao.getPodcastEpisodes(podcastId)

		return RssFeed(podcastInfo, podcastEpisodes)
	}
}