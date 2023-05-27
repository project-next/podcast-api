package com.rtomyj.podcast.dao

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.TestConstants
import com.rtomyj.podcast.util.TestObjectsFromFile
import com.rtomyj.podcast.util.enum.ErrorType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [JdbcDao::class])
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@JdbcTest
@ActiveProfiles("test") // Loading test props with H2 in memory DB configurations
@SqlGroup(
	Sql("classpath:sql/drop.sql"), Sql("classpath:sql/schema.sql"), Sql("classpath:sql/data.sql")
)
@Tag("Dao")
class JdbcDaoTest {
	@Autowired
	private lateinit var dao: Dao

	@Nested
	inner class StoreEpisode {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Store Episode`() {
				// Mock
				val sampleEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val episode = PodcastEpisode(podcastId = TestConstants.PODCAST_ID_FROM_SQL_QUERY).apply {
					this.title = sampleEpisode.title
					this.episodeWebpageLink = sampleEpisode.episodeWebpageLink
					this.episodeAudioLink = sampleEpisode.episodeAudioLink
					this.description = sampleEpisode.description
					this.publicationDate = sampleEpisode.publicationDate
					this.author = sampleEpisode.author
					this.imageLink = sampleEpisode.imageLink
					this.keywords = sampleEpisode.keywords
					this.length = sampleEpisode.length
					this.mediaType = sampleEpisode.mediaType
					this.isExplicit = sampleEpisode.isExplicit
					this.duration = sampleEpisode.duration
				}

				// Call
				dao.storeNewPodcastEpisode(episode, "")
			}
		}

		@Nested
		inner class DatabaseErrors {
			@Test
			fun `Data Integrity Error`() {
				// Mock
				val episode =
					TestObjectsFromFile.podcastData1.podcastEpisodes[0]   // this test data has a podcast ID that doesn't currently exist in podcast table, therefore it will throw an integrity error on the foreign key

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ dao.storeNewPodcastEpisode(episode, "") },
					"Trying to store a record with integrity issue - as such an exception should be thrown"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals(PodcastException(JdbcDao.DATA_CONSTRAINT_ISSUE, ErrorType.DB002), err)
			}
		}
	}

	@Nested
	inner class UpdateEpisode {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Update Existing Episode`() {
				// Mock
				val sampleEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val episode = PodcastEpisode(
					podcastId = TestConstants.PODCAST_ID_FROM_SQL_QUERY, episodeId = TestConstants.EPISODE_ID_FROM_SQL_QUERY
				).apply {  // using mock object value but will use mock DB IDs
					this.title = sampleEpisode.title
					this.episodeWebpageLink = sampleEpisode.episodeWebpageLink
					this.episodeAudioLink = sampleEpisode.episodeAudioLink
					this.description = sampleEpisode.description
					this.publicationDate = sampleEpisode.publicationDate
					this.author = sampleEpisode.author
					this.imageLink = sampleEpisode.imageLink
					this.keywords = sampleEpisode.keywords
					this.length = sampleEpisode.length
					this.mediaType = sampleEpisode.mediaType
					this.isExplicit = sampleEpisode.isExplicit
					this.duration = sampleEpisode.duration
				}

				// Call
				dao.updatePodcastEpisode(episode, "")
			}
		}

		@Nested
		inner class UserError {
			@Test
			fun `User Tries To Update An Episode That DNE`() {
				// Mock
				val episode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]   // IDs found in this object DNE in mock DB

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ dao.updatePodcastEpisode(episode, "") },
					"Trying to update a podcast episode using an ID that DNE exist in DB - since no rows are effected, DAO will throw an exception"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals(PodcastException(JdbcDao.NO_ROWS_UPDATED, ErrorType.DB004), err)
			}
		}

		@Nested
		inner class DatabaseErrors {
			@Test
			fun `Data Integrity Error`() {
				// Mock
				val sampleEpisode = TestObjectsFromFile.podcastData1.podcastEpisodes[0]
				val episode = PodcastEpisode(
					podcastId = TestConstants.PODCAST_ID_FROM_SQL_QUERY, episodeId = TestConstants.EPISODE_ID_FROM_SQL_QUERY
				).apply {  // using mock object value but will use mock DB IDs
					this.title = sampleEpisode.title
					this.episodeWebpageLink = sampleEpisode.episodeWebpageLink
					this.episodeAudioLink = sampleEpisode.episodeAudioLink
					this.description = sampleEpisode.description
					this.publicationDate = sampleEpisode.publicationDate
					this.author = sampleEpisode.author
					this.imageLink = sampleEpisode.imageLink
					this.keywords = sampleEpisode.keywords
					this.length = sampleEpisode.length
					this.mediaType = "SOMETHING THAT WILL TRIGGER AN INTEGRITY ISSUE"    // triggers integrity issue
					this.isExplicit = sampleEpisode.isExplicit
					this.duration = sampleEpisode.duration
				}

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java, {
						dao.updatePodcastEpisode(episode, "")
					}, "Trying to update a record with integrity issue - as such an exception should be thrown"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals(PodcastException(JdbcDao.DATA_CONSTRAINT_ISSUE, ErrorType.DB002), err)
			}
		}
	}
}
