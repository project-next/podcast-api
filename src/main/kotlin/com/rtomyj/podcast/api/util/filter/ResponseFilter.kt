package com.rtomyj.podcast.api.util.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class ResponseHeaderFilter : OncePerRequestFilter() {
	@Throws(IOException::class, ServletException::class)
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		response.setHeader("Cache-Control", "max-age=300")
		chain.doFilter(request, response)
	}
}