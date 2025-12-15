package com.rtomyj.podcast.controller

import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.model.Status
import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
@Validated
class StorePodcastDataController {
    @Autowired
    private lateinit var podcastService: PodcastService

    @PostMapping("/podcast")
    fun storeNewPodcast(
        @Valid
        @RequestBody
        podcast: Podcast
    ): ResponseEntity<Status> {
        podcastService.storeNewPodcast(podcast)
        return ResponseEntity(Status("Successfully stored new podcast!"), HttpStatus.CREATED)
    }

    @PostMapping("/podcast/{podcastId}/episode")
    fun storeNewPodcastEpisode(
        @PathVariable("podcastId")
        @NotBlank
        @Size(min = 36, max = 36)
        podcastId: String,
        @Valid
        @RequestBody
        podcastEpisode: PodcastEpisode
    ): ResponseEntity<Status> {
        podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)
        return ResponseEntity(Status("Successfully stored new podcast episode!"), HttpStatus.CREATED)
    }
}