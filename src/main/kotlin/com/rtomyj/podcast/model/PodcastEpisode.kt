package com.rtomyj.podcast.model

import com.rometools.modules.itunes.EntryInformationImpl
import com.rometools.modules.itunes.types.Duration
import com.rometools.rome.feed.rss.Description
import com.rometools.rome.feed.rss.Enclosure
import com.rometools.rome.feed.rss.Guid
import com.rometools.rome.feed.rss.Item
import com.rtomyj.podcast.util.constant.Generic
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class PodcastEpisode(val podcastId: String = Guid().toString()) {
	@NotBlank
	@Size(max = 100)
	@Pattern(regexp = "[\\w\\d\$&+,:;=?@# ]+")
	lateinit var episodeTitle: String

	@NotBlank
	@Size(max = 255)
	@org.hibernate.validator.constraints.URL
	lateinit var episodeLink: String

	@NotBlank
	@Size(min = 10, max = 1000)
	lateinit var episodeDescription: String

	lateinit var episodePublicationDate: LocalDateTime

	@NotBlank
	@Size(min = 3, max = 30)
	lateinit var episodeAuthor: String

	@NotBlank
	@Size(max = 255)
	@org.hibernate.validator.constraints.URL
	lateinit var episodeImageLink: String

	var episodeKeywords = arrayListOf<String>()

	var episodeGuid = Guid()

	var episodeLength = 0L

	@NotBlank
	lateinit var episodeMediaType: String

	var isEpisodeExplicit = false

	@NotBlank
	@Pattern(regexp = "(\\d{2}:?){3}")
	lateinit var episodeDuration: String

	fun toRssItem(): Item {
		return Item().apply {
			// Set description
			this.description = Description().apply {
				value = episodeDescription
			}

			this.enclosures = Enclosure().run {
				length = episodeLength
				type = Generic.MEDIA_TYPE
				url = episodeLink.toString()
				listOf(this)
			}

			title = episodeTitle
			author = episodeAuthor
			link = episodeLink.toString()
			pubDate = Date.from(episodePublicationDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
			guid = episodeGuid

			this.modules = EntryInformationImpl().run {
				image = URL(episodeImageLink)
				keywords = episodeKeywords.toTypedArray()

				val durationTokens = episodeDuration.split(":")
				duration = Duration(durationTokens[0].toInt(), durationTokens[1].toInt(), durationTokens[2].toFloat())
				listOf(this)
			}
		}
	}
}