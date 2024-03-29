package com.rtomyj.podcast.util

object Constants {
	const val CLIENT_IP_MDC = "reqIp"

	const val URL_REGEX = "^(http(s)://.)[-\\d\\w@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-\\d\\w@:%_+.~#?&/=]*)\$"
	const val URL_VALIDATOR_MESSAGE = "URL must be in correct format ex: https://im-awesome.com"

	const val PODCAST_URI = "/**"

	var APP_VERSION: String = Constants::class.java.getPackage().implementationVersion ?: "LOCAL"
}