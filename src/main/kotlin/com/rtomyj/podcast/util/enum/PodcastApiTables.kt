package com.rtomyj.podcast.util.enum

enum class PodcastApiTables(
	val tableName: String
) {
	PODCAST_INFO_TABLE("podcast_info"), PODCAST_EPISODES_TABLE("podcast_episode");

	override fun toString(): String = tableName

	enum class PodcastInfoTableColumns(
		val columnName: String
	) {
		PODCAST_ID("podcast_id"), PODCAST_TITLE("podcast_title"), PODCAST_LINK("podcast_link"), PODCAST_DESCRIPTION("podcast_description"), PODCAST_LANGUAGE("podcast_language"), PODCAST_COPYRIGHT(
			"podcast_copyright"
		),
		PODCAST_LAST_BUILD_DATE("podcast_last_build_date"), PODCAST_EMAIL("podcast_email"), PODCAST_CATEGORY("podcast_category"), PODCAST_AUTHOR("podcast_author"), IS_EXPLICIT("is_explicit"), PODCAST_IMAGE_URL(
			"podcast_image_url"
		);


		override fun toString(): String = columnName

	}

	enum class PodcastEpisodeTableColumns(
		val columnName: String
	) {

		EPISODE_TITLE("episode_title"), PODCAST_ID("podcast_id"), EPISODE_LINK("episode_link"), EPISODE_DESCRIPTION("episode_description"), EPISODE_PUBLICATION_DATE("episode_pub_date"), EPISODE_AUTHOR(
			"episode_author"
		),
		EPISODE_IMAGE("episode_image"), EPISODE_KEYWORDS("episode_keywords"), EPISODE_GUID("episode_guid"), EPISODE_LENGTH("episode_length"), EPISODE__MEDIA_TYPE("episode_media_type"), IS_EXPLICIT(
			"is_episode_explicit"
		),
		EPISODE_DURATION("episode_duration");


		override fun toString(): String = columnName
	}
}