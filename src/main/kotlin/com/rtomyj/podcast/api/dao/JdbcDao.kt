package com.rtomyj.podcast.api.dao

import com.rtomyj.podcast.api.helper.Constants
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

        return namedParameterJdbcTemplate.queryForObject(Constants.PODCAST_INFO_QUERY, mapSqlParameterSource
                , fun(rs: ResultSet, _: Int): PodcastInfo
        {

            return PodcastInfo(podcastId).apply {
                podcastTitle = rs.getString(2)
                podcastLink = URL(rs.getString(3))
                podcastDescription = rs.getString(4)
                podcastLanguage = rs.getString(5)
                podcastCopyright = rs.getString(6)
                podcastLastBuildDate = LocalDateTime.from(dbDate.parse(rs.getString(7)))
                podcastEmail = rs.getString(8)
                podcastCategory = rs.getString(9)
                podcastAuthor = rs.getString(10)
                isExplicit = rs.getBoolean(11)
                podcastImageUrl = URL(rs.getString(12))
            }

        })

    }


    override fun getPodcastEpisodes(podcastId: Int): ArrayList<PodcastEpisode> {

        val sqlParams = MapSqlParameterSource();
        sqlParams.addValue("podcastId", podcastId)

        return namedParameterJdbcTemplate.query(Constants.PODCAST_EPISODES_QUERY, sqlParams
                , fun(row: ResultSet, _: Int): PodcastEpisode
        {

            return PodcastEpisode(podcastId).apply {
                episodeTitle = row.getString(1)
                episodeLink = URL(row.getString(3))
                episodeDescription = row.getString(4)
                episodePublicationDate = LocalDateTime.from(dbDate.parse(row.getString(5)))
                episodeAuthor = row.getString(6)
                episodeImage = URL(row.getString(7))

                val keywords = row.getString(8)
                keywords.split("|").toCollection(episodeKeywords)

                episodeGuid.value = row.getString(9)
                episodeLength = row.getLong(10)
                episodeMediaType = row.getString(11)
                isEpisodeExplicit = row.getBoolean(12)
            }

        }) as ArrayList<PodcastEpisode>
    }

}