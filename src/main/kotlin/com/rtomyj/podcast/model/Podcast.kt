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

@Entity(name = "podcast")
@Table(schema = "podcasting")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Podcast(
    @Id @Column(
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_build_date", nullable = false)
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

    override fun toString(): String {
        return StringBuilder("Podcast ID: $id").append("Podcast Title: $title").append("Podcast Link: $link").toString()
    }
}