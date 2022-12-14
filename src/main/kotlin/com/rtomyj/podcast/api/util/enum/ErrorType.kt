package com.rtomyj.podcast.api.util.enum

import org.springframework.http.HttpStatus

enum class ErrorType(val error: String, val httpStatus: HttpStatus) {
	G001("URL params or path variables do not conform to spec", HttpStatus.BAD_REQUEST),
	G002("Body is missing data or fields do not conform to spec", HttpStatus.UNPROCESSABLE_ENTITY)

	, DB001("Requested resource was not found", HttpStatus.NOT_FOUND)
	, DB002("Error occurred interfacing with resource(s)", HttpStatus.INTERNAL_SERVER_ERROR);
}