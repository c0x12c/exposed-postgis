pluginManagement {
  repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://maven.google.com/")
    gradlePluginPortal()
  }

  plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.8.20")
    id("io.github.gradle-nexus.publish-plugin") version ("1.3.0")
    id("sonatype")
  }
}

rootProject.name = "exposed-postgis"

include("module-core")
includeBuild("plugin-sonatype")
