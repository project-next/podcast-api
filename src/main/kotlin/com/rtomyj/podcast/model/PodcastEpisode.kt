package com.rtomyj.podcast.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.rtomyj.podcast.util.Constants
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity(name = "podcast_episode")
@Table(schema = "podcasting")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class PodcastEpisode(
    @param:Size(min = 36, max = 36)
    @Column(name = "podcast_id", columnDefinition = "bpchar")
    var podcastId: String = "",
    @Id
    @Column(name = "episode_id", columnDefinition = "bpchar")
    val episodeId: String = UUID.randomUUID().toString()
) {
    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "[\\w\$&+,:?.!@#\\-• ]+")
    @Column(name = "title")
    lateinit var title: String

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
    @Column(name = "webpage_link")
    lateinit var webpageLink: String

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
    @Column(name = "audio_link")
    lateinit var audioLink: String

    @NotBlank
    @Size(max = 3000)
    @Column(name = "description")
    lateinit var description: String

    @Column(name = "pub_date", nullable = false)
    @CreationTimestamp
    lateinit var publicationDate: LocalDateTime

    @NotBlank
    @Size(max = 30)
    @Column(name = "author")
    lateinit var author: String

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
    @Column(name = "image")
    lateinit var imageLink: String

    @Column(
        name = "keywords", columnDefinition = "text[]"
    )
    var keywords = arrayOf<String>()

    @Column(name = "length")
    var length = 0L

    @NotBlank
    @Size(max = 15)
    @Column(name = "media_type")
    lateinit var mediaType: String

    @Column(name = "is_explicit")
    @JsonProperty("isExplicit")
    var isExplicit = false

    @NotBlank
    @Pattern(regexp = "(\\d{2}:?){3}")
    @Column(name = "duration")
    lateinit var duration: String

    @Min(1)
    @Max(100)
    @Column(name = "season", columnDefinition = "int2")
    var season = 0

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