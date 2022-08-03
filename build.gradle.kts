import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val romeToolsVersion = "1.18.0"
val springBootVersion = "2.7.2"
val jacksonVersion = "2.13.3"
val kotlinVersion = "1.7.10"
val postgresqlVersion = "42.4.1"

val archivesBaseName = "podcast-api"


plugins {
	id("org.springframework.boot") version "2.7.2"
	id("io.spring.dependency-management") version "1.0.12.RELEASE"
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.7.10"
}


group = "com.rtomyj.next"
version = "1.2.0"
java.sourceCompatibility = JavaVersion.VERSION_16


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


tasks {
	withType<Test> {
		useJUnitPlatform()
	}

	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_16.toString()
		}
	}

	withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
		group = "Build"
		description = "Creates a JAR bundled by Spring plugin"


		baseName = archivesBaseName
		manifest.attributes.apply {
			put("Implementation-Title", archivesBaseName)
		}
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