package com.rtomyj.podcast.api.model

data class PodcastData(val podcastInfo: PodcastInfo, val podcastEpisodes: MutableList<PodcastEpisode>)