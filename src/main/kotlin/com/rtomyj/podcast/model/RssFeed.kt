package com.rtomyj.podcast.model

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.io.WireFeedOutput
import com.rtomyj.podcast.util.TransformToFeedUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.util.StringUtils
import org.springframework.web.servlet.view.AbstractView
import java.io.OutputStreamWriter

class RssFeed(private val podcast: Podcast, private val transformToFeedUtil: TransformToFeedUtil) : AbstractView() {
    init {
        contentType = MediaType.APPLICATION_RSS_XML_VALUE
    }

    override fun renderMergedOutputModel(
        model: MutableMap<String, Any>, request: HttpServletRequest, response: HttpServletResponse
    ) {
        val channel = Channel("rss_2.0")
        transformToFeedUtil.populateChannelInfo(channel, podcast)
        channel.items = podcast.episodes.map { episode -> transformToFeedUtil.episodeToFeed(episode) }

        setResponseContentType(request, response)
        if (!StringUtils.hasText(channel.encoding)) {
            channel.encoding = "UTF-8"
        }

        val out = response.outputStream
        WireFeedOutput().output(channel, OutputStreamWriter(out, channel.encoding))
        out.flush()
    }
}