package com.rtomyj.podcast.api.component

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import com.rtomyj.podcast.api.dao.Dao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractRssFeedView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class ThreeSixOhRssComponent: AbstractRssFeedView()
{

    @Autowired
    @Qualifier("Jdbc")
    lateinit var dao: Dao


    override fun buildFeedMetadata(model: MutableMap<String, Any>, feed: Channel, request: HttpServletRequest)
    {
        val podcastInfo = dao.getPodcastInfo()
        podcastInfo.populateChanneelInfo(feed)
    }


    override fun buildFeedItems(
            model: MutableMap<String, Any>
            , request: HttpServletRequest
            , response: HttpServletResponse)
            : ArrayList<Item>
    {
        val podcastsEpisodes = dao.getPodcastEpisodes(1)
        println(podcastsEpisodes.size)

        val episodes = podcastsEpisodes
                .map { podcastEpisode ->  podcastEpisode.toRssItem()}


        println(episodes.size)
        return episodes as ArrayList<Item>
    }

}