package com.rtomyj.podcast.api.model

data class PodcastData(val podcast: Podcast, val podcastEpisodes: MutableList<PodcastEpisode>)