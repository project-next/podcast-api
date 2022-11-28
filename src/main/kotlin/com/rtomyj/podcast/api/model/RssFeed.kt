package com.rtomyj.podcast.api.model

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import org.springframework.web.servlet.view.feed.AbstractRssFeedView
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


class RssFeed(private val podcastInfo: PodcastInfo, private val podcastEpisodes: List<PodcastEpisode>): AbstractRssFeedView()
{

    override fun buildFeedMetadata(model: MutableMap<String, Any>
                                   , feed: Channel, request: HttpServletRequest) = podcastInfo.populateChannelInfo(feed)


    override fun buildFeedItems(
            model: MutableMap<String, Any>
            , request: HttpServletRequest
            , response: HttpServletResponse)
            : ArrayList<Item>
    {

        val episodes = podcastEpisodes
                .map { podcastEpisode ->  podcastEpisode.toRssItem()}

        return episodes as ArrayList<Item>

    }

}