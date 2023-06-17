pluginManagement {
  repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://maven.google.com/")
    gradlePluginPortal()
  }

  plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.8.20")
  }
}

rootProject.name = "spartan-exposed-postgis"

include(
  "module-core",
  "module-test"
)
