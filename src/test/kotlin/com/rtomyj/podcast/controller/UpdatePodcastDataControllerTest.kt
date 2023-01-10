package com.rtomyj.podcast.controller

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.exception.PodcastExceptionAdvice
import com.rtomyj.podcast.service.PodcastService
import com.rtomyj.podcast.util.TestConstants
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
@ContextConfiguration(classes = [RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class])  // import classes as beans - these are needed by SecurityConfig
/*
	Import security beans defined in SecurityConfig.
	Controller needs to be imported here and not in @WebMvcTest or else 404 errors is all that will be returned.
	ControllerAdvice should be imported, so it can handle the errors correctly.
 */
@Import(value = [SecurityConfig::class, UpdatePodcastDataController::class, PodcastExceptionAdvice::class])
@Tag("Controller")
class UpdatePodcastDataControllerTest {
	@MockBean
	private lateinit var service: PodcastService

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Nested
	inner class UpdateExistingPodcastAuthenticationIssue {
		@Test
		fun `Authorization Header Is Missing - With CSRF`() {
			mockMvc.perform(
				put(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.VALID_PODCAST_ID).contentType(TestConstants.CONTENT_TYPE).content(TestConstants.EMPTY_BODY).with(csrf())
			).andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `Authorization Header Is Missing - Without CSRF`() {
			mockMvc.perform(
				put(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.VALID_PODCAST_ID).contentType(TestConstants.CONTENT_TYPE).content(TestConstants.EMPTY_BODY)
			).andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `User is not admin`() {
			mockMvc.perform(
				put(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.VALID_PODCAST_ID).contentType(TestConstants.CONTENT_TYPE).content(TestConstants.EMPTY_BODY)
					.header("Authorization", "Basic VHlsZXI6Q2hhbmdlbWUh").with(csrf())
			).andExpect(MockMvcResultMatchers.status().isForbidden).andExpect(jsonPath("$.message", `is`("Forbidden"))).andExpect(jsonPath("$.code", `is`("G004")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `User is admin - Body is empty`() {
			mockMvc.perform(
				put(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.VALID_PODCAST_ID).contentType(TestConstants.CONTENT_TYPE).content(TestConstants.EMPTY_BODY)
					.header("Authorization", "Basic SmF2aTpDaGFuZ2VtZSE=")
			).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity).andExpect(
				jsonPath(
					"message", startsWith(
						"Body is missing data or fields do not conform to spec."
					)
				)
			).andExpect(jsonPath("$.code", `is`("G002")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}
	}
}