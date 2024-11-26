package com.rtomyj.podcast.controller

import com.rtomyj.podcast.model.Status
import com.rtomyj.podcast.service.PodcastService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json; charset=UTF-8"])
@Validated
class DeletePodcastDataController {
    @Autowired
    private lateinit var podcastService: PodcastService

    @DeleteMapping("/podcast/{podcastId}")
    fun deletePodcast(
        @PathVariable("podcastId") @NotBlank @Size(min = 36, max = 36) podcastId: String
    ): ResponseEntity<Status> {
        podcastService.deletePodcast(podcastId)
        return ResponseEntity.ok(Status("Successfully deleted podcast!"))
    }

    @DeleteMapping("/podcast/episode/{podcastEpisodeId}")
    fun deletePodcastEpisode(
        @PathVariable("podcastEpisodeId") @NotBlank @Size(min = 36, max = 36) podcastEpisodeId: String
    ): ResponseEntity<Status> {
        podcastService.deletePodcastEpisode(podcastEpisodeId)
        return ResponseEntity.ok(Status("Successfully deleted podcast episode!"))
    }
}