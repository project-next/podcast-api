package com.rtomyj.podcast.api.model

import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.rome.feed.rss.Channel
import java.lang.StringBuilder
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import com.rometools.modules.itunes.types.Category
import com.rometools.rome.feed.module.Module
import java.time.ZoneId

class PodcastInfo(val podcastId: String)
{

    lateinit var podcastTitle: String
    lateinit var podcastLink: URL
    lateinit var podcastDescription: String
    lateinit var podcastLanguage: String
    lateinit var podcastCopyright: String
    lateinit var podcastLastBuildDate: LocalDateTime

    lateinit var podcastEmail: String
    lateinit var podcastCategory: String
    lateinit var podcastAuthor: String

    var isExplicit: Boolean = true
    lateinit var podcastImageUrl: URL

    override fun toString(): String
    {
        return StringBuilder("Podcast ID: $podcastId")
                .append("Podcast Title: $podcastTitle")
                .append("Podcast Link: $podcastLink")
                .toString()
    }


    fun populateChannelInfo(feed: Channel)
    {

        with(feed)
        {
            title = podcastTitle
            link = podcastLink.toString()
            description = podcastDescription
            language = podcastLanguage
            copyright = podcastCopyright
            lastBuildDate = Date.from(podcastLastBuildDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        val feedInformationImpl = FeedInformationImpl().apply {
            author = podcastAuthor
            ownerName = podcastAuthor
            ownerEmailAddress = podcastEmail
            explicit = isExplicit
            image = podcastImageUrl
            categories = Arrays.asList(Category(podcastCategory))
        }

        feed.modules = listOf(feedInformationImpl)

    }

}