package com.rtomyj.three_six_oh.component

import com.rometools.modules.itunes.EntryInformationImpl
import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.io.ITunesGenerator
import com.rometools.modules.itunes.types.Category
import com.rometools.rome.feed.module.Module
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractRssFeedView
import javax.servlet.http.HttpServletRequest
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description
import com.rometools.rome.feed.rss.Enclosure
import com.rometools.rome.feed.rss.Item
import com.rtomyj.three_six_oh.dao.Dao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.util.UriBuilder
import java.net.URL
import java.time.Instant
import java.util.*
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
//        feed.title = "Three-Six-Oh Podcast Feed"
//        feed.description = "RSS feed with info about all the episodes on the Three-Six-Oh podcast."
//        feed.link = "https://thesupremekingscastle.com"
//        feed.language = "en-us"
//        feed.copyright = "Three-Six-Oh &#169;2020"
//        feed.lastBuildDate = Date.from(Instant.parse("2017-12-19T00:00:00Z"))
//
//
//        val feedInformationImpl: FeedInformationImpl = FeedInformationImpl()
//        feedInformationImpl.author = "Javi Gomez"
//        feedInformationImpl.summary = "Three friends talking shit."
//
//        feedInformationImpl.ownerName = "Three-Six-Oh"
//        feedInformationImpl.ownerEmailAddress = "rtomyj@gmail.com"
//
//        feedInformationImpl.explicit = true
//
//        feedInformationImpl.image = URL("http://files.idrsolutions.com/Java_PDF_Podcasts/idrlogo.png")
//
//        feedInformationImpl.categories = Arrays.asList(Category("Comedy"))


        //feed.modules = Arrays.asList(feedInformationImpl) as List<Module>?
    }


    override fun buildFeedItems(
            model: MutableMap<String, Any>
            , request: HttpServletRequest
            , response: HttpServletResponse)
            : MutableList<Item>
    {
        val item = Item()
        val description = Description()
        val enclosure = Enclosure()
        val entryInformationImpl = EntryInformationImpl()


        description.type = "type"
        description.value = "First podcast episode mofos"

        enclosure.length = 11779397
        enclosure.type = "audio/mpeg"
        enclosure.url = "http://files.idrsolutions.com/Java_PDF_Podcasts/Interview_4_Advice_and_XFA.mp3"

        item.title = "Test1"
        item.author = "rtomyj@gmail.com"
        item.link = "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"
        item.description = description
        item.pubDate = Date.from(Instant.parse("2017-12-19T00:00:00Z"))
        item.enclosures = Arrays.asList(enclosure)

        entryInformationImpl.summary = "The first episode"
        entryInformationImpl.image = URL("http://files.idrsolutions.com/Java_PDF_Podcasts/idrlogo.png")

        item.modules = Arrays.asList(entryInformationImpl) as List<Module>?

        return Arrays.asList(item)
    }
}