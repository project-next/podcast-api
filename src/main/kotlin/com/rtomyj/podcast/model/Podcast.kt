package com.rtomyj.podcast.model

import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.types.Category
import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Guid
import com.rtomyj.podcast.util.constant.Generic
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class Podcast(val podcastId: String = Guid().toString()) {
	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "[\\w\\d ]+")
	lateinit var podcastTitle: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var podcastLink: String

	@NotBlank
	@Size(max = 3000)
	lateinit var podcastDescription: String

	@NotBlank
	@Pattern(regexp = "\\w{2}-\\w{2}")
	@Size(max = 3000)
	lateinit var podcastLanguage: String

	@NotBlank
	@Size(max = 40)
	lateinit var podcastCopyright: String

	lateinit var podcastLastBuildDate: LocalDateTime

	@NotBlank
	@Email
	@Size(max = 30)
	lateinit var podcastEmail: String

	@NotBlank
	@Size(max = 20)
	lateinit var podcastCategory: String

	@NotBlank
	@Size(max = 30)
	lateinit var podcastAuthor: String

	var isExplicit: Boolean = true

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var podcastImageUrl: String

	override fun toString(): String {
		return StringBuilder("Podcast ID: $podcastId").append("Podcast Title: $podcastTitle").append("Podcast Link: $podcastLink").toString()
	}

	fun populateChannelInfo(feed: Channel) {
		with(feed) {
			title = podcastTitle
			link = podcastLink
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
			image = URL(podcastImageUrl)
			categories = listOf(Category(podcastCategory))
		}

		feed.modules = listOf(feedInformationImpl)
	}
}