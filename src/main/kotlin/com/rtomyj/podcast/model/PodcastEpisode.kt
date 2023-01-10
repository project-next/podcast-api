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
	@Size(min = 36, max = 36) val podcastId: String = "", val episodeId: String = UUID.randomUUID().toString()
) {
	@NotBlank
	@Size(max = 100)
	@Pattern(regexp = "[\\w\\d\$&+,:;=?@# ]+")
	lateinit var title: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var link: String

	@NotBlank
	@Size(max = 3000)
	lateinit var description: String

	lateinit var publicationDate: LocalDateTime

	@NotBlank
	@Size(max = 30)
	lateinit var author: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var imageLink: String

	var keywords = arrayListOf<String>()

	var length = 0L

	@NotBlank
	@Size(max = 15)
	lateinit var mediaType: String

	var isExplicit = false

	@NotBlank
	@Pattern(regexp = "(\\d{2}:?){3}")
	lateinit var duration: String

	fun toRssItem(): Item {
		return Item().apply {
			// Set description
			this.description = Description().apply {
				value = this@PodcastEpisode.description
			}

			this.enclosures = Enclosure().run {
				length = this@PodcastEpisode.length
				type = Generic.MEDIA_TYPE
				url = this@PodcastEpisode.link
				listOf(this)
			}

			title = this@PodcastEpisode.title
			author = this@PodcastEpisode.author
			link = this@PodcastEpisode.link
			pubDate = Date.from(publicationDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
			guid = Guid()
			guid.value = episodeId

			this.modules = EntryInformationImpl().run {
				image = URL(imageLink)
				keywords = this@PodcastEpisode.keywords.toTypedArray()

				val durationTokens = this@PodcastEpisode.duration.split(":")
				duration = Duration(durationTokens[0].toInt(), durationTokens[1].toInt(), durationTokens[2].toFloat())
				listOf(this)
			}
		}
	}
}