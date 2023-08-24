package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.PodcastEpisode
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface PodcastEpisodePagingAndSortingRepository: PagingAndSortingRepository<PodcastEpisode, String>, CrudRepository<PodcastEpisode, String> {
    fun findAllByPodcastId(podcastId: String, sort: Sort): MutableList<PodcastEpisode>
}