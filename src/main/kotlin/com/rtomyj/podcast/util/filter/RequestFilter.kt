package com.rtomyj.podcast.util.filter

import com.rtomyj.podcast.util.Logging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RequestFilter : OncePerRequestFilter() {
	companion object {
		const val X_FORWARDED_FOR = "X-Forwarded-For"
	}
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		try {
			val clientIP = if (StringUtils.hasLength(request.getHeader(X_FORWARDED_FOR))) request.getHeader(X_FORWARDED_FOR) else request.remoteHost

			Logging.configureMDC(request, clientIP)
			chain.doFilter(request, response)
		} finally {
			MDC.clear()
		}
	}
}