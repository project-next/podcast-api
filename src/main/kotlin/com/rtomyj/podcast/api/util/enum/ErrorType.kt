package com.rtomyj.podcast.api.util.enum

import org.springframework.http.HttpStatus

enum class ErrorType(val error: String, val httpStatus: HttpStatus) {
	G001("URL or data in body doesn't use proper syntax", HttpStatus.NOT_FOUND)

	, DB001("Requested resource was not found", HttpStatus.NOT_FOUND)
	, DB002("Error occurred interfacing with resource(s)", HttpStatus.INTERNAL_SERVER_ERROR);
}