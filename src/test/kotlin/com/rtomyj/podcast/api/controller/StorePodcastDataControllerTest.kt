package com.rtomyj.podcast.api.controller

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.controller.StorePodcastDataController
import com.rtomyj.podcast.service.PodcastService
import org.hamcrest.Matchers.`is`
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(StorePodcastDataController::class)
@ContextConfiguration(classes = [RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class])  // import classes as beans - these are needed by SecurityConfig
@Import(SecurityConfig::class)  // import security beans defined in file
@Tag("Controller")
class StorePodcastDataControllerTest {
	@MockBean
	private lateinit var service: PodcastService

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Nested
	inner class StoreNewPodcastAuthenticationIssue {
		@Test
		fun `Authorization Header Is Missing - With CSRF`() {
			mockMvc.perform(post("/podcast").with(csrf())).andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized")))
				.andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `Authorization Header Is Missing - Without CSRF`() {
			mockMvc.perform(post("/podcast")).andExpect(MockMvcResultMatchers.status().isUnauthorized).andExpect(jsonPath("$.message", `is`("Unauthorized")))
				.andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `User is not admin`() {
			mockMvc.perform(post("/").header("Authorization", "Basic VHlsZXI6Q2hhbmdlbWUh")).andExpect(MockMvcResultMatchers.status().isForbidden)
				.andExpect(jsonPath("$.message", `is`("Forbidden"))).andExpect(jsonPath("$.code", `is`("G004")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}
	}

	@Nested
	inner class StoreNewPodcastEpisodeAuthenticationIssue {
		@Test
		fun `Authorization Header Is Missing - With CSRF`() {
			mockMvc.perform(post("/podcast/{podcastId}/episode", "16e2094c-1bbc-4de4-a6fd-ee6b730b77e2").with(csrf())).andExpect(MockMvcResultMatchers.status().isUnauthorized)
				.andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}

		@Test
		fun `Authorization Header Is Missing - Without CSRF`() {
			mockMvc.perform(post("/podcast/{podcastId}/episode", "16e2094c-1bbc-4de4-a6fd-ee6b730b77e2")).andExpect(MockMvcResultMatchers.status().isUnauthorized)
				.andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

			// verify mocks are called
			Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
		}
	}
}