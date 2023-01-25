package com.rtomyj.podcast.model

import com.rtomyj.podcast.util.constant.Generic
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.*


data class PodcastEpisode(
	@Size(min = 36, max = 36) val podcastId: String = "", val episodeId: String = UUID.randomUUID().toString()
) {
	@NotBlank
	@Size(max = 100)
	@Pattern(regexp = "[\\w\\d\$&+,:;=?@# ]+")
	lateinit var title: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var episodeWebpageLink: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	lateinit var episodeAudioLink: String

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
}