package com.rtomyj.podcast

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.*


@SpringBootApplication
@EnableJpaRepositories
class Application {
	@Value("\${server.timezone}")
	private lateinit var serverTimeZone: String
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
	}

	@PostConstruct
	fun init() {
		// Setting Spring Boot SetTimeZone
		log.info("Configuring API timezone as {}", serverTimeZone)
		TimeZone.setDefault(TimeZone.getTimeZone(serverTimeZone))
	}
}

fun main() {
	SpringApplication.run(Application::class.java)
}