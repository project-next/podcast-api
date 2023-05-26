package com.rtomyj.podcast.util.constant

import com.rtomyj.podcast.util.enum.PodcastApiTables.*

object SqlQueries {
	// Update podcast deets
	val INSERT_NEW_PODCAST_QUERY =
		"INSERT INTO $PODCAST_INFO_TABLE(${PodcastInfoTableColumns.PODCAST_ID}, ${PodcastInfoTableColumns.PODCAST_TITLE}, ${PodcastInfoTableColumns.PODCAST_LINK}, ${PodcastInfoTableColumns.PODCAST_DESCRIPTION}, ${PodcastInfoTableColumns.PODCAST_LANGUAGE}, ${PodcastInfoTableColumns.PODCAST_COPYRIGHT}, ${PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE}, ${PodcastInfoTableColumns.PODCAST_EMAIL}, ${PodcastInfoTableColumns.PODCAST_CATEGORY}, ${PodcastInfoTableColumns.PODCAST_AUTHOR}, ${PodcastInfoTableColumns.IS_EXPLICIT}, ${PodcastInfoTableColumns.PODCAST_IMAGE_URL}) VALUES (:podcast_id,:podcast_title, :podcast_link, :podcast_description, :podcast_language, :podcast_copyright, NOW(), :podcast_email, :podcast_category, :podcast_author, :is_explicit, :podcast_image_url)"
	val UPDATE_PODCAST_QUERY =
		"UPDATE $PODCAST_INFO_TABLE SET ${PodcastInfoTableColumns.PODCAST_TITLE} = :podcast_title, ${PodcastInfoTableColumns.PODCAST_LINK} = :podcast_link, ${PodcastInfoTableColumns.PODCAST_DESCRIPTION} = :podcast_description, ${PodcastInfoTableColumns.PODCAST_LANGUAGE} = :podcast_language, ${PodcastInfoTableColumns.PODCAST_COPYRIGHT} = :podcast_copyright, ${PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE} = NOW(), ${PodcastInfoTableColumns.PODCAST_EMAIL} = :podcast_email, ${PodcastInfoTableColumns.PODCAST_CATEGORY} = :podcast_category, ${PodcastInfoTableColumns.PODCAST_AUTHOR} = :podcast_author, ${PodcastInfoTableColumns.IS_EXPLICIT} = :is_explicit, ${PodcastInfoTableColumns.PODCAST_IMAGE_URL} = :podcast_image_url WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcast_id"

	// Update episode deets
	val INSERT_NEW_PODCAST_EPISODE_QUERY =
		"INSERT INTO $PODCAST_EPISODES_TABLE(${PodcastInfoTableColumns.PODCAST_ID}, ${PodcastEpisodeTableColumns.EPISODE_TITLE}, ${PodcastEpisodeTableColumns.EPISODE_WEBPAGE_LINK}, ${PodcastEpisodeTableColumns.EPISODE_AUDIO_LINK}, ${PodcastEpisodeTableColumns.EPISODE_DESCRIPTION}, ${PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE}, ${PodcastEpisodeTableColumns.EPISODE_AUTHOR}, ${PodcastEpisodeTableColumns.EPISODE_IMAGE}, ${PodcastEpisodeTableColumns.EPISODE_KEYWORDS}, ${PodcastEpisodeTableColumns.EPISODE_GUID}, ${PodcastEpisodeTableColumns.EPISODE_LENGTH}, ${PodcastEpisodeTableColumns.EPISODE_MEDIA_TYPE}, ${PodcastEpisodeTableColumns.IS_EXPLICIT}, ${PodcastEpisodeTableColumns.EPISODE_DURATION}) VALUES (:podcast_id, :episode_title, :episode_webpage_link, :episode_audio_link, :episode_description, NOW(), :episode_author, :episode_image, :episode_keywords, :episode_guid, :episode_length, :episode_media_type, :is_episode_explicit, CAST (:episode_duration AS TIME))"
	val UPDATE_PODCAST_EPISODE_QUERY =
		"UPDATE $PODCAST_EPISODES_TABLE SET ${PodcastEpisodeTableColumns.EPISODE_TITLE} = :episode_title, ${PodcastEpisodeTableColumns.EPISODE_WEBPAGE_LINK} = :episode_webpage_link, ${PodcastEpisodeTableColumns.EPISODE_AUDIO_LINK} = :episode_audio_link, ${PodcastEpisodeTableColumns.EPISODE_DESCRIPTION} = :episode_description, ${PodcastEpisodeTableColumns.EPISODE_AUTHOR} = :episode_author, ${PodcastEpisodeTableColumns.EPISODE_IMAGE} = :episode_image, ${PodcastEpisodeTableColumns.EPISODE_KEYWORDS} = :episode_keywords, ${PodcastEpisodeTableColumns.EPISODE_LENGTH} = :episode_length, ${PodcastEpisodeTableColumns.EPISODE_MEDIA_TYPE} = :episode_media_type, ${PodcastEpisodeTableColumns.IS_EXPLICIT} = :is_episode_explicit, ${PodcastEpisodeTableColumns.EPISODE_DURATION} = episode_duration WHERE ${PodcastEpisodeTableColumns.EPISODE_GUID} = :episode_guid"
}