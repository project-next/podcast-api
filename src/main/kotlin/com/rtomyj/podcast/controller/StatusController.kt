package com.rtomyj.podcast.controller

import com.rtomyj.podcast.model.StatusResponse
import com.rtomyj.podcast.util.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path = ["/status"], produces = ["application/json; charset=UTF-8"])
class StatusController {

	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}

	/**
	 * Retrieve basic info of the API and status on all dependant downstream services.
	 * @return Status info.
	 */
	@GetMapping
	fun status(): ResponseEntity<StatusResponse> {
		log.info("Status of API was requested")

		return ResponseEntity.ok(
			StatusResponse("API is online and functional.", Constants.APP_VERSION)
		)
	}
}