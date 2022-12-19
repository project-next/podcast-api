val springVersion = "3.0.0"
val mockitKotlinVersion = "1.6.0"

dependencies {
	"testImplementation"(kotlin("test"))

	"testImplementation"("com.nhaarman:mockito-kotlin:$mockitKotlinVersion")    // provides helper functions needed for mockito to work in Kotlin
	"testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
	"testImplementation"("org.springframework.security:spring-security-test:6.0.0")
}


tasks.withType<Test> {
	useJUnitPlatform()

	minHeapSize = "256m"
	maxHeapSize = "896m"
	maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 ?: 1
}