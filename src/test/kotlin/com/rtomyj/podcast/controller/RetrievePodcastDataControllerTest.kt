package com.rtomyj.podcast.controller

import com.rtomyj.podcast.config.RestAccessDeniedHandler
import com.rtomyj.podcast.config.RestAuthenticationEntryPoint
import com.rtomyj.podcast.config.SecurityConfig
import com.rtomyj.podcast.exception.PodcastExceptionAdvice
import com.rtomyj.podcast.model.RssFeed
import com.rtomyj.podcast.service.PodcastService
import com.rtomyj.podcast.util.TestConstants
import com.rtomyj.podcast.util.TestObjectsFromFile
import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@ContextConfiguration(classes = [RestAccessDeniedHandler::class, RestAuthenticationEntryPoint::class])  // import classes as beans - these are needed by SecurityConfig
/*
	Import security beans defined in SecurityConfig.
	Controller needs to be imported here and not in @WebMvcTest or else 404 errors is all that will be returned.
	ControllerAdvice should be imported, so it can handle the errors correctly.
 */
@Import(value = [SecurityConfig::class, RetrievePodcastDataController::class, PodcastExceptionAdvice::class])
@Tag("Controller")
class RetrievePodcastDataControllerTest {
	@MockBean
	private lateinit var service: PodcastService

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Nested
	inner class LegacyPodcastFeedAuthenticationTests {
		@Test
		fun `Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1
			val feed = RssFeed(mockData.podcast, mockData.podcastEpisodes)
			val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

			// setup mocks
			`when`(service.getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(feed)

			mockMvc.perform(
				get(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID).with(csrf())
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.podcastTitle))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.podcastLink))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.podcastDescription))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.podcastLanguage))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.podcastCopyright))
//				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").string(mockData.podcast.podcastLastBuildDate))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace).string(mockData.podcast.podcastEmail))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace).string(mockData.podcast.podcastCategory))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace).string(if (mockData.podcast.isExplicit) "yes" else "no"))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace).string(mockData.podcast.podcastImageUrl))


			// verify mocks are called
			Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)
		}

		@Test
		fun `Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1
			val feed = RssFeed(mockData.podcast, mockData.podcastEpisodes)
			val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

			// setup mocks
			`when`(service.getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(feed)

			mockMvc.perform(
				get(TestConstants.PODCAST_WITH_ID_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID)
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.podcastTitle))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.podcastLink))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.podcastDescription))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.podcastLanguage))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.podcastCopyright))
//				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").string(mockData.podcast.podcastLastBuildDate))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace).string(mockData.podcast.podcastEmail))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace).string(mockData.podcast.podcastCategory))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace).string(if (mockData.podcast.isExplicit) "yes" else "no"))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace).string(mockData.podcast.podcastImageUrl))

			// verify mocks are called
			Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)
		}
	}

	@Nested
	inner class RetrievePodcastFeedAuthenticationTests {
		@Test
		fun `Retrieve Podcast Data XML - Authorization Header Is Missing - With CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1
			val feed = RssFeed(mockData.podcast, mockData.podcastEpisodes)
			val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

			// setup mocks
			`when`(service.getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(feed)

			mockMvc.perform(
				get(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID).with(csrf())
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.podcastTitle))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.podcastLink))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.podcastDescription))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.podcastLanguage))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.podcastCopyright))
//				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").string(mockData.podcast.podcastLastBuildDate))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace).string(mockData.podcast.podcastEmail))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace).string(mockData.podcast.podcastCategory))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace).string(if (mockData.podcast.isExplicit) "yes" else "no"))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace).string(mockData.podcast.podcastImageUrl))

			// verify mocks are called
			Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)
		}

		@Test
		fun `Retrieve Podcast Data XML - Authorization Header Is Missing - Without CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1
			val feed = RssFeed(mockData.podcast, mockData.podcastEpisodes)
			val namespace = mapOf(Pair("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd"))

			// setup mocks
			`when`(service.getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(feed)

			mockMvc.perform(
				get(TestConstants.PODCAST_DATA_AS_FEED_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID)
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/title").string(mockData.podcast.podcastTitle))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/link").string(mockData.podcast.podcastLink))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/description").string(mockData.podcast.podcastDescription))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/language").string(mockData.podcast.podcastLanguage))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/copyright").string(mockData.podcast.podcastCopyright))
//				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/lastBuildDate").string(mockData.podcast.podcastLastBuildDate))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:email", namespace).string(mockData.podcast.podcastEmail))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:owner/itunes:name", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:category/@text", namespace).string(mockData.podcast.podcastCategory))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:author", namespace).string(mockData.podcast.podcastAuthor))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:explicit", namespace).string(if (mockData.podcast.isExplicit) "yes" else "no"))
				.andExpect(MockMvcResultMatchers.xpath("/rss/channel/itunes:image/@href", namespace).string(mockData.podcast.podcastImageUrl))

			// verify mocks are called
			Mockito.verify(service).getRssFeedForPodcast(TestConstants.PODCAST_DATA_RES_1_ID)
		}
	}

	@Nested
	inner class RetrievePodcastJSONAuthenticationTests {
		@Test
		fun `Retrieve Podcast Data As JSON - Authorization Header Is Missing - With CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1

			// setup mocks
			`when`(service.getPodcastData(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(mockData)

			mockMvc.perform(
				get(TestConstants.PODCAST_DATA_AS_JSON_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID).with(csrf())
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastId", Matchers.`is`(mockData.podcast.podcastId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastTitle", Matchers.`is`(mockData.podcast.podcastTitle)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLink", Matchers.`is`(mockData.podcast.podcastLink)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastDescription", Matchers.`is`(mockData.podcast.podcastDescription)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLanguage", Matchers.`is`(mockData.podcast.podcastLanguage)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastCopyright", Matchers.`is`(mockData.podcast.podcastCopyright)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLastBuildDate", Matchers.`is`(mockData.podcast.podcastLastBuildDate.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastEmail", Matchers.`is`(mockData.podcast.podcastEmail)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastCategory", Matchers.`is`(mockData.podcast.podcastCategory)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastAuthor", Matchers.`is`(mockData.podcast.podcastAuthor)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.isExplicit", Matchers.`is`(mockData.podcast.isExplicit)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastImageUrl", Matchers.`is`(mockData.podcast.podcastImageUrl)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcastEpisodes").isEmpty)

			// verify mocks are called
			Mockito.verify(service).getPodcastData(TestConstants.PODCAST_DATA_RES_1_ID)
		}

		@Test
		fun `Retrieve Podcast Data As JSON - Authorization Header Is Missing - Without CSRF`() {
			val mockData = TestObjectsFromFile.podcastData1

			// setup mocks
			`when`(service.getPodcastData(TestConstants.PODCAST_DATA_RES_1_ID)).thenReturn(mockData)

			mockMvc.perform(
				get(TestConstants.PODCAST_DATA_AS_JSON_ENDPOINT, TestConstants.PODCAST_DATA_RES_1_ID)
			).andExpect(MockMvcResultMatchers.status().isOk).andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastId", Matchers.`is`(mockData.podcast.podcastId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastTitle", Matchers.`is`(mockData.podcast.podcastTitle)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLink", Matchers.`is`(mockData.podcast.podcastLink)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastDescription", Matchers.`is`(mockData.podcast.podcastDescription)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLanguage", Matchers.`is`(mockData.podcast.podcastLanguage)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastCopyright", Matchers.`is`(mockData.podcast.podcastCopyright)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastLastBuildDate", Matchers.`is`(mockData.podcast.podcastLastBuildDate.toString())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastEmail", Matchers.`is`(mockData.podcast.podcastEmail)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastCategory", Matchers.`is`(mockData.podcast.podcastCategory)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastAuthor", Matchers.`is`(mockData.podcast.podcastAuthor)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.isExplicit", Matchers.`is`(mockData.podcast.isExplicit)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcast.podcastImageUrl", Matchers.`is`(mockData.podcast.podcastImageUrl)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.podcastEpisodes").isEmpty)

			// verify mocks are called
			Mockito.verify(service).getPodcastData(TestConstants.PODCAST_DATA_RES_1_ID)
		}
	}
}