package com.rtomyj.podcast.util.filter

import com.rtomyj.podcast.util.Constants
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class RequestFilter : OncePerRequestFilter() {
	companion object {
		private const val X_FORWARDED_FOR = "X-Forwarded-For"
		const val CLIENT_ID_NAME = "CLIENT_ID"

		/**
		 * Configures the global MDC object for all requests. MDC is used to hold useful info that will later be used in logs to better track requests.
		 * @param httpServletRequest Contains useful information about new requests to the server from the client that will be used to access IP address and header information.
		 */
		fun configureMDC(httpServletRequest: HttpServletRequest, clientIP: String) {
			val queryParams = if (httpServletRequest.queryString == null) "" else "?" + httpServletRequest.queryString

			// proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
			MDC.put(Constants.CLIENT_IP_MDC, clientIP.replace("[", "").replace("]", ""))
			MDC.put("reqPath", httpServletRequest.servletPath + queryParams)
			MDC.put("reqUUID", UUID.randomUUID().toString())
			MDC.put("clientID", httpServletRequest.getHeader(CLIENT_ID_NAME))
			MDC.put("userAgent", httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
		}
	}
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		try {
			val clientIP = if (StringUtils.hasLength(request.getHeader(X_FORWARDED_FOR))) request.getHeader(X_FORWARDED_FOR) else request.remoteHost

			configureMDC(request, clientIP)
			chain.doFilter(request, response)
		} finally {
			MDC.clear()
		}
	}
}