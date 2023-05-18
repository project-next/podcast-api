val springVersion = "3.0.7"
val mockitKotlinVersion = "1.6.0"
val h2Version = "2.1.214"

dependencies {
	"testImplementation"(kotlin("test"))

	"testImplementation"("com.nhaarman:mockito-kotlin:$mockitKotlinVersion")    // provides helper functions needed for mockito to work in Kotlin
	"testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
	"testImplementation"("org.springframework.security:spring-security-test:6.1.0")

	"testRuntimeOnly"("com.h2database:h2:$h2Version")
}

tasks.withType<Test>  {
	useJUnitPlatform()

	minHeapSize = "256m"
	maxHeapSize = "896m"
	maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 ?: 1

	finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
	dependsOn(tasks.withType<Test>())

	reports {
		xml.required.set(false)
		csv.required.set(false)
	}

	afterEvaluate {
		classDirectories.setFrom(classDirectories.files.map {
			fileTree(it).matching {
				exclude("com/rtomyj/podcast/model/**", "com/rtomyj/podcast/Application.kt", "com/rtomyj/podcast/util/enum/**", "com/rtomyj/podcast/util/constant/**")
			}
		})
	}

	finalizedBy(tasks.withType<JacocoCoverageVerification>())
}

tasks.withType<JacocoCoverageVerification> {
	violationRules {
		rule {
			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = "0.2".toBigDecimal()
			}
		}

		rule {
			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = "0.1".toBigDecimal()
			}
		}
	}
}