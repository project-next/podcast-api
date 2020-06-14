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

class PodcastInfo
{

    var podcastId: Int = 0
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


    fun populateChanneelInfo(feed: Channel)
    {
        feed.title = this.podcastTitle
        feed.link = this.podcastLink.toString()
        feed.description = this.podcastDescription
        feed.language = this.podcastLanguage
        feed.copyright = this.podcastCopyright
        feed.lastBuildDate = Date.from(podcastLastBuildDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())


        val feedInformationImpl = FeedInformationImpl()

        feedInformationImpl.author = podcastAuthor
        feedInformationImpl.ownerName = podcastAuthor
        feedInformationImpl.ownerEmailAddress = podcastEmail
        feedInformationImpl.explicit = isExplicit
        feedInformationImpl.image = podcastImageUrl
        feedInformationImpl.categories = Arrays.asList(Category(podcastCategory))

        feed.modules = listOf(feedInformationImpl)
    }

}