package com.rtomyj.three_six_oh.dao

import com.rtomyj.three_six_oh.model.PodcastInfo
import org.springframework.beans.factory.annotation.Autowired
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

    val getPodcastInfo = "SELECT * FROM podcast_info WHERE podcast_id = 1"

    // TODO: add exception handling for date creation
    override fun getPodcastInfo(): PodcastInfo {

        val podcastInfo: MutableList<PodcastInfo> = namedParameterJdbcTemplate.query(getPodcastInfo, fun(rs: ResultSet, _: Int): PodcastInfo {
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
}