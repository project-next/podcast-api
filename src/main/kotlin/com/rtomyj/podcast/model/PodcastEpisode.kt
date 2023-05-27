package com.rtomyj.podcast.model

import com.rtomyj.podcast.dao.KeywordsConverter
import com.rtomyj.podcast.util.constant.Generic
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate
import java.time.LocalDateTime
import java.util.*

@Entity(name = "podcast_episode")
data class PodcastEpisode(
	@Size(min = 36, max = 36) @Column(name = "podcast_id", columnDefinition="bpchar") val podcastId: String = "",
	@Id @Column(name = "episode_guid", columnDefinition="bpchar") val episodeId: String = UUID.randomUUID().toString()
) {
	@NotBlank
	@Size(max = 100)
	@Pattern(regexp = "[\\w\\d\$&+,:;=?@# ]+")
	@Column(name = "episode_title")
	lateinit var title: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	@Column(name = "episode_webpage_link")
	lateinit var episodeWebpageLink: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	@Column(name = "episode_audio_link")
	lateinit var episodeAudioLink: String

	@NotBlank
	@Size(max = 3000)
	@Column(name = "episode_description")
	lateinit var description: String

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "episode_pub_date")
	lateinit var publicationDate: LocalDateTime

	@NotBlank
	@Size(max = 30)
	@Column(name = "episode_author")
	lateinit var author: String

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = Generic.URL_REGEX, message = Generic.URL_VALIDATOR_MESSAGE)
	@Column(name = "episode_image")
	lateinit var imageLink: String

	@Column(name = "episode_keywords")
	@Convert(converter = KeywordsConverter::class)
	var keywords = arrayListOf<String>()

	@Column(name = "episode_length")
	var length = 0L

	@NotBlank
	@Size(max = 15)
	@Column(name = "episode_media_type")
	lateinit var mediaType: String

	@Column(name = "is_episode_explicit")
	var isExplicit = false

	@NotBlank
	@Pattern(regexp = "(\\d{2}:?){3}")
	@Column(name = "episode_duration")
	lateinit var duration: String

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as PodcastEpisode

		return episodeId == other.episodeId
	}

	override fun hashCode(): Int = javaClass.hashCode()

	@Override
	override fun toString(): String {
		return this::class.simpleName + "(episodeId = $episodeId , podcastId = $podcastId , title = $title )"
	}
}