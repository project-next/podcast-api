import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val romeToolsVersion = "1.18.0"
val springBootVersion = "3.0.2"
val jacksonDatabindVersion = "2.14.2"
val jacksonKotlinVersion = "2.14.2"
val jacksonCoreVersion = "2.14.2"
val kotlinVersion = "1.7.22"
val postgresqlVersion = "42.5.2"
val slf4jVersion = "2.0.6"
val guavaVersion = "31.1-jre"

val archivesBaseName = "podcast-api"


plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	id("info.solidsoft.pitest") version "1.9.11"
	id("com.adarshr.test-logger") version "3.2.0"	// printing for JUnits
//	id("org.graalvm.buildtools.native") version "0.9.18" // - native

	kotlin("jvm") version "1.8.10"
	kotlin("plugin.spring") version "1.8.0"

	jacoco
}


group = "com.rtomyj.next"
version = "1.3.0"
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")    // needed for @Validated to work
	runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")

	implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")

	implementation("org.springframework.boot:spring-boot-starter-jetty:$springBootVersion")
	runtimeOnly("org.eclipse.jetty:jetty-alpn-java-server")
	runtimeOnly("org.eclipse.jetty.http2:http2-server")

	implementation("org.slf4j:slf4j-api:$slf4jVersion")

	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonDatabindVersion")
	implementation("com.fasterxml.jackson.core:jackson-core:$jacksonCoreVersion")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")

	// below are needed for native???
//	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("com.rometools:rome:$romeToolsVersion")
	implementation("com.rometools:rome-modules:$romeToolsVersion")

	runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")

	implementation("com.google.guava:guava:$guavaVersion")
}

configurations {
	implementation {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
		exclude(module = "spring-boot-starter-tomcat")
		exclude(group = "org.apache.tomcat")
		exclude(group = "junit")
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

apply(from = "gradle/unitTest.gradle.kts")

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
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

	withType<Javadoc> {
		options.memberLevel = JavadocMemberLevel.PRIVATE
		source = sourceSets["main"].allJava
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

		dependsOn(bootJar)

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

jacoco {
	toolVersion = "0.8.8"
}