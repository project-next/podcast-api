package com.rtomyj.podcast.api.constant

import com.rtomyj.podcast.api.enum.PodcastApiTables.*
import com.rtomyj.podcast.api.enum.PodcastApiTables.PodcastEpisodeTableColumns.*
import com.rtomyj.podcast.api.enum.PodcastApiTables.PodcastInfoTableColumns.*

object SqlQueries
{

    val PODCAST_INFO_QUERY = "SELECT ${PodcastInfoTableColumns.PODCAST_ID}, $PODCAST_TITLE, $PODCAST_LINK, $PODCAST_DESCRIPTION, $PODCAST_LANGUAGE, $PODCAST_COPYRIGHT, $PODCAST_LAST_BUILD_DATE, $PODCAST_EMAIL, $PODCAST_CATEGORY, $PODCAST_AUTHOR, ${PodcastInfoTableColumns.IS_EXPLICIT}, $PODCAST_IMAGE_URL FROM $PODCAST_INFO_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"

    val PODCAST_EPISODES_QUERY = "SELECT $EPISODE_TITLE, ${PodcastEpisodeTableColumns.PODCAST_ID}, $EPISODE_LINK, $EPISODE_DESCRIPTION, $EPISODE_PUBLICATION_DATE, $EPISODE_AUTHOR, $EPISODE_IMAGE, $EPISODE_KEYWORDS, $EPISODE_GUID, $EPISODE_LENGTH, $EPISODE__MEDIA_TYPE, ${PodcastEpisodeTableColumns.IS_EXPLICIT}, $EPISODE_DURATION from $PODCAST_EPISODES_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"

}