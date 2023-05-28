package com.rtomyj.podcast.util.enum

import org.springframework.http.HttpStatus

enum class ErrorType(val error: String, val httpStatus: HttpStatus) {
	G001("URL params or path variables do not conform to spec", HttpStatus.BAD_REQUEST),
	G002("Body is missing data or fields do not conform to spec", HttpStatus.UNPROCESSABLE_ENTITY),
	G003("User is unauthorized", HttpStatus.UNAUTHORIZED),
	G004("User is forbidden from accessing resource", HttpStatus.FORBIDDEN),
	G005("Data integrity via user request", HttpStatus.BAD_REQUEST),

	DB001("Requested resource was not found", HttpStatus.NOT_FOUND),
	DB002("Constraint violation on DB column", HttpStatus.INTERNAL_SERVER_ERROR),
	DB003("Error updating database", HttpStatus.INTERNAL_SERVER_ERROR),
	DB004("No rows effected by update", HttpStatus.INTERNAL_SERVER_ERROR);
}