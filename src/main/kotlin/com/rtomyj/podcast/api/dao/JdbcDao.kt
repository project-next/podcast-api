package com.rtomyj.podcast.api.dao

import com.rometools.rome.feed.rss.Guid
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
import java.util.*
import kotlin.collections.ArrayList

@Repository("Jdbc")
class JdbcDao: Dao
{

    val dbDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Autowired
    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate


    // TODO: add exception handling for date creation
    override fun getPodcastInfo(): PodcastInfo
    {

        val podcastInfo: MutableList<PodcastInfo> = namedParameterJdbcTemplate.query(Constants.PODCAST_INFO_QUERY, fun(rs: ResultSet, _: Int): PodcastInfo {
            val podcastInfo = PodcastInfo()
            podcastInfo.podcastId = rs.getInt(1)
            podcastInfo.podcastTitle = rs.getString(2)
            podcastInfo.podcastLink = URL(rs.getString(3))
            podcastInfo.podcastDescription = rs.getString(4)
            podcastInfo.podcastLanguage = rs.getString(5)
            podcastInfo.podcastCopyright = rs.getString(6)
            podcastInfo.podcastLastBuildDate = LocalDateTime.from(dbDate.parse(rs.getString(7)))
            podcastInfo.podcastEmail = rs.getString(8)
            podcastInfo.podcastCategory = rs.getString(9)
            podcastInfo.podcastAuthor = rs.getString(10)
            podcastInfo.isExplicit = rs.getBoolean(11)
            podcastInfo.podcastImageUrl = URL(rs.getString(12))
            podcastInfo.podcastSummary = rs.getString(13)

            return podcastInfo
        })

        return podcastInfo[0]
    }


    override fun getPodcastEpisodes(podcastId: Int): ArrayList<PodcastEpisode> {

        val sqlParams = MapSqlParameterSource();
        sqlParams.addValue("podcastId", podcastId)


        return namedParameterJdbcTemplate.query(Constants.PODCAST_EPISODES_QUERY, sqlParams, fun(row: ResultSet, _: Int): PodcastEpisode {
            val podcastEpisode = PodcastEpisode()
            podcastEpisode.episodetitle = row.getString(1)
            podcastEpisode.podcastId = podcastId
            podcastEpisode.episodeTitle = row.getString(2)
            podcastEpisode.episodeLink = URL(row.getString(3))
            podcastEpisode.episodeDescription = row.getString(4)
            podcastEpisode.episodePublicationDate = LocalDateTime.from(dbDate.parse(row.getString(5)))
            podcastEpisode.episodeAuthor = row.getString(6)
            podcastEpisode.episodeImage = URL(row.getString(7))
            podcastEpisode.episodeSummary = row.getString(8)

            val keywords = row.getString(9)
            keywords.split("|").toCollection(podcastEpisode.episodeKeywords)

            podcastEpisode.episodeGuid.value = row.getString(10)
            podcastEpisode.episodeLength = row.getLong(11)
            return podcastEpisode
        }) as ArrayList<PodcastEpisode>
    }

}