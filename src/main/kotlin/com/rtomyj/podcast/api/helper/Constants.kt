package com.rtomyj.podcast.api.helper

object Constants {
    const val MEDIA_TYPE = "audio/mpeg"


    const val PODCAST_INFO_QUERY = "SELECT * FROM podcast_info WHERE podcast_id = 1"
    const val PODCAST_EPISODES_QUERY = "select episode_id, episode_title, episode_link, episode_description, episode_pub_date, episode_author, episode_image, episode_summary, episode_keywords, episode_guid, episode_length from podcast_episode where podcast_id = :podcastId"
}