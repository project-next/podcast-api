package com.rtomyj.podcast.service

import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodeCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastData
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.RssFeed
import com.rtomyj.podcast.util.enum.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.sql.SQLException
import kotlin.jvm.optionals.getOrNull


@Service
class PodcastService @Autowired constructor(
	val podcastCrudRepository: PodcastCrudRepository,
	val podcastEpisodePagingAndSortingRepository: PodcastEpisodePagingAndSortingRepository,
	val podcastEpisodeCrudRepository: PodcastEpisodeCrudRepository
) {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)

		private const val DataIntegrityViolationExceptionLog = "DataIntegrityViolationException occurred while inserting new podcast info. {}"
		private const val SQLExceptionLog = "SQLException occurred while inserting new podcast info. {}"

		private const val SOMETHING_WENT_WRONG = "Something went wrong!"
		private const val DATA_CONSTRAINT_ISSUE = "Data constraint issue!"
		private const val NO_ROWS_UPDATED = "No rows updated!"
	}

	fun getRssFeedForPodcast(podcastId: String): RssFeed {
		log.info("Retrieving feed for podcast w/ ID: {}", podcastId)
		val podcastInfo = getPodcastInfo(podcastId)
		val podcastEpisodes = getPodcastEpisodes(podcastId)

		log.info("Found podcast w/ ID: {}. Total episodes: {}", podcastId, podcastEpisodes.size)
		return RssFeed(podcastInfo, podcastEpisodes)
	}

	fun getPodcastData(podcastId: String): PodcastData {
		log.info("Retrieving data for podcast w/ ID: {}", podcastId)
		val podcastInfo = getPodcastInfo(podcastId)
		val podcastEpisodes = getPodcastEpisodes(podcastId)

		log.info("Found podcast w/ ID: {}. Total episodes: {}", podcastId, podcastEpisodes.size)
		return PodcastData(podcastInfo, podcastEpisodes)
	}

	private fun getPodcastInfo(podcastId: String) =
		podcastCrudRepository.findById(podcastId).getOrNull() ?: throw PodcastException("Podcast not found in DB", ErrorType.DB001)

	private fun getPodcastEpisodes(podcastId: String) = podcastEpisodePagingAndSortingRepository.findAllByPodcastId(podcastId, Sort.by("publicationDate"))

	fun storeNewPodcast(podcast: Podcast) = savePodcast(podcast)

	fun updatePodcast(podcastId: String, podcast: Podcast) {
		if (podcastCrudRepository.findById(podcastId).isEmpty) {
			log.error("Podcast with ID {} not found in DB", podcastId)
			throw PodcastException(NO_ROWS_UPDATED, ErrorType.DB004)
		}
		podcast.id = podcastId
		savePodcast(podcast)
	}

	private fun savePodcast(podcast: Podcast) {
		try {
			podcastCrudRepository.save(podcast)
		} catch (ex: DataIntegrityViolationException) {
			log.error(DataIntegrityViolationExceptionLog, ex.toString())
			throw PodcastException(DATA_CONSTRAINT_ISSUE, ErrorType.DB002)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
		}
	}

	fun storeNewPodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		if (podcastId != podcastEpisode.podcastId) {
			throw PodcastException("Podcast ID from URL and the one from the body do not match!", ErrorType.G005)
		}

		savePodcastEpisode(podcastEpisode)
	}

	fun updatePodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		if (podcastId != podcastEpisode.podcastId) {
			throw PodcastException("Podcast ID from URL and the one from the body do not match!", ErrorType.G005)
		}

		if (podcastCrudRepository.findById(podcastId).isEmpty) {
			log.error("Podcast with ID {} not found in DB", podcastId)
			throw PodcastException(NO_ROWS_UPDATED, ErrorType.DB004)
		}

		if (podcastEpisodeCrudRepository.findById(podcastEpisode.episodeId).isEmpty) {
			log.error("Podcast episode with ID {} not found in DB", podcastEpisode.episodeId)
			throw PodcastException(NO_ROWS_UPDATED, ErrorType.DB004)
		}

		savePodcastEpisode(podcastEpisode)
	}

	private fun savePodcastEpisode(podcastEpisode: PodcastEpisode) {
		try {
			podcastEpisodeCrudRepository.save(podcastEpisode)
		} catch (ex: DataIntegrityViolationException) {
			log.error(DataIntegrityViolationExceptionLog, ex.toString())
			throw PodcastException(DATA_CONSTRAINT_ISSUE, ErrorType.DB002)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
		}
	}
}