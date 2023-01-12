package com.rtomyj.podcast.util

import com.rometools.modules.itunes.EntryInformationImpl
import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.types.Category
import com.rometools.modules.itunes.types.Duration
import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Description
import com.rometools.rome.feed.rss.Enclosure
import com.rometools.rome.feed.rss.Guid
import com.rometools.rome.feed.rss.Item
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.constant.Generic
import java.net.URL
import java.time.ZoneId
import java.util.*

class TransformToFeed {
	companion object {
		@JvmStatic
		fun populateChannelInfo(feed: Channel, podcast: Podcast) {
			with(feed) {
				title = podcast.title
				link = podcast.link
				description = podcast.description
				language = podcast.language
				copyright = podcast.copyright
				lastBuildDate = Date.from(podcast.lastBuildDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
			}

			val feedInformationImpl = FeedInformationImpl().apply {
				author = podcast.author
				ownerName = podcast.author
				ownerEmailAddress = podcast.email
				explicit = podcast.isExplicit
				image = URL(podcast.imageUrl)
				categories = listOf(Category(podcast.category))
			}

			feed.modules = listOf(feedInformationImpl)
		}


		fun episodeToFeed(episode: PodcastEpisode): Item {
			return Item().apply {
				// Set description
				this.description = Description().apply {
					value = episode.description
				}

				this.enclosures = Enclosure().run {
					length = episode.length
					type = Generic.MEDIA_TYPE
					url = episode.link
					listOf(this)
				}

				title = episode.title
				author = episode.author
				link = episode.link
				pubDate = Date.from(episode.publicationDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
				guid = Guid()
				guid.value = episode.episodeId

				this.modules = EntryInformationImpl().run {
					image = URL(episode.imageLink)
					keywords = episode.keywords.toTypedArray()

					val durationTokens = episode.duration.split(":")
					duration = Duration(durationTokens[0].toInt(), durationTokens[1].toInt(), durationTokens[2].toFloat())
					listOf(this)
				}
			}
		}
	}
}