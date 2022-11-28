import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val romeToolsVersion = "1.18.0"
val springBootVersion = "3.0.0"
val jacksonVersion = "2.14.1"
val kotlinVersion = "1.7.22"
val postgresqlVersion = "42.5.1"

val archivesBaseName = "podcast-api"


plugins {
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.0"
	id("info.solidsoft.pitest") version "1.9.11"
	id("com.adarshr.test-logger") version "3.2.0"	// printing for JUnits

	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"

	jacoco
}


group = "com.rtomyj.next"
version = "1.2.0"
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

	implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

	implementation("com.rometools:rome:$romeToolsVersion")
	implementation("com.rometools:rome-modules:$romeToolsVersion")

	runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

apply(from = "gradle/unitTest.gradle.kts")

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_17.toString()
		}
	}

	withType<BootJar> {
		group = "Build"
		description = "Creates a JAR bundled by Spring plugin"

		manifest.attributes.apply {
			put("Implementation-Title", archivesBaseName)
			put("Implementation-Version", project.version)
		}
	}

	getByName<Jar>("jar") {
		enabled = false	// Spring Boot > 2.5.x will create two JARs (one which is useless) unless this is disabled
	}

	create("bootJarPath")  {
		group = "Project Info"
		description = "Specifies the absolute path of the JAR created by the bootJar task."

		doFirst {
			println("$buildDir/libs/$archivesBaseName-${project.version}.jar")
		}
	}

	register("createDockerJar", Copy::class) {
		description = "Renames JAR (removes version number) which makes it easier to deploy via Docker"
		group = "Util"

		from("${buildDir}/libs/${archivesBaseName}-${project.version}.jar")
		into("${buildDir}/libs")

		rename ("${archivesBaseName}-${project.version}.jar", "${archivesBaseName}.jar")
	}
}

pitest {
	targetClasses.set(listOf("com.rtomyj.podcast.api.*"))
	excludedClasses.set(listOf("com.rtomyj.podcast.api.model.*", "com.rtomyj.podcast.api.constant.*"
		, "com.rtomyj.podcast.api.exception.*", "com.rtomyj.podcast.api.enum.*"))

	threads.set(Runtime.getRuntime().availableProcessors() - 2)
	outputFormats.set(listOf("XML", "HTML"))
	timestampedReports.set(false)
	junit5PluginVersion.set("1.0.0")
	pitestVersion.set("1.9.4")

	mutators.set(listOf("STRONGER"))

	avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}