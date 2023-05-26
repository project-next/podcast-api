package com.rtomyj.podcast.util

import com.rometools.modules.itunes.EntryInformationImpl
import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.types.Category
import com.rometools.modules.itunes.types.Duration
import com.rometools.rome.feed.rss.*
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TransformToFeed {
	companion object {
		@JvmStatic
		fun localDateToDate(localDateTime: LocalDateTime): Date = Date.from(localDateTime.atZone(ZoneId.of("GMT")).toInstant())

		@JvmStatic
		fun populateChannelInfo(feed: Channel, podcast: Podcast) {
			with(feed) {
				title = podcast.title
				link = podcast.link
				description = podcast.description
				language = podcast.language
				copyright = podcast.copyright
				lastBuildDate = localDateToDate(podcast.lastBuildDate)
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

		@JvmStatic
		fun episodeToFeed(episode: PodcastEpisode): Item {
			return Item().apply {
				// Set description
				this.description = Description().apply {
					value = episode.description
				}

				this.enclosures = Enclosure().run {
					length = episode.length
					type = episode.mediaType
					url = episode.episodeAudioLink
					listOf(this)
				}

				title = episode.title
				author = episode.author
				link = episode.episodeWebpageLink
				pubDate = localDateToDate(episode.publicationDate)
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