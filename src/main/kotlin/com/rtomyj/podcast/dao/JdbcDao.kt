package com.rtomyj.podcast.dao

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.constant.SqlQueries
import com.rtomyj.podcast.util.enum.ErrorType
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository("jdbc")
class JdbcDao : Dao {
	private val dbDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]")

	@Autowired
	private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)

		private const val DataIntegrityViolationExceptionLog = "DataIntegrityViolationException occurred while inserting new podcast info. {}"
		private const val SQLExceptionLog = "SQLException occurred while inserting new podcast info. {}"

		private const val SOMETHING_WENT_WRONG = "Something went wrong!"
		private const val DATA_CONSTRAINT_ISSUE = "Data constraint issue!"
	}

	override fun getPodcastInfo(podcastId: String): Podcast {
		val mapSqlParameterSource = MapSqlParameterSource()
		mapSqlParameterSource.addValue("podcastId", podcastId)

		try {
			val desiredPodcast = namedParameterJdbcTemplate.queryForObject(SqlQueries.PODCAST_INFO_QUERY, mapSqlParameterSource, fun(rs: ResultSet, _: Int): Podcast {
				return Podcast(podcastId).apply {
					podcastTitle = rs.getString(PodcastInfoTableColumns.PODCAST_TITLE.columnName)
					podcastLink = rs.getString(PodcastInfoTableColumns.PODCAST_LINK.columnName)
					podcastDescription = rs.getString(PodcastInfoTableColumns.PODCAST_DESCRIPTION.columnName)
					podcastLanguage = rs.getString(PodcastInfoTableColumns.PODCAST_LANGUAGE.columnName)
					podcastCopyright = rs.getString(PodcastInfoTableColumns.PODCAST_COPYRIGHT.columnName)
					podcastLastBuildDate = LocalDateTime.from(dbDate.parse(rs.getString(PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE.columnName)))
					podcastEmail = rs.getString(PodcastInfoTableColumns.PODCAST_EMAIL.columnName)
					podcastCategory = rs.getString(PodcastInfoTableColumns.PODCAST_CATEGORY.columnName)
					podcastAuthor = rs.getString(PodcastInfoTableColumns.PODCAST_AUTHOR.columnName)
					isExplicit = rs.getBoolean(PodcastInfoTableColumns.IS_EXPLICIT.columnName)
					podcastImageUrl = rs.getString(PodcastInfoTableColumns.PODCAST_IMAGE_URL.columnName)
				}
			})

			return desiredPodcast!!
		} catch (ex: EmptyResultDataAccessException) {
			throw PodcastException("Podcast not found in DB", ErrorType.DB001)
		}
	}

	override fun getPodcastEpisodes(podcastId: String): ArrayList<PodcastEpisode> {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("podcastId", podcastId)

		return namedParameterJdbcTemplate.query(SqlQueries.PODCAST_EPISODES_QUERY, sqlParams, fun(row: ResultSet, _: Int): PodcastEpisode {

			return PodcastEpisode(podcastId, row.getString(PodcastEpisodeTableColumns.EPISODE_GUID.columnName)).apply {
				title = row.getString(PodcastEpisodeTableColumns.EPISODE_TITLE.columnName)
				link = row.getString(PodcastEpisodeTableColumns.EPISODE_LINK.columnName)
				description = row.getString(PodcastEpisodeTableColumns.EPISODE_DESCRIPTION.columnName)
				publicationDate = LocalDateTime.from(dbDate.parse(row.getString(PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE.columnName)))
				author = row.getString(PodcastEpisodeTableColumns.EPISODE_AUTHOR.columnName)
				imageLink = row.getString(PodcastEpisodeTableColumns.EPISODE_IMAGE.columnName)

				val keywords = row.getString(PodcastEpisodeTableColumns.EPISODE_KEYWORDS.columnName)
				keywords.split("|").toCollection(this.keywords)

				length = row.getLong(PodcastEpisodeTableColumns.EPISODE_LENGTH.columnName)
				mediaType = row.getString(PodcastEpisodeTableColumns.EPISODE_MEDIA_TYPE.columnName)
				isExplicit = row.getBoolean(PodcastEpisodeTableColumns.IS_EXPLICIT.columnName)
				duration = row.getString(PodcastEpisodeTableColumns.EPISODE_DURATION.columnName)
			}

		}) as ArrayList<PodcastEpisode>
	}

	override fun storeNewPodcast(podcast: Podcast) {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("podcast_id", podcast.podcastId)
		sqlParams.addValue("podcast_title", podcast.podcastTitle)
		sqlParams.addValue("podcast_link", podcast.podcastLink)
		sqlParams.addValue("podcast_description", podcast.podcastDescription)
		sqlParams.addValue("podcast_language", podcast.podcastLanguage)
		sqlParams.addValue("podcast_copyright", podcast.podcastCopyright)
		sqlParams.addValue("podcast_email", podcast.podcastEmail)
		sqlParams.addValue("podcast_category", podcast.podcastCategory)
		sqlParams.addValue("podcast_author", podcast.podcastAuthor)
		sqlParams.addValue("is_explicit", podcast.isExplicit)
		sqlParams.addValue("podcast_image_url", podcast.podcastImageUrl)

		try {
			namedParameterJdbcTemplate.update(SqlQueries.INSERT_NEW_PODCAST_QUERY, sqlParams)
		} catch (ex: DataIntegrityViolationException) {
			log.error(DataIntegrityViolationExceptionLog, ex.toString())
			throw PodcastException(DATA_CONSTRAINT_ISSUE, ErrorType.DB002)
		} catch (ex: SQLException) {
			log.error(SQLExceptionLog, ex.toString())
			throw PodcastException(SOMETHING_WENT_WRONG, ErrorType.DB002)
		}
	}

	override fun storeNewPodcastEpisode(podcastEpisode: PodcastEpisode, delimitedKeywords: String) {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("podcast_id", podcastEpisode.podcastId)
		sqlParams.addValue("episode_title", podcastEpisode.title)
		sqlParams.addValue("episode_link", podcastEpisode.link)
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

	override fun updatePodcast(podcastId: String, podcast: Podcast) {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("podcast_id", podcastId)
		sqlParams.addValue("podcast_title", podcast.podcastTitle)
		sqlParams.addValue("podcast_link", podcast.podcastLink)
		sqlParams.addValue("podcast_description", podcast.podcastDescription)
		sqlParams.addValue("podcast_language", podcast.podcastLanguage)
		sqlParams.addValue("podcast_copyright", podcast.podcastCopyright)
		sqlParams.addValue("podcast_email", podcast.podcastEmail)
		sqlParams.addValue("podcast_category", podcast.podcastCategory)
		sqlParams.addValue("podcast_author", podcast.podcastAuthor)
		sqlParams.addValue("is_explicit", podcast.isExplicit)
		sqlParams.addValue("podcast_image_url", podcast.podcastImageUrl)

		try {
			if (namedParameterJdbcTemplate.update(SqlQueries.UPDATE_PODCAST_QUERY, sqlParams) == 0) {
				throw PodcastException("No rows updated!", ErrorType.DB004)
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