package com.rtomyj.podcast.exception

import com.rtomyj.podcast.util.enum.ErrorType

data class PodcastException(override val message: String, val errorType: ErrorType)
	: RuntimeException()