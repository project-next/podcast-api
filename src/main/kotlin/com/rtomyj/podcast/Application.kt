package com.rtomyj.podcast

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.*


@SpringBootApplication
@EnableJpaRepositories
class Application {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)
		private const val timeZone = "America/Chicago"
	}

	@PostConstruct
	fun init() {
		// Setting Spring Boot SetTimeZone
		log.info("Configuring API timezone as {}", timeZone)
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone))
	}
}

fun main() {
	SpringApplication.run(Application::class.java)
}