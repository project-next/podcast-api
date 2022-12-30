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


class PodcastEpisode(
	@NotBlank @Size(min = 36, max = 36) val podcastId: String, val episodeGuid: String = UUID.randomUUID().toString()
) {
	@NotBlank
	@Size(max = 100)
	@Pattern(regexp = "[\\w\\d\$&+,:;=?@# ]+")
	lateinit var episodeTitle: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var episodeLink: String

	@NotBlank
	@Size(max = 3000)
	lateinit var episodeDescription: String

	lateinit var episodePublicationDate: LocalDateTime

	@NotBlank
	@Size(max = 30)
	lateinit var episodeAuthor: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var episodeImageLink: String

	var episodeKeywords = arrayListOf<String>()

	var episodeLength = 0L

	@NotBlank
	@Size(max = 15)
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
				url = episodeLink
				listOf(this)
			}

			title = episodeTitle
			author = episodeAuthor
			link = episodeLink
			pubDate = Date.from(episodePublicationDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
			guid = Guid()
			guid.value = episodeGuid

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