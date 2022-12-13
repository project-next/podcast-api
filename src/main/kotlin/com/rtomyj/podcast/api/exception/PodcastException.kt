package com.rtomyj.podcast.api.exception

import com.rtomyj.podcast.api.util.enum.ErrorType

data class PodcastException(override val message: String, val errorType: ErrorType)
	: RuntimeException()