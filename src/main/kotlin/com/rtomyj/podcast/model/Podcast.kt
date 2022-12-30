package com.rtomyj.podcast.model

import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.types.Category
import com.rometools.rome.feed.rss.Channel
import com.rtomyj.podcast.util.constant.Generic
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class Podcast(val id: String = UUID.randomUUID().toString()) {
	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "[\\w\\d ]+")
	lateinit var title: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var link: String

	@NotBlank
	@Size(max = 3000)
	lateinit var description: String

	@NotBlank
	@Pattern(regexp = "\\w{2}-\\w{2}")
	@Size(max = 3000)
	lateinit var language: String

	@NotBlank
	@Size(max = 40)
	lateinit var copyright: String

	lateinit var lastBuildDate: LocalDateTime

	@NotBlank
	@Email
	@Size(max = 30)
	lateinit var email: String

	@NotBlank
	@Size(max = 20)
	lateinit var category: String

	@NotBlank
	@Size(max = 30)
	lateinit var author: String

	var isExplicit: Boolean = true

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var imageUrl: String

	override fun toString(): String {
		return StringBuilder("Podcast ID: $id").append("Podcast Title: $title").append("Podcast Link: $link").toString()
	}

	fun populateChannelInfo(feed: Channel) {
		with(feed) {
			title = this@Podcast.title
			link = this@Podcast.link
			description = this@Podcast.description
			language = this@Podcast.language
			copyright = this@Podcast.copyright
			lastBuildDate = Date.from(this@Podcast.lastBuildDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
		}

		val feedInformationImpl = FeedInformationImpl().apply {
			author = this@Podcast.author
			ownerName = this@Podcast.author
			ownerEmailAddress = email
			explicit = isExplicit
			image = URL(imageUrl)
			categories = listOf(Category(category))
		}

		feed.modules = listOf(feedInformationImpl)
	}
}