package com.rtomyj.podcast.api.model

import com.rometools.modules.itunes.FeedInformationImpl
import com.rometools.modules.itunes.types.Category
import com.rometools.rome.feed.rss.Channel
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class PodcastInfo(val podcastId: String = UUID.randomUUID().toString()) {
	@NotBlank(message = "Podcast title cannot be empty")
	@Length(min = 3, max = 30)
	@Pattern(regexp = "[\\w\\d ]+", message = "Podcast title can only use letters, numbers and spaces")
	lateinit var podcastTitle: String

	//	@NotBlank(message = "Podcast link cannot be empty")
//	@Length(max = 255)
//	@org.hibernate.validator.constraints.URL(message = "Podcast link must be a valid url")
	lateinit var podcastLink: URL

	@NotBlank(message = "Podcast description cannot be empty")
	@Length(min = 10, max = 1000)
	lateinit var podcastDescription: String

	@NotBlank(message = "Podcast language cannot be empty")
	@Length(min = 5, max = 5)
	@Pattern(regexp = "\\w{2}-\\w{2}", message = "Podcast language not using proper format")
	lateinit var podcastLanguage: String

	@NotBlank(message = "Podcast copyright info cannot be empty")
	@Length(min = 5, max = 40)
	lateinit var podcastCopyright: String

	lateinit var podcastLastBuildDate: LocalDateTime

	@Email
	lateinit var podcastEmail: String

	lateinit var podcastCategory: String

	lateinit var podcastAuthor: String

	var isExplicit: Boolean = true

	lateinit var podcastImageUrl: URL

	override fun toString(): String {
		return StringBuilder("Podcast ID: $podcastId").append("Podcast Title: $podcastTitle").append("Podcast Link: $podcastLink").toString()
	}

	fun populateChannelInfo(feed: Channel) {
		with(feed) {
			title = podcastTitle
			link = podcastLink.toString()
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
			image = podcastImageUrl
			categories = Arrays.asList(Category(podcastCategory))
		}

		feed.modules = listOf(feedInformationImpl)
	}
}