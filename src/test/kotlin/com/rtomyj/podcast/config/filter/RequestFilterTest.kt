package com.rtomyj.podcast.config.filter

import com.nhaarman.mockito_kotlin.atLeastOnce
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
class RequestFilterTest {
    @Mock
    private lateinit var httpServletRequest: HttpServletRequest

    companion object {
        private const val PATH = "/path"
        private const val QUERY_STRING = "x=y"
        private const val CLIENT_ID = "JUNIT"
        private const val AGENT = "POSTMAN"
    }

    @Test
    fun `Test MDC Configuration - Query String Not Null`() {
        // mock
        Mockito.`when`(httpServletRequest.servletPath)
            .thenReturn(PATH)
        Mockito.`when`(httpServletRequest.queryString)
            .thenReturn(QUERY_STRING)
        Mockito.`when`(httpServletRequest.getHeader(RequestFilter.CLIENT_ID_NAME))
            .thenReturn(CLIENT_ID)
        Mockito.`when`(httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
            .thenReturn(AGENT)

        // call
        RequestFilter.configureMDC(httpServletRequest, "")

        // verify
        assertNotNull(MDC.get("reqPath"))
        assertNotNull(MDC.get("reqUUID"))
        assertNotNull(MDC.get("clientID"))
        assertNotNull(MDC.get("userAgent"))

        assertEquals("$PATH?$QUERY_STRING", MDC.get("reqPath"))
        assertEquals(CLIENT_ID, MDC.get("clientID"))
        assertEquals(AGENT, MDC.get("userAgent"))

        Mockito.verify(httpServletRequest)
            .servletPath
        Mockito.verify(httpServletRequest, atLeastOnce())
            .queryString
        Mockito.verify(httpServletRequest)
            .getHeader(RequestFilter.CLIENT_ID_NAME)
        Mockito.verify(httpServletRequest)
            .getHeader(HttpHeaders.USER_AGENT)
    }

    @Test
    fun `Test MDC Configuration - Query String Is Null`() {
        // mock
        Mockito.`when`(httpServletRequest.servletPath)
            .thenReturn(PATH)
        Mockito.`when`(httpServletRequest.queryString)
            .thenReturn(null)
        Mockito.`when`(httpServletRequest.getHeader(RequestFilter.CLIENT_ID_NAME))
            .thenReturn(CLIENT_ID)
        Mockito.`when`(httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
            .thenReturn(AGENT)

        // call
        RequestFilter.configureMDC(httpServletRequest, "")

        // verify
        assertNotNull(MDC.get("reqPath"))
        assertNotNull(MDC.get("reqUUID"))
        assertNotNull(MDC.get("clientID"))
        assertNotNull(MDC.get("userAgent"))

        assertEquals(PATH, MDC.get("reqPath"))
        assertEquals(CLIENT_ID, MDC.get("clientID"))
        assertEquals(AGENT, MDC.get("userAgent"))

        Mockito.verify(httpServletRequest)
            .servletPath
        Mockito.verify(httpServletRequest)
            .queryString
        Mockito.verify(httpServletRequest)
            .getHeader(RequestFilter.CLIENT_ID_NAME)
        Mockito.verify(httpServletRequest)
            .getHeader(HttpHeaders.USER_AGENT)
    }
}