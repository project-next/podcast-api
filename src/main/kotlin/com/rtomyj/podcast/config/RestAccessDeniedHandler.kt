package com.rtomyj.podcast.config

import com.rtomyj.podcast.exception.PodcastError
import com.rtomyj.podcast.util.enum.ErrorType
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import java.io.IOException


@Component
class RestAccessDeniedHandler : AccessDeniedHandler {
    @Autowired
    private lateinit var mapper: JsonMapper

    @Throws(IOException::class, ServletException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, e: AccessDeniedException) {
        val body = PodcastError("Forbidden", ErrorType.G004.name)

        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.writer.write(mapper.writeValueAsString(body))
        response.writer.close()
    }
}
