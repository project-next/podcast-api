package com.rtomyj.podcast.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodeCrudRepository
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
import org.springframework.dao.DataRetrievalFailureException
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
    inner class PodcastRSSFeedRetrievalHappyPath {
        @Test
        fun `Successfully Retrieve Feed`() {
            // Mock
            val mockPodcastData = TestObjectsFromFile.podcastData1
            val podcastId = mockPodcastData.podcast.id
            val mockedPodcast = mockPodcastData.podcast
            val mockedEpisodes = mockPodcastData.podcastEpisodes

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(
                podcastEpisodePagingAndSortingRepositoryMock.findAllByPodcastId(
                    podcastId,
                    Sort.by("publicationDate")
                )
            )
                .thenReturn(mockedEpisodes as ArrayList<PodcastEpisode>)

            // Call
            val feed = podcastService.getPodcastData(podcastId)

            // Assert
            Assertions.assertNotNull(feed)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock)
                .findAllByPodcastId(podcastId, Sort.by("publicationDate"))
        }
    }

    @Nested
    inner class PodcastInfoRetrievalHappyPath {
        @Test
        fun `Successfully Retrieve Podcast Info`() {
            // Mock
            val mockPodcastData = TestObjectsFromFile.podcastData1
            val podcastId = mockPodcastData.podcast.id
            val mockedPodcast = mockPodcastData.podcast
            val mockedEpisodes = mockPodcastData.podcastEpisodes

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(
                podcastEpisodePagingAndSortingRepositoryMock.findAllByPodcastId(
                    podcastId,
                    Sort.by("publicationDate")
                )
            )
                .thenReturn(mockedEpisodes as ArrayList<PodcastEpisode>)

            // Call
            val data = podcastService.getPodcastData(podcastId)

            // Assert
            Assertions.assertNotNull(data)
            Assertions.assertEquals(mockPodcastData, data)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock)
                .findAllByPodcastId(podcastId, Sort.by("publicationDate"))
        }
    }

    @Nested
    inner class PodcastInfoRetrievalErrorScenarios {
        @Test
        fun `Podcast ID Not In DB`() {
            // Mock
            val mockPodcastData = TestObjectsFromFile.podcastData1
            val podcastId = mockPodcastData.podcast.id

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.empty())

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java, { podcastService.getPodcastData(podcastId) }, "Expected an error thrown as there should be no records for ID."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Podcast ID not found in DB", ErrorType.DB001), err)

            Mockito.verify(podcastCrudRepositoryMock)
                .findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock, never())
                .findAllByPodcastId(any(), any())
        }
    }

    @Nested
    inner class StoreNewPodcastHappyPath {
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

    @Nested
    inner class UpdateExistingPodcastHappyPath {
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
            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock).save(podcast)
        }
    }

    @Nested
    inner class UpdateExistingPodcastErrorScenarios {
        @Test
        fun `Podcast ID Not In DB`() {
            // Mock
            val podcastId = TestObjectsFromFile.podcastData1.podcast.id
            val podcast = TestObjectsFromFile.podcastData1.podcast

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.empty())

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java, { podcastService.updatePodcast(podcastId, podcast) }
                , "Expected an error thrown as there should be no records for ID."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Podcast ID not found in DB", ErrorType.DB001), err)

            Mockito.verify(podcastCrudRepositoryMock)
                .findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock, never())
                .save(podcast)
        }

        @Test
        fun `DB Error On Podcast Save`() {

            // Mock
            val podcastId = TestObjectsFromFile.podcastData1.podcast.id
            val podcast = TestObjectsFromFile.podcastData1.podcast

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(podcast))

            Mockito.`when`(podcastCrudRepositoryMock.save(podcast))
                .thenThrow(DataRetrievalFailureException(""))

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java, { podcastService.updatePodcast(podcastId, podcast) }
                , "Expected an error thrown as an error was thrown while saving podcast data."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Something went wrong!", ErrorType.DB003), err)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock).save(podcast)
        }
    }

    @Nested
    inner class StoreNewPodcastEpisodeHappyPath {
        @Test
        fun `Successfully Store New Episode`() {
            // Mock
            val podcastId = TestObjectsFromFile.podcastData1.podcast.id
            val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
            val mockPodcastData = TestObjectsFromFile.podcastData1
            val mockedPodcast = mockPodcastData.podcast

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(podcastEpisodeCrudRepository.save(podcastEpisode))
                .thenReturn(podcastEpisode)

            // Call
            podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)

            // Assert
            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodeCrudRepository).save(podcastEpisode)
        }
    }

    @Nested
    inner class UpdatePodcastEpisodeHappyPath {
        @Test
        fun `Successfully Update Episode`() {
            // Mock
            val podcastEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
            val mockPodcastData = TestObjectsFromFile.podcastData1
            val podcastId = mockPodcastData.podcast.id
            val mockedPodcast = mockPodcastData.podcast

            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(podcastEpisodeCrudRepository.findById(podcastEpisode.episodeId))
                .thenReturn(Optional.of(podcastEpisode))
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