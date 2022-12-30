package com.rtomyj.podcast.util.constant

import com.rtomyj.podcast.util.enum.PodcastApiTables.PODCAST_EPISODES_TABLE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PODCAST_INFO_TABLE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns

object SqlQueries {
	val PODCAST_INFO_QUERY =
		"SELECT ${PodcastInfoTableColumns.PODCAST_ID}, ${PodcastInfoTableColumns.PODCAST_TITLE}, ${PodcastInfoTableColumns.PODCAST_LINK}, ${PodcastInfoTableColumns.PODCAST_DESCRIPTION}, ${PodcastInfoTableColumns.PODCAST_LANGUAGE}, ${PodcastInfoTableColumns.PODCAST_COPYRIGHT}, ${PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE}, ${PodcastInfoTableColumns.PODCAST_EMAIL}, ${PodcastInfoTableColumns.PODCAST_CATEGORY}, ${PodcastInfoTableColumns.PODCAST_AUTHOR}, ${PodcastInfoTableColumns.IS_EXPLICIT}, ${PodcastInfoTableColumns.PODCAST_IMAGE_URL} FROM $PODCAST_INFO_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"
	val PODCAST_EPISODES_QUERY =
		"SELECT ${PodcastEpisodeTableColumns.EPISODE_TITLE}, ${PodcastEpisodeTableColumns.PODCAST_ID}, ${PodcastEpisodeTableColumns.EPISODE_LINK}, ${PodcastEpisodeTableColumns.EPISODE_DESCRIPTION}, ${PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE}, ${PodcastEpisodeTableColumns.EPISODE_AUTHOR}, ${PodcastEpisodeTableColumns.EPISODE_IMAGE}, ${PodcastEpisodeTableColumns.EPISODE_KEYWORDS}, ${PodcastEpisodeTableColumns.EPISODE_GUID}, ${PodcastEpisodeTableColumns.EPISODE_LENGTH}, ${PodcastEpisodeTableColumns.EPISODE_MEDIA_TYPE}, ${PodcastEpisodeTableColumns.IS_EXPLICIT}, ${PodcastEpisodeTableColumns.EPISODE_DURATION} from $PODCAST_EPISODES_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"

	// Update podcast deets
	val INSERT_NEW_PODCAST_QUERY =
		"INSERT INTO $PODCAST_INFO_TABLE(${PodcastInfoTableColumns.PODCAST_ID}, ${PodcastInfoTableColumns.PODCAST_TITLE}, ${PodcastInfoTableColumns.PODCAST_LINK}, ${PodcastInfoTableColumns.PODCAST_DESCRIPTION}, ${PodcastInfoTableColumns.PODCAST_LANGUAGE}, ${PodcastInfoTableColumns.PODCAST_COPYRIGHT}, ${PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE}, ${PodcastInfoTableColumns.PODCAST_EMAIL}, ${PodcastInfoTableColumns.PODCAST_CATEGORY}, ${PodcastInfoTableColumns.PODCAST_AUTHOR}, ${PodcastInfoTableColumns.IS_EXPLICIT}, ${PodcastInfoTableColumns.PODCAST_IMAGE_URL}) VALUES (:podcast_id,:podcast_title, :podcast_link, :podcast_description, :podcast_language, :podcast_copyright, NOW(), :podcast_email, :podcast_category, :podcast_author, :is_explicit, :podcast_image_url)"
	val UPDATE_PODCAST_QUERY =
		"UPDATE $PODCAST_INFO_TABLE SET ${PodcastInfoTableColumns.PODCAST_TITLE} = :podcast_title, ${PodcastInfoTableColumns.PODCAST_LINK} = :podcast_link, ${PodcastInfoTableColumns.PODCAST_DESCRIPTION} = :podcast_description, ${PodcastInfoTableColumns.PODCAST_LANGUAGE} = :podcast_language, ${PodcastInfoTableColumns.PODCAST_COPYRIGHT} = :podcast_copyright, ${PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE} = NOW(), ${PodcastInfoTableColumns.PODCAST_EMAIL} = :podcast_email, ${PodcastInfoTableColumns.PODCAST_CATEGORY} = :podcast_category, ${PodcastInfoTableColumns.PODCAST_AUTHOR} = :podcast_author, ${PodcastInfoTableColumns.IS_EXPLICIT} = :is_explicit, ${PodcastInfoTableColumns.PODCAST_IMAGE_URL} = :podcast_image_url WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcast_id"

	// Update episode deets
	val INSERT_NEW_PODCAST_EPISODE_QUERY =
		"INSERT INTO $PODCAST_EPISODES_TABLE(${PodcastInfoTableColumns.PODCAST_ID}, ${PodcastEpisodeTableColumns.EPISODE_TITLE}, ${PodcastEpisodeTableColumns.EPISODE_LINK}, ${PodcastEpisodeTableColumns.EPISODE_DESCRIPTION}, ${PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE}, ${PodcastEpisodeTableColumns.EPISODE_AUTHOR}, ${PodcastEpisodeTableColumns.EPISODE_IMAGE}, ${PodcastEpisodeTableColumns.EPISODE_KEYWORDS}, ${PodcastEpisodeTableColumns.EPISODE_GUID}, ${PodcastEpisodeTableColumns.EPISODE_LENGTH}, ${PodcastEpisodeTableColumns.EPISODE_MEDIA_TYPE}, ${PodcastEpisodeTableColumns.IS_EXPLICIT}, ${PodcastEpisodeTableColumns.EPISODE_DURATION}) VALUES (:podcast_id, :episode_title, :episode_link, :episode_description, NOW(), :episode_author, :episode_image, :episode_keywords, :episode_guid, :episode_length, :episode_media_type, :is_episode_explicit, CAST (:episode_duration AS TIME))"
}