package com.rtomyj.podcast.util.constant

import com.rtomyj.podcast.util.enum.PodcastApiTables.PODCAST_EPISODES_TABLE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PODCAST_INFO_TABLE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_AUTHOR
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_DESCRIPTION
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_DURATION
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_GUID
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_IMAGE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_KEYWORDS
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_LENGTH
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_LINK
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_PUBLICATION_DATE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE_TITLE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastEpisodeTableColumns.EPISODE__MEDIA_TYPE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_AUTHOR
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_CATEGORY
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_COPYRIGHT
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_DESCRIPTION
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_EMAIL
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_IMAGE_URL
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_LANGUAGE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_LAST_BUILD_DATE
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_LINK
import com.rtomyj.podcast.util.enum.PodcastApiTables.PodcastInfoTableColumns.PODCAST_TITLE

object SqlQueries {
	val PODCAST_INFO_QUERY =
		"SELECT ${PodcastInfoTableColumns.PODCAST_ID}, $PODCAST_TITLE, $PODCAST_LINK, $PODCAST_DESCRIPTION, $PODCAST_LANGUAGE, $PODCAST_COPYRIGHT, $PODCAST_LAST_BUILD_DATE, $PODCAST_EMAIL, $PODCAST_CATEGORY, $PODCAST_AUTHOR, ${PodcastInfoTableColumns.IS_EXPLICIT}, $PODCAST_IMAGE_URL FROM $PODCAST_INFO_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"
	val PODCAST_EPISODES_QUERY =
		"SELECT $EPISODE_TITLE, ${PodcastEpisodeTableColumns.PODCAST_ID}, $EPISODE_LINK, $EPISODE_DESCRIPTION, $EPISODE_PUBLICATION_DATE, $EPISODE_AUTHOR, $EPISODE_IMAGE, $EPISODE_KEYWORDS, $EPISODE_GUID, $EPISODE_LENGTH, $EPISODE__MEDIA_TYPE, ${PodcastEpisodeTableColumns.IS_EXPLICIT}, $EPISODE_DURATION from $PODCAST_EPISODES_TABLE WHERE ${PodcastInfoTableColumns.PODCAST_ID} = :podcastId"
}