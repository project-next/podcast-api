package com.rtomyj.podcast.controller

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.exception.PodcastError
import com.rtomyj.podcast.exception.PodcastExceptionAdvice
import com.rtomyj.podcast.model.PodcastData
import com.rtomyj.podcast.model.RssFeed
import com.rtomyj.podcast.service.PodcastService
import com.rtomyj.podcast.util.TestConstants
import com.rtomyj.podcast.util.TestObjectsFromFile
import com.rtomyj.podcast.util.enum.ErrorType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
/*
	Normally, RetrievePodcastDataController would not need to be imported in @ContextConfiguration but instead in the @WebMvcTest.
		However, since we are using @ContextConfiguration, we have to include RetrievePodcastDataController here
		 which will correctly configure our tests - else we will see 404 errors when using MockMvc.
	ControllerAdvice should be imported, so it can handle the errors correctly.
 */
@ContextConfiguration(
    classes = [SecurityConfig::class, RetrievePodcastDataController::class, PodcastExceptionAdvice::class,
        RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class]
)  // import classes as beans - these are needed by SecurityConfig
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@Tag("Controller")
class RetrievePodcastDataControllerTest {
    @MockBean
    private lateinit var service: PodcastService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class LegacyPodcastFeedAuthenticationHappyPathTests {
        @Test
        fun `Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1
            val feed = RssFeed(PodcastData(mockData.podcast, mockData.podcastEpisodes))
            val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

            // setup mocks
            `when`(service.getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(feed)

            mockMvc.perform(
                get(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1).with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.title))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.link))
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.description)
                )
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.language))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.copyright))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").exists())
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace)
                        .string(mockData.podcast.email)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace)
                        .string(mockData.podcast.category)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace)
                        .string(if (mockData.podcast.isExplicit) "yes" else "no")
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace)
                        .string(mockData.podcast.imageUrl)
                )


            // verify mocks are called
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
            mockMvc.perform(
                head(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1).with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.content().string(""))

            // verify
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1
            val feed = RssFeed(PodcastData(mockData.podcast, mockData.podcastEpisodes))
            val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

            // setup mocks
            `when`(service.getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(feed)

            mockMvc.perform(
                get(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.title))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.link))
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.description)
                )
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.language))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.copyright))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").exists())
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace)
                        .string(mockData.podcast.email)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace)
                        .string(mockData.podcast.category)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace)
                        .string(if (mockData.podcast.isExplicit) "yes" else "no")
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace)
                        .string(mockData.podcast.imageUrl)
                )

            // verify mocks are called
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
            mockMvc.perform(
                head(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
            ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.content().string(""))

            // verify
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }
    }

    @Nested
    inner class LegacyPodcastFeedAuthenticationErrorScenarioTests {
        @Test
        fun `Retrieve Podcast Data XML - With Incorrectly Formatted Podcast ID`() {
            val body = PodcastError(ErrorType.G001.error, ErrorType.G001.name)

            mockMvc.perform(
                get(TestConstants.PODCAST_WITH_ID_ENDPOINT, "NOT_ENOUGH_CHARS")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(body.message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(body.code)))

            // verify mocks are not called
            Mockito.verify(service, Mockito.times(0)).getRssFeedForPodcast(any())
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - With Incorrectly Formatted Podcast ID`() {
            val body = PodcastError(ErrorType.G001.error, ErrorType.G001.name)

            mockMvc.perform(
                head(TestConstants.PODCAST_WITH_ID_ENDPOINT, "NOT_ENOUGH_CHARS")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(body.message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(body.code)))

            // verify
            Mockito.verify(service, Mockito.times(0)).getRssFeedForPodcast(any())
        }
    }

    @Nested
    inner class RetrievePodcastFeedAuthenticationHappyPathTests {
        @Test
        fun `Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1
            val feed = RssFeed(PodcastData(mockData.podcast, mockData.podcastEpisodes))
            val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

            // setup mocks
            `when`(service.getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(feed)

            mockMvc.perform(
                get(
                    TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT,
                    TestConstants.PODCAST_ID_FROM_MOCK_RES_1
                ).with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.title))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.link))
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.description)
                )
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.language))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.copyright))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").exists())
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace)
                        .string(mockData.podcast.email)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace)
                        .string(mockData.podcast.category)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace)
                        .string(if (mockData.podcast.isExplicit) "yes" else "no")
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace)
                        .string(mockData.podcast.imageUrl)
                )

            // verify mocks are called
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
            mockMvc.perform(
                head(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1).with(
                    csrf()
                )
            ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.content().string(""))

            // verify
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1
            val feed = RssFeed(PodcastData(mockData.podcast, mockData.podcastEpisodes))
            val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

            // setup mocks
            `when`(service.getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(feed)

            mockMvc.perform(
                get(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.title))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.link))
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.description)
                )
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.language))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.copyright))
                .andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").exists())
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace)
                        .string(mockData.podcast.email)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace)
                        .string(mockData.podcast.category)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace)
                        .string(mockData.podcast.author)
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace)
                        .string(if (mockData.podcast.isExplicit) "yes" else "no")
                )
                .andExpect(
                    MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace)
                        .string(mockData.podcast.imageUrl)
                )

            // verify mocks are called
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
            mockMvc.perform(
                head(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
            ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.content().string(""))

            // verify
            Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }
    }

    @Nested
    inner class RetrievePodcastFeedAuthenticationErrorScenarioTests {
        @Test
        fun `Retrieve Podcast Data XML - With Incorrectly Formatted Podcast ID`() {
            val body = PodcastError(ErrorType.G001.error, ErrorType.G001.name)

            mockMvc.perform(
                get(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, "NOT_ENOUGH_CHARS")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(body.message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(body.code)))

            // verify mocks are not called
            Mockito.verify(service, Mockito.times(0)).getRssFeedForPodcast(any())
        }

        @Test
        fun `HEAD Request - Retrieve Podcast Data XML - With Incorrectly Formatted Podcast ID`() {
            val body = PodcastError(ErrorType.G001.error, ErrorType.G001.name)

            mockMvc.perform(
                head(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, "NOT_ENOUGH_CHARS")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(body.message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(body.code)))

            // verify
            Mockito.verify(service, Mockito.times(0)).getRssFeedForPodcast(any())
        }
    }


    @Nested
    inner class RetrievePodcastJSONAuthenticationHappyPathTests {
        @Test
        fun `Retrieve Podcast Data As JSON - Authorization Header Is Missing - With CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1

            // setup mocks
            `when`(service.getPodcastData(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(mockData)

            mockMvc.perform(
                get(
                    TestConstants.PODCAST_DATA_AS_JSON_ENDPOINT,
                    TestConstants.PODCAST_ID_FROM_MOCK_RES_1
                ).with(csrf())
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.id", Matchers.`is`(mockData.podcast.id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.title", Matchers.`is`(mockData.podcast.title)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.link", Matchers.`is`(mockData.podcast.link)))
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.description",
                        Matchers.`is`(mockData.podcast.description)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.language",
                        Matchers.`is`(mockData.podcast.language)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.copyright",
                        Matchers.`is`(mockData.podcast.copyright)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.lastBuildDate",
                        Matchers.`is`(mockData.podcast.lastBuildDate.toString())
                    )
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.email", Matchers.`is`(mockData.podcast.email)))
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.category",
                        Matchers.`is`(mockData.podcast.category)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.author",
                        Matchers.`is`(mockData.podcast.author)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.isExplicit",
                        Matchers.`is`(mockData.podcast.isExplicit)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.imageUrl",
                        Matchers.`is`(mockData.podcast.imageUrl)
                    )
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcastEpisodes").isNotEmpty)

            // verify mocks are called
            Mockito.verify(service).getPodcastData(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }

        @Test
        fun `Retrieve Podcast Data As JSON - Authorization Header Is Missing - Without CSRF`() {
            val mockData = TestObjectsFromFile.podcastData1

            // setup mocks
            `when`(service.getPodcastData(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)).thenReturn(mockData)

            mockMvc.perform(
                get(TestConstants.PODCAST_DATA_AS_JSON_ENDPOINT, TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.id", Matchers.`is`(mockData.podcast.id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.title", Matchers.`is`(mockData.podcast.title)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.link", Matchers.`is`(mockData.podcast.link)))
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.description",
                        Matchers.`is`(mockData.podcast.description)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.language",
                        Matchers.`is`(mockData.podcast.language)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.copyright",
                        Matchers.`is`(mockData.podcast.copyright)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.lastBuildDate",
                        Matchers.`is`(mockData.podcast.lastBuildDate.toString())
                    )
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcast.email", Matchers.`is`(mockData.podcast.email)))
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.category",
                        Matchers.`is`(mockData.podcast.category)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.author",
                        Matchers.`is`(mockData.podcast.author)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.isExplicit",
                        Matchers.`is`(mockData.podcast.isExplicit)
                    )
                )
                .andExpect(
                    MockMvcResultMatchers.jsonPath(
                        "$.podcast.imageUrl",
                        Matchers.`is`(mockData.podcast.imageUrl)
                    )
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.podcastEpisodes").isNotEmpty)

            // verify mocks are called
            Mockito.verify(service).getPodcastData(TestConstants.PODCAST_ID_FROM_MOCK_RES_1)
        }
    }

    @Nested
    inner class RetrievePodcastJSONAuthenticationErrorScenarioTests {
        @Test
        fun `Retrieve Podcast Data As JSON - With Incorrectly Formatted Podcast ID`() {
            val body = PodcastError(ErrorType.G001.error, ErrorType.G001.name)

            mockMvc.perform(
                get(TestConstants.PODCAST_DATA_AS_JSON_ENDPOINT, "NOT_ENOUGH_CHARS")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`(body.message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.`is`(body.code)))

            // verify mocks are not called
            Mockito.verify(service, Mockito.times(0)).getRssFeedForPodcast(any())
        }
    }
}
