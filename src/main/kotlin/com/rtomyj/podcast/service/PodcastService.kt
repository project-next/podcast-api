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

		private const val SQLExceptionLog = "SQLException occurred while inserting new podcast info. {}"

		private const val SOMETHING_WENT_WRONG = "Something went wrong!"
		private const val DB_MISSING_RECORD_CANNOT_UPDATE = "Could not update record as it DNE in DB."
	}

	fun getRssFeedForPodcast(podcastId: String) = RssFeed(getPodcastDataFromDB(podcastId))

	fun getPodcastData(podcastId: String): PodcastData = getPodcastDataFromDB(podcastId)

	private fun getPodcastDataFromDB(podcastId: String): PodcastData {
		log.info("Retrieving podcast info and episodes for podcast w/ ID: {}", podcastId)
		val podcastInfo = getPodcastInfo(podcastId)
		val podcastEpisodes = getPodcastEpisodes(podcastId)

		log.info("Podcast w/ ID: {} and name {} has a feed with {} episodes.", podcastId, podcastInfo.title,
			podcastEpisodes.size)
		return PodcastData(podcastInfo, podcastEpisodes)
	}

	private fun getPodcastInfo(podcastId: String) =
		podcastCrudRepository.findById(podcastId).getOrNull() ?: throw PodcastException("Podcast not found in DB", ErrorType.DB001)

	private fun getPodcastEpisodes(podcastId: String) = podcastEpisodePagingAndSortingRepository.findAllByPodcastId(podcastId, Sort.by("publicationDate"))

	fun storeNewPodcast(podcast: Podcast) {
		log.info("Attempting to store new podcast w/ name {}. ID of podcast will be {} if storage is successful",
			podcast.title, podcast.id)
		savePodcast(podcast)
		log.info("Successfully added new podcast!")
	}

	fun updatePodcast(podcastId: String, podcast: Podcast) {
		if (podcastCrudRepository.findById(podcastId).isEmpty) {
			log.error("Podcast with ID {} not found in DB, therefore cannot update podcast information.", podcastId)
			throw PodcastException(DB_MISSING_RECORD_CANNOT_UPDATE, ErrorType.DB004)
		}
		podcast.id = podcastId
		savePodcast(podcast)
	}

	private fun savePodcast(podcast: Podcast) {
		try {
			podcastCrudRepository.save(podcast)
		}  catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
		}
	}

	fun storeNewPodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		podcastEpisode.podcastId = podcastId
		log.info(
			"Attempting to store new episode w/ name {}. ID of episode will be {} if storage is successful. ID of podcast to associate episode is {}",
			podcastEpisode.title,
			podcastEpisode.episodeId,
			podcastEpisode.podcastId
		)

		savePodcastEpisode(podcastEpisode)
		log.info("Successfully added new episode!")
	}

	fun updatePodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
		podcastEpisode.podcastId = podcastId

		if (podcastCrudRepository.findById(podcastId).isEmpty) {
			log.error("Podcast with ID {} not found in DB, therefore cannot update podcast episode.", podcastId)
			throw PodcastException(DB_MISSING_RECORD_CANNOT_UPDATE, ErrorType.DB004)
		}

		if (podcastEpisodeCrudRepository.findById(podcastEpisode.episodeId).isEmpty) {
			log.error("Podcast episode with ID {} not found in DB, therefore cannot update podcast episode.", podcastEpisode.episodeId)
			throw PodcastException(DB_MISSING_RECORD_CANNOT_UPDATE, ErrorType.DB004)
		}

		savePodcastEpisode(podcastEpisode)
	}

	private fun savePodcastEpisode(podcastEpisode: PodcastEpisode) {
		try {
			podcastEpisodeCrudRepository.save(podcastEpisode)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
		}
	}
}