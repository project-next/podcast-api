val springVersion = "4.0.0"
val mockitKotlinVersion = "1.6.0"
val h2Version = "2.4.240"

dependencies {
    "testImplementation"(kotlin("test"))

    "testImplementation"("com.nhaarman:mockito-kotlin:$mockitKotlinVersion")    // provides helper functions needed for mockito to work in Kotlin

    "testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
    "testImplementation"("org.springframework.boot:spring-boot-starter-jdbc-test:$springVersion")
    "testImplementation"("org.springframework.boot:spring-boot-starter-webmvc-test:$springVersion")

    "testImplementation"("org.springframework.security:spring-security-test:7.0.2")

    "testRuntimeOnly"("com.h2database:h2:$h2Version")
}

tasks.withType<Test> {
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
                exclude(
                    "com/rtomyj/podcast/model/**",
                    "com/rtomyj/podcast/Application**",
                    "com/rtomyj/podcast/util/enum/**",
                    "com/rtomyj/podcast/util/Constants**"
                )
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