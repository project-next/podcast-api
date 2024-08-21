val springVersion = "3.3.2"
val mockitKotlinVersion = "1.6.0"
val h2Version = "2.3.232"

dependencies {
	"testImplementation"(kotlin("test"))

	"testImplementation"("com.nhaarman:mockito-kotlin:$mockitKotlinVersion")    // provides helper functions needed for mockito to work in Kotlin
	"testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
	"testImplementation"("org.springframework.security:spring-security-test:6.3.3")

	"testRuntimeOnly"("com.h2database:h2:$h2Version")
}

tasks.withType<Test>  {
	useJUnitPlatform()

	minHeapSize = "256m"
	maxHeapSize = "896m"
	maxParallelForks = Runtime.getRuntime().availableProcessors() / 2

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
				exclude("com/rtomyj/podcast/model/**", "com/rtomyj/podcast/Application**", "com/rtomyj/podcast/util/enum/**", "com/rtomyj/podcast/util/Constants**")
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
				minimum = "0.8".toBigDecimal()
			}
		}

		rule {
			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = "0.5".toBigDecimal()
			}
		}
	}
}