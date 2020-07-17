package com.rtomyj.podcast.api.dao

import com.rtomyj.podcast.api.constant.SqlQueries
import com.rtomyj.podcast.api.enum.PodcastApiTables.PodcastInfoTableColumns
import com.rtomyj.podcast.api.enum.PodcastApiTables.PodcastEpisodeTableColumns
import com.rtomyj.podcast.api.model.PodcastEpisode
import com.rtomyj.podcast.api.model.PodcastInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.net.URL
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository("jdbc")
class JdbcDao: Dao
{

    val dbDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Autowired
    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate


    // TODO: add exception handling for date creation
    override fun getPodcastInfo(podcastId: Int): PodcastInfo?
    {

        val mapSqlParameterSource = MapSqlParameterSource()
        mapSqlParameterSource.addValue("podcastId", podcastId)

        return namedParameterJdbcTemplate.queryForObject(SqlQueries.PODCAST_INFO_QUERY, mapSqlParameterSource
                , fun(rs: ResultSet, _: Int): PodcastInfo
        {

            return PodcastInfo(podcastId).apply {
                podcastTitle = rs.getString(PodcastInfoTableColumns.PODCAST_TITLE.columnName)
                podcastLink = URL(rs.getString(PodcastInfoTableColumns.PODCAST_LINK.columnName))
                podcastDescription = rs.getString(PodcastInfoTableColumns.PODCAST_DESCRIPTION.columnName)
                podcastLanguage = rs.getString(PodcastInfoTableColumns.PODCAST_LANGUAGE.columnName)
                podcastCopyright = rs.getString(PodcastInfoTableColumns.PODCAST_COPYRIGHT.columnName)
                podcastLastBuildDate = LocalDateTime.from(dbDate.parse(rs.getString(PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE.columnName)))
                podcastEmail = rs.getString(PodcastInfoTableColumns.PODCAST_EMAIL.columnName)
                podcastCategory = rs.getString(PodcastInfoTableColumns.PODCAST_CATEGORY.columnName)
                podcastAuthor = rs.getString(PodcastInfoTableColumns.PODCAST_AUTHOR.columnName)
                isExplicit = rs.getBoolean(PodcastInfoTableColumns.IS_EXPLICIT.columnName)
                podcastImageUrl = URL(rs.getString(PodcastInfoTableColumns.PODCAST_IMAGE_URL.columnName))
            }

        })

    }


    override fun getPodcastEpisodes(podcastId: Int): ArrayList<PodcastEpisode> {

        val sqlParams = MapSqlParameterSource();
        sqlParams.addValue("podcastId", podcastId)

        return namedParameterJdbcTemplate.query(SqlQueries.PODCAST_EPISODES_QUERY, sqlParams
                , fun(row: ResultSet, _: Int): PodcastEpisode
        {

            return PodcastEpisode(podcastId).apply {
                episodeTitle = row.getString(PodcastEpisodeTableColumns.EPISODE_TITLE.columnName)
                episodeLink = URL(row.getString(PodcastEpisodeTableColumns.EPISODE_LINK.columnName))
                episodeDescription = row.getString(PodcastEpisodeTableColumns.EPISODE_DESCRIPTION.columnName)
                episodePublicationDate = LocalDateTime.from(dbDate.parse(row.getString(PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE.columnName)))
                episodeAuthor = row.getString(PodcastEpisodeTableColumns.EPISODE_AUTHOR.columnName)
                episodeImage = URL(row.getString(PodcastEpisodeTableColumns.EPISODE_IMAGE.columnName))

                val keywords = row.getString(PodcastEpisodeTableColumns.EPISODE_KEYWORDS.columnName)
                keywords.split("|").toCollection(episodeKeywords)

                episodeGuid.value = row.getString(PodcastEpisodeTableColumns.EPISODE_GUID.columnName)
                episodeLength = row.getLong(PodcastEpisodeTableColumns.EPISODE_LENGTH.columnName)
                episodeMediaType = row.getString(PodcastEpisodeTableColumns.EPISODE__MEDIA_TYPE.columnName)
                isEpisodeExplicit = row.getBoolean(PodcastEpisodeTableColumns.IS_EXPLICIT.columnName)
                episodeDuration = row.getString(PodcastEpisodeTableColumns.EPISODE_DURATION.columnName)
            }

        }) as ArrayList<PodcastEpisode>
    }

}