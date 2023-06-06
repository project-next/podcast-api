package com.rtomyj.podcast.util

import org.springframework.http.MediaType
import java.nio.charset.Charset

object TestConstants {
	const val PODCAST_ENDPOINT = "/podcast"
	const val PODCAST_WITH_ID_ENDPOINT = "/podcast/{podcastId}"
	const val PODCAST_DATA_AS_JSON_ENDPOINT = "/podcast/json/{podcastId}"
	const val PODCAST_DATA_AS_FEED_ENDPOINT = "/podcast/feed/{podcastId}"

	const val PODCAST_EPISODE_ENDPOINT ="/podcast/{podcastId}/episode"

	const val PODCAST_ID_FROM_SQL_QUERY = "41c4e54d-bee9-43c9-b34b-d4eb87c1a377"

	const val PODCAST_ID_FROM_MOCK_RES_1 = "1712103d-14ee-4679-8e9d-d6d277257b8b"

	const val EMPTY_BODY = "{}"

	val CONTENT_TYPE = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))
}