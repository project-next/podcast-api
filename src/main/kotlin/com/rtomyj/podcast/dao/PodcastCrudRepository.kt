package com.rtomyj.podcast.dao

import com.rtomyj.podcast.model.Podcast
import org.springframework.data.repository.CrudRepository

interface PodcastCrudRepository: CrudRepository<Podcast, String>