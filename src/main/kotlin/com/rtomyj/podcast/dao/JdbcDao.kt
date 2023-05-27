package com.rtomyj.podcast.dao

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.constant.SqlQueries
import com.rtomyj.podcast.util.enum.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.SQLException

@Repository("jdbc")
class JdbcDao @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : Dao {

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)

		private const val DataIntegrityViolationExceptionLog = "DataIntegrityViolationException occurred while inserting new podcast info. {}"
		private const val SQLExceptionLog = "SQLException occurred while inserting new podcast info. {}"

		const val SOMETHING_WENT_WRONG = "Something went wrong!"
		const val DATA_CONSTRAINT_ISSUE = "Data constraint issue!"
		const val NO_ROWS_UPDATED = "No rows updated!"
	}

	override fun storeNewPodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String) {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("podcast_id", podcastEpisode.podcastId)
		sqlParams.addValue("episode_title", podcastEpisode.title)
		sqlParams.addValue("episode_webpage_link", podcastEpisode.episodeWebpageLink)
		sqlParams.addValue("episode_audio_link", podcastEpisode.episodeAudioLink)
		sqlParams.addValue("episode_description", podcastEpisode.description)
		sqlParams.addValue("episode_author", podcastEpisode.author)
		sqlParams.addValue("episode_image", podcastEpisode.imageLink)
		sqlParams.addValue("episode_keywords", delimitedKeywords)   // keywords should be delimited correctly in service layer
		sqlParams.addValue("episode_guid", podcastEpisode.episodeId)
		sqlParams.addValue("episode_length", podcastEpisode.length)
		sqlParams.addValue("episode_media_type", podcastEpisode.mediaType)
		sqlParams.addValue("is_episode_explicit", podcastEpisode.isExplicit)
		sqlParams.addValue("episode_duration", podcastEpisode.duration)

		try {
			namedParameterJdbcTemplate.update(SqlQueries.INSERT_NEW_PODCAST_EPISODE_QUERY, sqlParams)
		} catch (ex: DataIntegrityViolationException) {
			log.error(DataIntegrityViolationExceptionLog, ex.toString())
			throw PodcastException(DATA_CONSTRAINT_ISSUE, ErrorType.DB002)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB002)
		}
	}

	override fun updatePodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String) {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("episode_title", podcastEpisode.title)
		sqlParams.addValue("episode_webpage_link", podcastEpisode.episodeWebpageLink)
		sqlParams.addValue("episode_audio_link", podcastEpisode.episodeAudioLink)
		sqlParams.addValue("episode_description", podcastEpisode.description)
		sqlParams.addValue("episode_author", podcastEpisode.author)
		sqlParams.addValue("episode_image", podcastEpisode.imageLink)
		sqlParams.addValue("episode_keywords", delimitedKeywords)   // keywords should be delimited correctly in service layer
		sqlParams.addValue("episode_guid", podcastEpisode.episodeId)
		sqlParams.addValue("episode_length", podcastEpisode.length)
		sqlParams.addValue("episode_media_type", podcastEpisode.mediaType)
		sqlParams.addValue("is_episode_explicit", podcastEpisode.isExplicit)
		sqlParams.addValue("episode_duration", podcastEpisode.duration)

		try {
			if (namedParameterJdbcTemplate.update(SqlQueries.UPDATE_PODCAST_EPISODE_QUERY, sqlParams) == 0) {
				throw PodcastException(NO_ROWS_UPDATED, ErrorType.DB004)
			}
		} catch (ex: DataIntegrityViolationException) {
			log.error(DataIntegrityViolationExceptionLog, ex.toString())
			throw PodcastException(DATA_CONSTRAINT_ISSUE, ErrorType.DB002)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB002)
		}
	}
}