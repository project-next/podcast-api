package com.rtomyj.podcast.service

import com.nhaarman.mockito_kotlin.any
import com.rtomyj.podcast.dao.Dao
import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.TestObjectsFromFile
import com.rtomyj.podcast.util.enum.ErrorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Sort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PodcastService::class])
@Tag("Service")
class PodcastServiceTest {
	@MockBean
	private lateinit var daoMock: Dao

	@MockBean
	private lateinit var podcastCrudRepositoryMock: PodcastCrudRepository

	@MockBean
	private lateinit var podcastEpisodePagingAndSortingRepositoryMock: PodcastEpisodePagingAndSortingRepository

	@Autowired
	private lateinit var podcastService: PodcastService

	@Nested
	inner class PodcastRSSFeedRetrieval {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Retrieve Feed`() {
				// Mock
				val mockPodcastData = TestObjectsFromFile.podcastData1
				val podcastId = mockPodcastData.podcast.id
				val mockedPodcast = mockPodcastData.podcast
				val mockedEpisodes = mockPodcastData.podcastEpisodes

				Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
				Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.findAllByPodcastId(podcastId, Sort.by("publicationDate")))
					.thenReturn(mockedEpisodes as ArrayList<PodcastEpisode>)

				// Call
				val feed = podcastService.getRssFeedForPodcast(podcastId)

				// Assert
				Assertions.assertNotNull(feed)

				Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
				Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).findAllByPodcastId(podcastId, Sort.by("publicationDate"))
			}
		}
	}

	@Nested
	inner class PodcastInfoRetrieval {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Retrieve Podcast Info`() {
				// Mock
				val mockPodcastData = TestObjectsFromFile.podcastData1
				val podcastId = mockPodcastData.podcast.id
				val mockedPodcast = mockPodcastData.podcast
				val mockedEpisodes = mockPodcastData.podcastEpisodes

				Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
				Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.findAllByPodcastId(podcastId, Sort.by("publicationDate")))
					.thenReturn(mockedEpisodes as ArrayList<PodcastEpisode>)

				// Call
				val data = podcastService.getPodcastData(podcastId)

				// Assert
				Assertions.assertNotNull(data)
				Assertions.assertEquals(mockPodcastData, data)

				Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
				Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).findAllByPodcastId(podcastId, Sort.by("publicationDate"))
			}
		}
	}

	@Nested
	inner class StoreNewPodcast {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Add Podcast To DB`() {
				// Mock
				val podcast = TestObjectsFromFile.podcastData1.podcast

				Mockito.`when`(podcastCrudRepositoryMock.save(podcast))
					.thenReturn(podcast)

				// Call
				podcastService.storeNewPodcast(podcast)

				// Assert
				Mockito.verify(podcastCrudRepositoryMock).save(podcast)
			}
		}
	}

	@Nested
	inner class UpdateExistingPodcast {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Update Existing Podcast`() {
				// Mock
				val podcastId = TestObjectsFromFile.podcastData1.podcast.id
				val podcast = TestObjectsFromFile.podcastData1.podcast

				Mockito.doNothing().`when`(daoMock).updatePodcast(podcastId, podcast)

				// Call
				podcastService.updatePodcast(podcastId, podcast)

				// Assert
				Mockito.verify(daoMock).updatePodcast(podcastId, podcast)
			}
		}
	}

	@Nested
	inner class StoreNewPodcastEpisode {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Store New Episode`() {
				// Mock
				val podcastId = TestObjectsFromFile.podcastData1.podcast.id
				val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val delimitedKeywords = podcastEpisode.keywords.joinToString(separator = "|")

				Mockito.doNothing().`when`(daoMock).storeNewPodcastEpisode(podcastEpisode, delimitedKeywords)

				// Call
				podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)

				// Assert
				Mockito.verify(daoMock).storeNewPodcastEpisode(podcastEpisode, delimitedKeywords)
			}
		}

		@Nested
		inner class VerificationIssue {
			@Test
			fun `Podcast IDs Differ`() {
				// Mock
				val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]

				Mockito.doNothing().`when`(daoMock).storeNewPodcastEpisode(any(), any())

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ podcastService.storeNewPodcastEpisode("Random", podcastEpisode) },
					"Wanted differing IDs and an exception to rise, but exception did not get thrown"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals("Podcast ID from URL and the one from the body do not match!", err.message)
				Assertions.assertEquals(ErrorType.G005, err.errorType)

				Mockito.verify(daoMock, Mockito.times(0)).storeNewPodcastEpisode(any(), any())
			}
		}
	}

	@Nested
	inner class UpdatePodcastEpisode {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Update Episode`() {
				// Mock
				val podcastId = TestObjectsFromFile.podcastData1.podcast.id
				val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val delimitedKeywords = podcastEpisode.keywords.joinToString(separator = "|")

				Mockito.doNothing().`when`(daoMock).updatePodcastEpisode(podcastEpisode, delimitedKeywords)

				// Call
				podcastService.updatePodcastEpisode(podcastId, podcastEpisode)

				// Assert
				Mockito.verify(daoMock).updatePodcastEpisode(podcastEpisode, delimitedKeywords)
			}
		}

		@Nested
		inner class VerificationIssue {
			@Test
			fun `Podcast IDs Differ`() {
				// Mock
				val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]

				Mockito.doNothing().`when`(daoMock).updatePodcastEpisode(any(), any())

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ podcastService.updatePodcastEpisode("Random", podcastEpisode) },
					"Wanted differing IDs and an exception to rise, but exception did not get thrown"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals("Podcast ID from URL and the one from the body do not match!", err.message)
				Assertions.assertEquals(ErrorType.G005, err.errorType)

				Mockito.verify(daoMock, Mockito.times(0)).updatePodcastEpisode(any(), any())
			}
		}
	}
}