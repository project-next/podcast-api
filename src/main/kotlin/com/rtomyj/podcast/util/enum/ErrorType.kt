package com.rtomyj.podcast.util.enum

import org.springframework.http.HttpStatus

enum class ErrorType(val error: String, val httpStatus: HttpStatus) {
	G001("URL params or path variables do not conform to spec", HttpStatus.BAD_REQUEST),
	G002("Body is missing data or fields do not conform to spec", HttpStatus.UNPROCESSABLE_ENTITY)

	, DB001("Requested resource was not found", HttpStatus.NOT_FOUND);
}