package com.rtomyj.podcast.util.constant

object Generic {
	const val MEDIA_TYPE = "audio/x-m4a"
	const val CLIENT_IP_MDC = "reqIp"
	const val EXCEPTION_PROVIDER_LOG = "Exception occurred w/ message: {}, with ErrorType: {}, responding with: {}"

	const val URL_REGEX = "^(http(s)://.)[-\\d\\w@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-\\d\\w@:%_+.~#?&/=]*)\$"
	const val URL_VALIDATOR_MESSAGE = "URL must be in correct format ex: https://im-awesome.com"
}