import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.c0x12c.Dependencies

plugins {
  id("org.jetbrains.kotlin.jvm")
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
    maven(url = "https://maven.google.com/")
  }

  kotlin {
    jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "17"
      suppressWarnings = true
    }
  }

  dependencies {
    implementation(Dependencies.Kotlin.stdlib)

    testImplementation(Dependencies.Testing.jupiter)
    testImplementation(Dependencies.Testing.mockk)
    testImplementation(Dependencies.Testing.strickt)
  }

  tasks {
    test {
      useJUnitPlatform()
    }
  }
}

