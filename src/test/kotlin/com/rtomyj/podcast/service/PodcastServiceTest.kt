package com.rtomyj.podcast.service

import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodeCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.TestObjectsFromFile
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
	private lateinit var podcastCrudRepositoryMock: PodcastCrudRepository

	@MockBean
	private lateinit var podcastEpisodeCrudRepository: PodcastEpisodeCrudRepository

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

				Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
					.thenReturn(Optional.of(podcast))

				Mockito.`when`(podcastCrudRepositoryMock.save(podcast))
					.thenReturn(podcast)

				// Call
				podcastService.updatePodcast(podcastId, podcast)

				// Assert
				Mockito.verify(podcastCrudRepositoryMock).save(podcast)
				Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
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

				Mockito.`when`(podcastEpisodeCrudRepository.save(podcastEpisode))
					.thenReturn(podcastEpisode)

				// Call
				podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)

				// Assert
				Mockito.verify(podcastEpisodeCrudRepository).save(podcastEpisode)
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
				val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val mockPodcastData = TestObjectsFromFile.podcastData1
				val podcastId = mockPodcastData.podcast.id
				val mockedPodcast = mockPodcastData.podcast

				Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
				Mockito.`when`(podcastEpisodeCrudRepository.findById(podcastEpisode.episodeId)).thenReturn(Optional.of(podcastEpisode))
				Mockito.`when`(podcastEpisodeCrudRepository.save(podcastEpisode))
					.thenReturn(podcastEpisode)

				// Call
				podcastService.updatePodcastEpisode(podcastId, podcastEpisode)

				// Assert
				Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
				Mockito.verify(podcastEpisodeCrudRepository).findById(podcastEpisode.episodeId)
				Mockito.verify(podcastEpisodeCrudRepository).save(podcastEpisode)
			}
		}
	}
}