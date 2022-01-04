import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val romeToolsVersion = "1.18.0"
val springBootVersion = "2.6.2"
val jacksonVersion = "2.13.1"
val kotlinVersion = "1.6.10"
val postgresqlVersion = "42.3.1"

val archivesBaseName = "Podcast-API"


plugins {
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}


group = "com.rtomyj"
version = "1.1.0"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

	implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	implementation("com.rometools:rome:$romeToolsVersion")
	implementation("com.rometools:rome-modules:$romeToolsVersion")

	runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
}


tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = JavaVersion.VERSION_11.toString()
	}
}


tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	group = "Build"
	description = "Creates a JAR bundled by Spring plugin"


	baseName = archivesBaseName
	manifest.attributes.apply {
		put("Implementation-Title", archivesBaseName)
	}
}


tasks.create("bootJarPath")  {
	group = "Project Info"
	description = "Specifies the absolute path of the JAR created by the bootJar task."

	doFirst {
		println("$buildDir/libs/$archivesBaseName-${project.version}.jar")
	}
}
