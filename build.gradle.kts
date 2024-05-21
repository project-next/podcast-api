import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

// main
val romeToolsVersion = "2.1.0"
val springBootVersion = "3.2.5"
val jacksonKotlinVersion = "2.17.1"
val jacksonCoreVersion = "2.17.1"
val snakeYamlVersion = "2.2"
val kotlinVersion = "1.7.22"
val postgresqlVersion = "42.7.3"
val slf4jVersion = "2.0.13"
val jCacheVersion = "6.5.2.Final"
val ehCacheVersion = "3.10.8"

val archivesBaseName = "podcast-api"

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.5"
    id("info.solidsoft.pitest") version "1.15.0"
    id("com.adarshr.test-logger") version "4.0.0"    // printing for JUnits

    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"

    jacoco
}


group = "com.rtomyj.next"
version = "1.6.4"
java.sourceCompatibility = JavaVersion.VERSION_21


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")    // needed for @Validated to work
    runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-undertow:$springBootVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonCoreVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")
    implementation("org.yaml:snakeyaml:$snakeYamlVersion")

    implementation("com.rometools:rome:$romeToolsVersion")
    implementation("com.rometools:rome-modules:$romeToolsVersion")

    runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")

    // below are needed for Hibernate L2 caching - https://www.baeldung.com/hibernate-second-level-cache
    runtimeOnly("org.hibernate.orm:hibernate-jcache:$jCacheVersion")
    runtimeOnly("org.ehcache:ehcache:$ehCacheVersion")
}

configurations {
    all {
        resolutionStrategy.eachDependency {
            if (this.requested.group == ("com.fasterxml.jackson.core")) {
                this.useVersion(jacksonCoreVersion)
            }
        }
    }

    implementation {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
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
            jvmTarget = JavaVersion.VERSION_21.toString()
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
        enabled = false    // Spring Boot > 2.5.x will create two JARs (one which is useless) unless this is disabled
    }

    create("bootJarPath") {
        group = "Project Info"
        description = "Specifies the absolute path of the JAR created by the bootJar task."

        doFirst {
            println("${layout.buildDirectory.get()}/libs/$archivesBaseName-${project.version}.jar")
        }
    }

    register("createDockerJar", Copy::class) {
        description = "Renames JAR (removes version number) which makes it easier to deploy via Docker"
        group = "Util"

        dependsOn(bootJar)

        from("${layout.buildDirectory.get()}/libs/${archivesBaseName}-${project.version}.jar")
        into("${layout.buildDirectory.get()}/libs")

        rename("${archivesBaseName}-${project.version}.jar", "${archivesBaseName}.jar")
    }
}

pitest {
    targetClasses.set(listOf("com.rtomyj.podcast.*"))
    excludedClasses.set(
        listOf(
            "com.rtomyj.podcast.model.*",
            "com.rtomyj.podcast.Application*",
            "com.rtomyj.podcast.util.enum.*",
            "com.rtomyj.podcast.constant.Constants*"
        )
    )

    threads.set(Runtime.getRuntime().availableProcessors() - 2)
    outputFormats.set(listOf("XML", "HTML"))
    timestampedReports.set(false)
    junit5PluginVersion.set("1.1.2")

    mutators.set(listOf("STRONGER"))

    avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}

jacoco {
    toolVersion = "0.8.12"
}