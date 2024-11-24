package com.rtomyj.podcast.controller

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.exception.PodcastExceptionAdvice
import com.rtomyj.podcast.service.PodcastService
import com.rtomyj.podcast.util.Helpers
import com.rtomyj.podcast.util.TestConstants
import com.rtomyj.podcast.util.TestObjectsFromFile
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
/*
	Normally, StorePodcastDataController would not need to be imported in @ContextConfiguration but instead in the @WebMvcTest.
		However, since we are using @ContextConfiguration, we have to include StorePodcastDataController here
		 which will correctly configure our tests - else we will see 404 errors when using MockMvc.
	ControllerAdvice should be imported, so it can handle the errors correctly.
 */
@ContextConfiguration(
    classes = [SecurityConfig::class, StorePodcastDataController::class, PodcastExceptionAdvice::class,
        RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class]
)  // import classes as beans - these are needed by SecurityConfig
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
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
            mockMvc.perform(
                post(TestConstants.PODCAST_ENDPOINT).contentType(TestConstants.CONTENT_TYPE)
                    .content(TestConstants.EMPTY_BODY)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
                .andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }

        @Test
        fun `Authorization Header Is Missing - Without CSRF`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_ENDPOINT).contentType(TestConstants.CONTENT_TYPE)
                    .content(TestConstants.EMPTY_BODY)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
                .andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }

        @Test
        fun `User is not admin`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_ENDPOINT).contentType(TestConstants.CONTENT_TYPE)
                    .content(TestConstants.EMPTY_BODY)
                    .header("Authorization", "Basic VHlsZXI6Q2hhbmdlbWUh").with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isForbidden).andExpect(jsonPath("$.message", `is`("Forbidden")))
                .andExpect(jsonPath("$.code", `is`("G004")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }
    }

    @Nested
    inner class StoreNewPodcastRequestValidationError {
        @Test
        fun `User is admin - Body is empty`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_ENDPOINT).contentType(TestConstants.CONTENT_TYPE)
                    .content(TestConstants.EMPTY_BODY)
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
    inner class StoreNewPodcastHappyPath {
        @Test
        fun `User is admin - Body Is Valid`() {
            // setup mocks
            val mockPodcast = TestObjectsFromFile.podcastData1
            Mockito.doNothing().`when`(service).storeNewPodcast(mockPodcast)

            mockMvc.perform(
                post(TestConstants.PODCAST_ENDPOINT).contentType(TestConstants.CONTENT_TYPE)
                    .content(Helpers.mapper.writeValueAsString(mockPodcast))
                    .header("Authorization", "Basic SmF2aTpDaGFuZ2VtZSE=")
            ).andExpect(MockMvcResultMatchers.status().isCreated).andExpect(
                jsonPath(
                    "message", `is`(
                        "Successfully stored new podcast!"
                    )
                )
            )

            // verify mocks are called
            Mockito.verify(service).storeNewPodcast(mockPodcast)
        }
    }

    @Nested
    inner class StoreNewPodcastEpisodeAuthenticationIssue {
        @Test
        fun `Authorization Header Is Missing - With CSRF`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_EPISODE_ENDPOINT, TestConstants.PODCAST_ID_FROM_SQL_QUERY).with(
                    csrf()
                )
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
                .andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }

        @Test
        fun `Authorization Header Is Missing - Without CSRF`() {
            mockMvc.perform(post(TestConstants.PODCAST_EPISODE_ENDPOINT, TestConstants.PODCAST_ID_FROM_SQL_QUERY))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
                .andExpect(jsonPath("$.message", `is`("Unauthorized"))).andExpect(jsonPath("$.code", `is`("G003")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }

        @Test
        fun `User is not admin`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_EPISODE_ENDPOINT, TestConstants.PODCAST_ID_FROM_SQL_QUERY).contentType(
                    TestConstants.CONTENT_TYPE
                ).content(TestConstants.EMPTY_BODY)
                    .header("Authorization", "Basic VHlsZXI6Q2hhbmdlbWUh").with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isForbidden).andExpect(jsonPath("$.message", `is`("Forbidden")))
                .andExpect(jsonPath("$.code", `is`("G004")))

            // verify mocks are called
            Mockito.verify(service, Mockito.times(0)).storeNewPodcast(any())
        }
    }

    @Nested
    inner class StoreNewPodcastEpisodeRequestValidationError {
        @Test
        fun `User is admin - Body is empty`() {
            mockMvc.perform(
                post(TestConstants.PODCAST_EPISODE_ENDPOINT, TestConstants.PODCAST_ID_FROM_SQL_QUERY).contentType(
                    TestConstants.CONTENT_TYPE
                ).content(TestConstants.EMPTY_BODY)
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
    inner class StoreNewPodcastEpisodeHappyPath {
        @Test
        fun `User is admin - Body Is Valid`() {
            // setup mocks
            val mockEpisode = TestObjectsFromFile.podcastData1.episodes[0]
            Mockito.doNothing().`when`(service)
                .storeNewPodcastEpisode(TestObjectsFromFile.podcastData1.id, mockEpisode)

            mockMvc.perform(
                post(TestConstants.PODCAST_EPISODE_ENDPOINT, TestObjectsFromFile.podcastData1.id).contentType(
                    TestConstants.CONTENT_TYPE
                )
                    .content(Helpers.mapper.writeValueAsString(mockEpisode))
                    .header("Authorization", "Basic SmF2aTpDaGFuZ2VtZSE=")
            ).andExpect(MockMvcResultMatchers.status().isCreated).andExpect(
                jsonPath(
                    "message", `is`(
                        "Successfully stored new podcast episode!"
                    )
                )
            )

            // verify mocks are called
            Mockito.verify(service).storeNewPodcastEpisode(TestObjectsFromFile.podcastData1.id, mockEpisode)
        }
    }
}
