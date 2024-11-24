package com.rtomyj.podcast.model

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import com.rtomyj.podcast.util.TransformToFeedUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.view.feed.AbstractRssFeedView

class RssFeed(private val podcast: Podcast, private val transformToFeedUtil: TransformToFeedUtil) :
    AbstractRssFeedView() {
    override fun buildFeedMetadata(
        model: MutableMap<String, Any>, feed: Channel, request: HttpServletRequest
    ) = transformToFeedUtil.populateChannelInfo(feed, podcast)

    override fun buildFeedItems(
        model: MutableMap<String, Any>, request: HttpServletRequest, response: HttpServletResponse
    ): ArrayList<Item> {
        val episodes = podcast.episodes.map { episode -> transformToFeedUtil.episodeToFeed(episode) }
        return episodes as ArrayList<Item>
    }
}