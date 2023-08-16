import dev.spartan.Dependencies

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("io.github.gradle-nexus.publish-plugin")
  `kotlin-dsl`
  id("sonatype")
}

group = "com.c0x12c.exposed.postgis"
version = "1.0.0"

repositories {
  mavenCentral()
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "java")

  repositories {
    mavenCentral()
  }

  dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    testImplementation(Dependencies.Testing.jupiter)
    testImplementation(Dependencies.Testing.strickt)
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

