package com.rtomyj.podcast.exception

import com.rtomyj.podcast.util.Constants
import com.rtomyj.podcast.util.enum.ErrorType
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class PodcastExceptionAdvice {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@ResponseBody
	@ExceptionHandler(PodcastException::class)
	fun onPodcastException(exception: PodcastException): ResponseEntity<PodcastError> {
		log.error(Constants.EXCEPTION_PROVIDER_LOG, exception.message, exception.errorType, exception.errorType.httpStatus)

		return ResponseEntity(
			PodcastError(exception.errorType.error, exception.errorType.name), exception.errorType.httpStatus
		)
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun onValidationFail(exception: MethodArgumentNotValidException): PodcastError {
		log.error("Request body did not conform to spec. Constraints violated: {}", exception.allErrors.toString())

		val errorMap = linkedMapOf<String, String>()

		exception.bindingResult.fieldErrors.forEach {
			errorMap[it.field] = it.defaultMessage as String
		}

		return PodcastError("${ErrorType.G002.error}. Errors: $errorMap", ErrorType.G002.name)
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException::class)
	fun onValidationFail(exception: ConstraintViolationException): PodcastError {
		log.error("Request url did not conform to spec. Constraints violated: {}", exception.toString())

		return PodcastError(ErrorType.G001.error, ErrorType.G001.name)
	}
}