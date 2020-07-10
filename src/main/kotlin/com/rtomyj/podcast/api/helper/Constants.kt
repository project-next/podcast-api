package com.rtomyj.podcast.api.helper

object Constants
{

    const val MEDIA_TYPE = "audio/x-m4a"

    const val PODCAST_INFO_QUERY = "SELECT * FROM podcast_info WHERE podcast_id = :podcastId"
    const val PODCAST_EPISODES_QUERY = "SELECT episode_title, podcast_id, episode_link, episode_description, episode_pub_date, episode_author, episode_image, episode_keywords, episode_guid, episode_length, episode_media_type, is_episode_explicit from podcast_episode WHERE podcast_id = :podcastId"

}