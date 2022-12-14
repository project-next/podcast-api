package com.rtomyj.podcast.util

import com.google.common.net.HttpHeaders
import com.rtomyj.podcast.util.constant.Generic
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import java.util.*

/**
 * Contains common logging utilities.
 */
class Logging private constructor() {
	init {
		throw UnsupportedOperationException("Cannot create instance for class: " + this.javaClass.toString())
	}

	companion object {
		private const val CLIENT_ID_NAME = "CLIENT_ID"

		/**
		 * Configures the global MDC object for all requests. MDC is used to hold useful info that will later be used in logs to better track requests.
		 * @param httpServletRequest Contains useful information about new requests to the server from the client that will be used to access IP address and header information.
		 */
		fun configureMDC(httpServletRequest: HttpServletRequest, clientIP: String) {
			val queryParams = if (httpServletRequest.queryString == null) "" else "?" + httpServletRequest.queryString

			// proxies and load balancers will forward client IP address in HTTP_X_FORWARDED_FOR header. If header exists, use value. Otherwise, use requests IP
			MDC.put(Generic.CLIENT_IP_MDC, clientIP.replace("[", "").replace("]", ""))
			MDC.put("reqPath", httpServletRequest.servletPath + queryParams)
			MDC.put("reqUUID", UUID.randomUUID().toString())
			MDC.put("clientID", httpServletRequest.getHeader(CLIENT_ID_NAME))
			MDC.put("userAgent", httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
		}
	}
}