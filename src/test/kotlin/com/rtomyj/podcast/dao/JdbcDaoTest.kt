package com.rtomyj.podcast.dao

import com.rtomyj.podcast.exception.PodcastException
import com.rtomyj.podcast.model.Podcast
import com.rtomyj.podcast.model.PodcastEpisode
import com.rtomyj.podcast.util.TestConstants
import com.rtomyj.podcast.util.TestObjectsFromFile
import com.rtomyj.podcast.util.enum.ErrorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
	inner class RetrievePodcastInfo {
		@Nested
		inner class HappyPath {
			@Test
			fun `Retrieve Podcast Info From DB`() {
				// Call
				val podcast = dao.getPodcastInfo(TestConstants.PODCAST_ID_FROM_SQL_QUERY)

				// Assert
				Assertions.assertNotNull(podcast)
			}
		}

		@Nested
		inner class DatabaseErrors {
			@Test
			fun `Podcast ID Does Not Exist In DB`() {
				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java, { dao.getPodcastInfo("INVALID") }, "Expected an error thrown as there should be no records for ID."
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals(PodcastException("Podcast not found in DB", ErrorType.DB001), err)
			}
		}
	}

	@Nested
	inner class RetrieveEpisodes {
		@Nested
		inner class HappyPath {
			@Test
			fun `Retrieve Episodes From DB`() {
				// Call
				val episodes = dao.getPodcastEpisodes(TestConstants.PODCAST_ID_FROM_SQL_QUERY)

				// Assert
				Assertions.assertNotNull(episodes)
				Assertions.assertEquals(1, episodes.size)
			}
		}
	}

	@Nested
	inner class StorePodcast {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Store New Podcast`() {
				// Mock
				val podcast = TestObjectsFromFile.podcastData1.podcast

				// Call
				dao.storeNewPodcast(podcast)
			}
		}

		@Nested
		inner class DatabaseErrors {
			@Test
			fun `Data Integrity Error`() {
				// Mock
				val podcast = Podcast(TestObjectsFromFile.podcastData1.podcast.id + "longer!!!!!!!!!!!").apply {
					this.title = TestObjectsFromFile.podcastData1.podcast.title
					this.link = TestObjectsFromFile.podcastData1.podcast.link
					this.description = TestObjectsFromFile.podcastData1.podcast.description
					this.language = TestObjectsFromFile.podcastData1.podcast.language
					this.copyright = TestObjectsFromFile.podcastData1.podcast.copyright
					this.lastBuildDate = TestObjectsFromFile.podcastData1.podcast.lastBuildDate
					this.email = TestObjectsFromFile.podcastData1.podcast.email
					this.category = TestObjectsFromFile.podcastData1.podcast.category
					this.author = TestObjectsFromFile.podcastData1.podcast.author
					this.imageUrl = TestObjectsFromFile.podcastData1.podcast.imageUrl
				}

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java, { dao.storeNewPodcast(podcast) }, "Trying to store a record with integrity issue - as such an exception should be thrown"
				)

				// Assert
				Assertions.assertNotNull(err)
				Assertions.assertEquals(PodcastException(JdbcDao.DATA_CONSTRAINT_ISSUE, ErrorType.DB002), err)
			}
		}
	}

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
					this.link = sampleEpisode.link
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
	inner class UpdatePodcast {
		@Nested
		inner class HappyPath {
			@Test
			fun `Successfully Update Existing Podcast`() {
				// Mock
				val podcast = TestObjectsFromFile.podcastData1.podcast  // this object has values different from what is currently in mock DB

				// Call
				dao.updatePodcast(TestConstants.PODCAST_ID_FROM_SQL_QUERY, podcast)
			}
		}

		@Nested
		inner class UserError {
			@Test
			fun `User Tries To Update A Podcast That DNE`() {
				// Mock
				val podcast = TestObjectsFromFile.podcastData1.podcast  // this object has values different from what is currently in mock DB

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ dao.updatePodcast(podcast.id, podcast) },
					"Trying to update a podcast using an ID that DNE exist in DB - since no rows are effected, DAO will throw an exception"
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
				val podcast = Podcast(TestConstants.PODCAST_ID_FROM_SQL_QUERY).apply {
					this.title = TestObjectsFromFile.podcastData1.podcast.title
					this.link = TestObjectsFromFile.podcastData1.podcast.link
					this.description = TestObjectsFromFile.podcastData1.podcast.description
					this.language = "SOMETHING THAT WILL TRIGGER AN INTEGRITY ISSUE"    // triggers integrity issue
					this.copyright = TestObjectsFromFile.podcastData1.podcast.copyright
					this.email = TestObjectsFromFile.podcastData1.podcast.email
					this.category = TestObjectsFromFile.podcastData1.podcast.category
					this.author = TestObjectsFromFile.podcastData1.podcast.author
					this.imageUrl = TestObjectsFromFile.podcastData1.podcast.imageUrl
				}

				// Call
				val err = Assertions.assertThrows(
					PodcastException::class.java,
					{ dao.updatePodcast(TestConstants.PODCAST_ID_FROM_SQL_QUERY, podcast) },
					"Trying to update a record with integrity issue - as such an exception should be thrown"
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
					this.link = sampleEpisode.link
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
					this.link = sampleEpisode.link
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
