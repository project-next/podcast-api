package com.rtomyj.podcast.dao

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
	Sql("classpath:sql/drop.sql"),
	Sql("classpath:sql/schema.sql"),
	Sql("classpath:sql/data.sql")
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
				val podcast = dao.getPodcastInfo("41c4e54d-bee9-43c9-b34b-d4eb87c1a377")

				Assertions.assertNotNull(podcast)
			}
		}
	}
}