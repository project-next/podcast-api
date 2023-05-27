package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.PodcastEpisode
import org.springframework.data.repository.CrudRepository

interface PodcastEpisodeCrudRepository: CrudRepository<PodcastEpisode, String>