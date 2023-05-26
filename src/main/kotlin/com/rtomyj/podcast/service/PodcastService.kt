package com.rtomyj.podcast.service

import com.rtomyj.podcast.dao.Dao
import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastData
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.RssFeed
import com.rtomyj.podcast.util.enum.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class PodcastService @Autowired constructor(
	val dao: Dao,
	val podcastCrudRepository: PodcastCrudRepository,
	val podcastEpisodePagingAndSortingRepository: PodcastEpisodePagingAndSortingRepository
) {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	fun getRssFeedForPodcast(podcastId: String): RssFeed {
		val podcastInfo = getPodcastInfo(podcastId)
		val podcastEpisodes = podcastEpisodePagingAndSortingRepository.findAllByPodcastId(podcastId, Sort.by("publicationDate"))

		return RssFeed(podcastInfo, podcastEpisodes)
	}

	fun getPodcastData(podcastId: String): PodcastData {
		val podcastInfo = getPodcastInfo(podcastId)
		val podcastEpisodes = podcastEpisodePagingAndSortingRepository.findAllByPodcastId(podcastId, Sort.by("publicationDate"))

		return PodcastData(podcastInfo, podcastEpisodes)
	}

	private fun getPodcastInfo(podcastId: String) =
		podcastCrudRepository.findById(podcastId).getOrNull() ?: throw PodcastException("Podcast not found in DB", ErrorType.DB001)

	fun storeNewPodcast(podcast: Podcast) {
		dao.storeNewPodcast(podcast)
	}

	fun updatePodcast(podcastId: String, podcast: Podcast) {
		dao.updatePodcast(podcastId, podcast)
	}

	fun storeNewPodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		if (podcastId != podcastEpisode.podcastId) {
			throw PodcastException("Podcast ID from URL and the one from the body do not match!", ErrorType.G005)
		}

		val delimitedKeywords = podcastEpisode.keywords.joinToString(separator = "|")
		log.info("Using the following keywords: {}", delimitedKeywords)

		dao.storeNewPodcastEpisode(podcastEpisode, delimitedKeywords)
	}

	fun updatePodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		if (podcastId != podcastEpisode.podcastId) {
			throw PodcastException("Podcast ID from URL and the one from the body do not match!", ErrorType.G005)
		}

		val delimitedKeywords = podcastEpisode.keywords.joinToString(separator = "|")
		log.info("Using the following keywords: {}", delimitedKeywords)

		dao.updatePodcastEpisode(podcastEpisode, delimitedKeywords)
	}
}