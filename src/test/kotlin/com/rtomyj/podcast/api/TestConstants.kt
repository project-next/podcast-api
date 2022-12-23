package com.rtomyj.podcast.api

import org.springframework.http.MediaType
import java.nio.charset.Charset

object TestConstants {
	const val PODCAST_ENDPOINT = "/podcast"
	const val PODCAST_WITH_ID_ENDPOINT = "/podcast/{podcastId}"

	const val PODCAST_EPISODE_ENDPOINT ="/podcast/{podcastId}/episode"

	const val VALID_PODCAST_ID = "16e2094c-1bbc-4de4-a6fd-ee6b730b77a3"

	const val EMPTY_BODY = "{}"
	const val EMPTY_BODY_LENGTH = EMPTY_BODY.length

	val CONTENT_TYPE = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))
}