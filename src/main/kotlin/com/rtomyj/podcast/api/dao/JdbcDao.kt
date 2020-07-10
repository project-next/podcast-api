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

@Repository("Jdbc")
class JdbcDao: Dao
{

    val dbDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Autowired
    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate


    // TODO: add exception handling for date creation
    override fun getPodcastInfo(podcastId: Int): PodcastInfo
    {

        val mapSqlParameterSource = MapSqlParameterSource()
        mapSqlParameterSource.addValue("podcastId", podcastId)

        val podcastInfo = namedParameterJdbcTemplate.query(Constants.PODCAST_INFO_QUERY, mapSqlParameterSource, fun(rs: ResultSet, _: Int): PodcastInfo {
            val podcastInfo = PodcastInfo()
            with(podcastInfo)
            {
                this.podcastId = rs.getInt(1)
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

            return podcastInfo
        })

        return podcastInfo[0]

    }


    override fun getPodcastEpisodes(podcastId: Int): ArrayList<PodcastEpisode> {

        val sqlParams = MapSqlParameterSource();
        sqlParams.addValue("podcastId", podcastId)

//episode_title, podcast_id, episode_link, 4 episode_description, episode_pub_date, episode_author, episode_image, 8 episode_keywords, episode_guid, episode_length, episode_media_type, is_episode_explicit
        return namedParameterJdbcTemplate.query(Constants.PODCAST_EPISODES_QUERY, sqlParams, fun(row: ResultSet, _: Int): PodcastEpisode {
            val podcastEpisode = PodcastEpisode()
            with(podcastEpisode)
            {
                episodeTitle = row.getString(1)
                this.podcastId = podcastId
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

            return podcastEpisode
        }) as ArrayList<PodcastEpisode>
    }

}