package com.rtomyj.podcast.api.exception

import com.rtomyj.podcast.api.util.enum.ErrorType
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionProvider : ResponseEntityExceptionHandler() {
	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException::class)
	fun onValidationFail(exception: ConstraintViolationException): PodcastError {
		log.error("Request did not conform to spec. Constraints violated: {}", exception.toString())

		return PodcastError(ErrorType.G001.error, ErrorType.G001)
	}
}