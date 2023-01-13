package com.rtomyj.podcast.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.podcast.exception.PodcastError
import com.rtomyj.podcast.util.enum.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
	@Autowired
	private lateinit var mapper: ObjectMapper

	@Throws(IOException::class)
	override fun commence(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
		val body = PodcastError("Unauthorized", ErrorType.G003.name)

		response.contentType = "application/json;charset=UTF-8"
		response.status = HttpServletResponse.SC_UNAUTHORIZED
		response.writer.write(mapper.writeValueAsString(body))
		response.writer.close()
	}
}