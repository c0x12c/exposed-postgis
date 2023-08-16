import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.*

plugins {
  `maven-publish`
  signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
  secretPropsFile.reader().use {
    Properties().apply {
      load(it)
    }
  }.onEach { (name, value) ->
    ext[name.toString()] = value
  }
} else {
  ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
  ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
  ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
  ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
  ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
  archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
  repositories {
    maven {
      name = "sonatype"
      setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
      credentials {
        username = getExtraString("ossrhUsername")
        password = getExtraString("ossrhPassword")
      }
    }
  }

  publishing {
    repositories {
      maven {
        name = "sonatype"
        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = getExtraString("ossrhUsername")
          password = getExtraString("ossrhPassword")
        }
      }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

      // Stub javadoc.jar artifact
      artifact(javadocJar.get())

      // Provide artifacts information requited by Maven Central
      pom {
        name.set("exposed-postgis")
        description.set("Exposed SQL PostGIS extension")
        url.set("https://github.com/c0x12c/exposed-postgis")

        licenses {
          license {
            name.set("Apache License, Version 2.0")
          }
        }

        developers {
          developer {
            id.set("spartan-dev")
            name.set("Spartan Dev")
            email.set("chan@c0x12c.com")
          }
        }

        scm {
          connection.set("scm:git://git@github.com:c0x12c/exposed-postgis.git")
          url.set("https://github.com/c0x12c/exposed-postgis.git")
        }
      }
    }
  }
}

signing {
  sign(publishing.publications)
}
