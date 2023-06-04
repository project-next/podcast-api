package com.rtomyj.podcast.exception

import com.rtomyj.podcast.util.enum.ErrorType
import org.hibernate.exception.ConstraintViolationException
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.SQLException
import kotlin.test.assertEquals

class PodcastExceptionAdviceTest {
    companion object {
        val podcastExceptionAdvice = PodcastExceptionAdvice()
    }

    @Test
    fun `Podcast Exception Advice Test`() {
        val podcastError = PodcastError(ErrorType.DB003.error, ErrorType.DB003.name)
        assertEquals(ResponseEntity(podcastError, HttpStatus.INTERNAL_SERVER_ERROR)
            , podcastExceptionAdvice.onPodcastException(PodcastException("Something went wrong", ErrorType.DB003)))
    }

    @Test
    fun `Data Integrity Exception Advice Test`() {
        val dbError = DataIntegrityViolationException("Error", ConstraintViolationException("", SQLException(), ""))
        assertEquals(PodcastError(ErrorType.DB002.error, ErrorType.DB002.name)
            , podcastExceptionAdvice.onDBDataIntegrityError(dbError))
    }
}