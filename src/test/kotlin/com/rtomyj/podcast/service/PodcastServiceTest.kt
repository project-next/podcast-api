package com.rtomyj.podcast.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.rtomyj.podcast.dao.PodcastCrudRepository
import com.rtomyj.podcast.dao.PodcastEpisodePagingAndSortingRepository
import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.util.TestObjectsFromFile
import com.rtomyj.podcast.util.enum.ErrorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PodcastService::class])
@Tag("Service")
class PodcastServiceTest {
    @MockitoBean
    private lateinit var podcastCrudRepositoryMock: PodcastCrudRepository

    @MockitoBean
    private lateinit var podcastEpisodePagingAndSortingRepositoryMock: PodcastEpisodePagingAndSortingRepository

    @Autowired
    private lateinit var podcastService: PodcastService

    companion object {
        val mockPodcastData = TestObjectsFromFile.podcastData1
        val podcastId = mockPodcastData.id
        val mockedPodcast = mockPodcastData
        val podcastEpisode = mockPodcastData.episodes[0]
    }

    @Nested
    inner class PodcastRSSFeedRetrievalHappyPath {
        @Test
        fun `Successfully Retrieve Feed`() {
            // mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))

            // Call
            val feed = podcastService.getRssFeedForPodcast(podcastId)

            // Assert
            Assertions.assertNotNull(feed)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
        }
    }

    @Nested
    inner class PodcastInfoRetrievalHappyPath {
        @Test
        fun `Successfully Retrieve Podcast Info`() {
            // mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))

            // Call
            val data = podcastService.getPodcastData(podcastId)

            // Assert
            Assertions.assertNotNull(data)
            Assertions.assertEquals(mockPodcastData, data)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
        }
    }

    @Nested
    inner class PodcastInfoRetrievalErrorScenarios {
        @Test
        fun `Podcast ID Not In DB`() {
            // mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.empty())

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java,
                { podcastService.getPodcastData(podcastId) },
                "Expected an error thrown as there should be no records for ID."
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
            val podcast = TestObjectsFromFile.podcastData1

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
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(mockedPodcast))

            Mockito.`when`(podcastCrudRepositoryMock.save(mockedPodcast))
                .thenReturn(mockedPodcast)

            // Call
            podcastService.updatePodcast(podcastId, mockedPodcast)

            // Assert
            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock).save(mockedPodcast)
        }
    }

    @Nested
    inner class UpdateExistingPodcastErrorScenarios {
        @Test
        fun `Podcast ID Not In DB`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.empty())

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java,
                { podcastService.updatePodcast(podcastId, mockedPodcast) },
                "Expected an error thrown as there should be no records for ID."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Podcast ID not found in DB", ErrorType.DB001), err)

            Mockito.verify(podcastCrudRepositoryMock)
                .findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock, never())
                .save(mockedPodcast)
        }

        @Test
        fun `DB Error On Podcast Save`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(mockedPodcast))

            Mockito.`when`(podcastCrudRepositoryMock.save(mockedPodcast))
                .thenThrow(DataRetrievalFailureException(""))

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java,
                { podcastService.updatePodcast(podcastId, mockedPodcast) },
                "Expected an error thrown as an error was thrown while saving podcast data."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Something went wrong!", ErrorType.DB003), err)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastCrudRepositoryMock).save(mockedPodcast)
        }
    }

    @Nested
    inner class StoreNewPodcastEpisodeHappyPath {
        @Test
        fun `Successfully Store New Episode`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.save(podcastEpisode))
                .thenReturn(podcastEpisode)

            // Call
            podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode)

            // Assert
            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).save(podcastEpisode)
        }
    }

    @Nested
    inner class StoreNewPodcastEpisodeErrorScenarios {
        @Test
        fun `Podcast ID Does Not Exist In DB`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.empty())

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java,
                { podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode) },
                "Expected an error thrown as there should be no records for Podcast ID."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Podcast ID not found in DB", ErrorType.DB001), err)

            Mockito.verify(podcastCrudRepositoryMock)
                .findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock, never())
                .save(podcastEpisode)
        }

        @Test
        fun `DB Error On Episode Save`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId))
                .thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.save(podcastEpisode))
                .thenThrow(DataRetrievalFailureException(""))

            // Call
            val err = Assertions.assertThrows(
                PodcastException::class.java,
                { podcastService.storeNewPodcastEpisode(podcastId, podcastEpisode) },
                "Expected an error thrown as mocks will cause SQL Error."
            )

            // Assert
            Assertions.assertNotNull(err)
            Assertions.assertEquals(PodcastException("Something went wrong!", ErrorType.DB003), err)

            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).save(podcastEpisode)
        }
    }

    @Nested
    inner class UpdatePodcastEpisodeHappyPath {
        @Test
        fun `Successfully Update Episode`() {
            // Mock
            Mockito.`when`(podcastCrudRepositoryMock.findById(podcastId)).thenReturn(Optional.of(mockedPodcast))
            Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.findById(podcastEpisode.episodeId))
                .thenReturn(Optional.of(podcastEpisode))
            Mockito.`when`(podcastEpisodePagingAndSortingRepositoryMock.save(podcastEpisode))
                .thenReturn(podcastEpisode)

            // Call
            podcastService.updatePodcastEpisode(podcastEpisode.episodeId, podcastEpisode)

            // Assert
            Mockito.verify(podcastCrudRepositoryMock).findById(podcastId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).findById(podcastEpisode.episodeId)
            Mockito.verify(podcastEpisodePagingAndSortingRepositoryMock).save(podcastEpisode)
        }
    }
}