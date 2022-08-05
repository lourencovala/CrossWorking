import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "ps"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// JDBI
	implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.30.0")
	implementation("org.jdbi:jdbi3-spring4:3.19.0")
	implementation("org.jdbi:jdbi3-kotlin:3.28.0")
	implementation("org.jdbi:jdbi3-postgres:3.30.0")

	implementation("com.google.guava:guava:31.1-jre")

	// PostgreSQL
	implementation("org.postgresql:postgresql:42.3.6")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.2")

	// For transactions
	implementation("org.springframework.boot:spring-boot-starter-jdbc")

	// Firebase Admin
	implementation("com.google.firebase:firebase-admin:9.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


