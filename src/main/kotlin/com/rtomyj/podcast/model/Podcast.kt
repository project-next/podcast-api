package com.rtomyj.podcast.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
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

@Entity(name = "podcast")
@Table(schema = "podcasting")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonPropertyOrder(value = ["id"], alphabetic = true)
data class Podcast(
    @get:JsonProperty
    @set:JsonIgnore
    @Id
    @Column(
        name = "id",
        columnDefinition = "bpchar",
        updatable = false,
        nullable = false
    ) var id: String = UUID.randomUUID().toString()
) {
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "[\\w ]+")
    @Column(name = "title")
    lateinit var title: String

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
    @Column(name = "link")
    lateinit var link: String

    @NotBlank
    @Size(max = 3000)
    @Column(name = "description")
    lateinit var description: String

    @NotBlank
    @Pattern(regexp = "\\w{2}-\\w{2}")
    @Size(max = 5)
    @Column(name = "language")
    lateinit var language: String

    @NotBlank
    @Size(max = 40)
    @Column(name = "copyright")
    lateinit var copyright: String

    @Column(name = "last_build_date", nullable = false, updatable = false)
    @CreationTimestamp
    lateinit var lastBuildDate: LocalDateTime

    @NotBlank
    @Email
    @Size(max = 30)
    @Column(name = "email")
    lateinit var email: String

    @NotBlank
    @Size(max = 20)
    @Column(name = "category")
    lateinit var category: String

    @NotBlank
    @Size(max = 30)
    @Column(name = "author")
    lateinit var author: String

    @JsonProperty("isExplicit")
    var isExplicit: Boolean = true

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = Constants.URL_REGEX, message = Constants.URL_VALIDATOR_MESSAGE)
    @Column(name = "image_url")
    lateinit var imageUrl: String

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    @JoinColumn(name = "podcast_id", insertable = false, updatable = false, nullable = false)
    @OrderBy("publicationDate ASC")
    var episodes: List<PodcastEpisode> = emptyList()

    // L2 cache will not have lastBuildDate value as it is generated when row is inserted
    // Meaning lastBuildDate will always be null when retrieving from L2 unless we specifically set it before record is added to L2
    @PrePersist
    fun onPrePersist() {
        lastBuildDate = LocalDateTime.now()
    }

    override fun toString(): String {
        return StringBuilder("Podcast ID: $id").append("Podcast Title: $title").append("Podcast Link: $link").toString()
    }
}