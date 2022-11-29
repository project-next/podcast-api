package com.rtomyj.podcast.api.model

import com.rometools.modules.itunes.EntryInformationImpl
import com.rometools.modules.itunes.types.Duration
import com.rometools.rome.feed.rss.Description
import com.rometools.rome.feed.rss.Enclosure
import com.rometools.rome.feed.rss.Guid
import com.rometools.rome.feed.rss.Item
import com.rtomyj.podcast.api.constant.GenericConstants
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class PodcastEpisode(val podcastId: String) {

	lateinit var episodeTitle: String
	lateinit var episodeLink: URL
	lateinit var episodeDescription: String
	lateinit var episodePublicationDate: LocalDateTime
	lateinit var episodeAuthor: String
	lateinit var episodeImage: URL
	var episodeKeywords = arrayListOf<String>()
	var episodeGuid = Guid()
	var episodeLength = 0L
	lateinit var episodeMediaType: String
	var isEpisodeExplicit = false
	lateinit var episodeDuration: String


	fun toRssItem(): Item {
		return Item().apply {
			// Set description
			this.description = Description().apply {
				value = episodeDescription
			}

			this.enclosures = Enclosure().run {
				length = episodeLength
				type = GenericConstants.MEDIA_TYPE
				url = episodeLink.toString()
				listOf(this)
			}

			title = episodeTitle
			author = episodeAuthor
			link = episodeLink.toString()
			pubDate = Date.from(episodePublicationDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
			guid = episodeGuid

			this.modules = EntryInformationImpl().run {
				image = episodeImage
				keywords = episodeKeywords.toTypedArray()

				val durationTokens = episodeDuration.split(":")
				duration = Duration(durationTokens[0].toInt(), durationTokens[1].toInt(), durationTokens[2].toFloat())
				listOf(this)
			}
		}
	}
}