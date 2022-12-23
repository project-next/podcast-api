package com.rtomyj.podcast.api.controller

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.api.TestConstants
import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.controller.StorePodcastDataController
import com.rtomyj.podcast.exception.PodcastExceptionAdvice
import com.rtomyj.podcast.service.PodcastService
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
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.nio.charset.Charset

@WebMvcTest
@ContextConfiguration(classes = [RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class])  // import classes as beans - these are needed by SecurityConfig
/*
	Import security beans defined in SecurityConfig.
	Controller needs to be imported here and not in @WebMvcTest or else 404 errors is all that will be returned.
	ControllerAdvice should be imported, so it can handle the errors correctly.
 */
@Import(value = [SecurityConfig::class, StorePodcastDataController::class, PodcastExceptionAdvice::class])
@Tag("Controller")
class StorePodcastDataControllerTest {
	@MockBean
	private lateinit var service: PodcastService

	@Autowired
	private lateinit var mockMvc: MockMvc

	private val contentType = MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

	@Nested
	inner class StoreNewPodcastAuthenticationIssue {
		@Test
		fun `Authorization Header Is Missing - With CSRF`() {
			mockMvc.perform(
				post(TestConstants.PODCAST_ENDPOINT).contentType(contentType).content(TestConstants.EMPTY_BODY).header("Content-Type", TestConstants.EMPTY_BODY_LENGTH).with(csrf())
			).andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `Authorization Header Is Missing - Without CSRF`() {
			mockMvc.perform(post(TestConstants.PODCAST_ENDPOINT).contentType(contentType).content(TestConstants.EMPTY_BODY).header("Content-Type", TestConstants.EMPTY_BODY_LENGTH))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `User is not admin`() {
			mockMvc.perform(
				post(TestConstants.PODCAST_ENDPOINT).contentType(contentType).content(TestConstants.EMPTY_BODY).header("Content-Type", TestConstants.EMPTY_BODY_LENGTH)
					.header("Authorization", "Basic VHlsZXI6Q2hhbmdlbWUh").with(csrf())
			).andExpect(MockMvcResultMatchers.status().isForbidden).andExpect(jsonPath("$.message", `is`("Forbidden"))).andExpect(jsonPath("$.code", `is`("G004")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `User is admin - Body is empty`() {
			mockMvc.perform(
				post(TestConstants.PODCAST_ENDPOINT).contentType(contentType).content(TestConstants.EMPTY_BODY).header("Content-Length", TestConstants.EMPTY_BODY_LENGTH)
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

	@Nested
	inner class StoreNewPodcastEpisodeAuthenticationIssue {
		@Test
		fun `Authorization Header Is Missing - With CSRF`() {
			mockMvc.perform(post(TestConstants.PODCAST_EPISODE_ENDPOINT, "16e2094c-1bbc-4de4-a6fd-ee6b730b77e2").with(csrf()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `Authorization Header Is Missing - Without CSRF`() {
			mockMvc.perform(post(TestConstants.PODCAST_EPISODE_ENDPOINT, "16e2094c-1bbc-4de4-a6fd-ee6b730b77e2")).andExpect(MockMvcResultMatchers.status().isUnauthorized)
				.andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}
	}
}