package com.rtomyj.podcast.config

import com.rtomyj.podcast.util.constant.Generic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
	var accessDeniedHandler: RestAccessDeniedHandler,
	val authenticationEntryPoint: RestAuthenticationEntryPoint,
	@Value("\${auth.admin.username}") val adminUsername: String,
	@Value("\${auth.admin.password}") val adminPassword: String,
	@Value("\${auth.generic-user.username}") val genericUserUsername: String,
	@Value("\${auth.generic-user.password}") val genericUserPassword: String
) {
	@Bean
	@Throws(Exception::class)
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, Generic.PODCAST_URI).hasRole("ADMIN").and().httpBasic().and().csrf().disable()
		http.authorizeHttpRequests().requestMatchers(HttpMethod.PUT, Generic.PODCAST_URI).hasRole("ADMIN").and().httpBasic().and().csrf().disable()
		http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, Generic.PODCAST_URI).permitAll().and().httpBasic().and().csrf().disable()

		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
		return http.build()
	}

	@Bean
	fun userDetailsService(): UserDetailsService {
		val manager = InMemoryUserDetailsManager()
		val adminEncodedPassword: String = passwordEncoder().encode(adminPassword)
		val genericUserEncodedPassword: String = passwordEncoder().encode(genericUserPassword)

		manager.createUser(
			User.withUsername(adminUsername).password(adminEncodedPassword).roles("ADMIN").build()
		)
		manager.createUser(
			User.withUsername(genericUserUsername).password(genericUserEncodedPassword).roles("USER").build()
		)
		return manager
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}
}