package com.c0x12c

object Dependencies {

  object Kotlin {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
  }

  object Database {
    const val postgresql = "org.postgresql:postgresql:${Versions.postgresql}"
    const val postgisJdbc = "net.postgis:postgis-jdbc:${Versions.postgis}"
    const val hikari = "com.zaxxer:HikariCP:${Versions.hikari}"
  }

  object Exposed {
    const val core = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
    const val jdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
    const val dao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
  }

  object Testing {
    const val jupiter = "org.junit.jupiter:junit-jupiter:${Versions.junit5}"
    const val mockk = "io.mockk:mockk:1.12.4"
    const val strickt = "io.strikt:strikt-jvm:0.34.0"
  }
}
