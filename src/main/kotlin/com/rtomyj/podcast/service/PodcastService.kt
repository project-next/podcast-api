package com.rtomyj.podcast.service

import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.RssFeed
import com.rtomyj.podcast.util.TransformToFeedUtil
import com.rtomyj.podcast.util.enum.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull


@Service
class PodcastService @Autowired constructor(
    val podcastCrudRepository: PodcastCrudRepository,
    val podcastEpisodePagingAndSortingRepository: PodcastEpisodePagingAndSortingRepository,
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)

        private const val SQL_EXCEPTION_LOG = "SQLException occurred while inserting new podcast info. {}"
        private const val PODCAST_ID_NOT_FOUND = "Podcast ID not found in DB"
        private const val EPISODE_ID_NOT_FOUND = "Episode ID not found in DB"
        private const val SOMETHING_WENT_WRONG = "Something went wrong!"
    }

    fun getRssFeedForPodcast(podcastId: String) = RssFeed(getPodcastData(podcastId), TransformToFeedUtil())

    @Transactional(readOnly = true)
    fun getPodcastData(podcastId: String): Podcast {
        log.info("Retrieving podcast info and episodes for podcast w/ ID: {}", podcastId)
        val podcast = getPodcastInfo(podcastId)

        log.info(
            "Podcast w/ ID: {} and name {} has {} episodes.", podcastId, podcast.title,
            podcast.episodes.size
        )
        return podcast
    }

    private fun getPodcastInfo(podcastId: String) =
        podcastCrudRepository.findById(podcastId).getOrNull() ?: throw PodcastException(
            PODCAST_ID_NOT_FOUND,
            ErrorType.DB001
        )

    @Transactional
    fun storeNewPodcast(podcast: Podcast) {
        log.info("Attempting to store new podcast w/ name {}", podcast.title)
        savePodcast(podcast)
        log.info("Successfully added new podcast [{}] - ID: {}", podcast.title, podcast.id)
    }


    @Transactional
    fun updatePodcast(podcastId: String, podcast: Podcast) {
        log.info("Updating info of an existing podcast using ID {}", podcastId)

        if (podcastCrudRepository.findById(podcastId).isEmpty) {
            log.error("Podcast with ID {} not found in DB, therefore cannot update podcast information.", podcastId)
            throw PodcastException(PODCAST_ID_NOT_FOUND, ErrorType.DB001)
        }
        podcast.id = podcastId
        savePodcast(podcast)

        log.info("Successfully updated podcast w/ ID {}", podcastId)
    }

    private fun savePodcast(podcast: Podcast) {
        try {
            podcastCrudRepository.save(podcast)
        } catch (ex: DataAccessException) {
            log.error(SQL_EXCEPTION_LOG, ex.toString())
            throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
        }
    }

    @Transactional
    fun storeNewPodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
        podcastEpisode.podcastId = podcastId
        log.info(
            "Attempting to store new episode w/ name {}. ID of episode will be {} if storage is successful. ID of podcast to associate episode is {}",
            podcastEpisode.title,
            podcastEpisode.episodeId,
            podcastEpisode.podcastId
        )

        savePodcastEpisode(podcastEpisode)
        log.info("Successfully added new episode [{}] - episode ID: {}", podcastEpisode.title, podcastEpisode.episodeId)
    }

    @Transactional
    fun updatePodcastEpisode(podcastId: String, podcastEpisode: PodcastEpisode) {
        log.info(
            "Attempting to update episode w/ name [{}]. ID of podcast is {} and the episode ID is {}",
            podcastEpisode.title,
            podcastEpisode.podcastId,
            podcastEpisode.episodeId
        )

        podcastEpisode.podcastId = podcastId
        val dbPodcast = podcastEpisodePagingAndSortingRepository.findById(podcastEpisode.episodeId)
        if (dbPodcast.isEmpty) {
            log.error(
                "Podcast episode with ID {} not found in DB, therefore cannot update podcast episode.",
                podcastEpisode.episodeId
            )
            throw PodcastException(EPISODE_ID_NOT_FOUND, ErrorType.DB001)
        } else if (dbPodcast.get().podcastId != podcastEpisode.podcastId) {
            log.error("Podcast ID from request does not match ID in DB for given podcast episode.")
            throw PodcastException("Podcast ID mismatch", ErrorType.DB003)
        }
        savePodcastEpisode(podcastEpisode)

        log.info("Successfully updated episode")
    }

    private fun savePodcastEpisode(podcastEpisode: PodcastEpisode) {
        if (podcastCrudRepository.findById(podcastEpisode.podcastId).isEmpty) {
            log.error(
                "Podcast with ID {} not found in DB, therefore cannot update podcast episode.",
                podcastEpisode.podcastId
            )
            throw PodcastException(PODCAST_ID_NOT_FOUND, ErrorType.DB001)
        }

        try {
            podcastEpisodePagingAndSortingRepository.save(podcastEpisode)
        } catch (ex: DataAccessException) {
            log.error(SQL_EXCEPTION_LOG, ex.toString())
            throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
        }
    }

    @Transactional
    fun deletePodcastEpisode(podcastEpisodeId: String) {
        log.info("Deleting podcast episode with ID {}", podcastEpisodeId)

        try {
            if (podcastEpisodePagingAndSortingRepository.existsById(podcastEpisodeId)) {
                podcastEpisodePagingAndSortingRepository.deleteById(podcastEpisodeId)
            } else {
                log.error("Episode doesn't exist - nothing to delete")
                throw PodcastException(EPISODE_ID_NOT_FOUND, ErrorType.DB001)
            }
        } catch (ex: DataAccessException) {
            log.error("Exception occurred while deleting podcast episode {}", ex.toString())
            throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB003)
        }
    }
}