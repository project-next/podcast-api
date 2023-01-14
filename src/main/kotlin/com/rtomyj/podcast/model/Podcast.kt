package com.rtomyj.podcast.model

import com.rtomyj.podcast.util.constant.Generic
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
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
}