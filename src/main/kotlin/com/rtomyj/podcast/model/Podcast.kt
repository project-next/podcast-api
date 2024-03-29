package com.rtomyj.podcast.model

import com.rtomyj.podcast.util.Constants
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity(name = "podcast_info")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Podcast(
	@Id @Column(name = "podcast_id", columnDefinition="bpchar", updatable = false, nullable = false) var id: String = UUID.randomUUID().toString()
) {
	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "[\\w\\d ]+")
	@Column(name = "podcast_title")
	lateinit var title: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
	@Column(name = "podcast_link")
	lateinit var link: String

	@NotBlank
	@Size(max = 3000)
	@Column(name = "podcast_description")
	lateinit var description: String

	@NotBlank
	@Pattern(regexp = "\\w{2}-\\w{2}")
	@Size(max = 5)
	@Column(name = "podcast_language")
	lateinit var language: String

	@NotBlank
	@Size(max = 40)
	@Column(name = "podcast_copyright")
	lateinit var copyright: String

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "podcast_last_build_date", nullable = false)
	@CreationTimestamp
	lateinit var lastBuildDate: LocalDateTime

	@NotBlank
	@Email
	@Size(max = 30)
	@Column(name = "podcast_email")
	lateinit var email: String

	@NotBlank
	@Size(max = 20)
	@Column(name = "podcast_category")
	lateinit var category: String

	@NotBlank
	@Size(max = 30)
	@Column(name = "podcast_author")
	lateinit var author: String

	var isExplicit: Boolean = true

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
	@Column(name = "podcast_image_url")
	lateinit var imageUrl: String

	override fun toString(): String {
		return StringBuilder("Podcast ID: $id").append("Podcast Title: $title").append("Podcast Link: $link").toString()
	}
}