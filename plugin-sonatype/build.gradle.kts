plugins {
  // Is needed to turn our build logic written in Kotlin into Gralde Plugin
  `kotlin-dsl`
}

repositories {
  // To use 'maven-publish' and 'signing' plugins in our own plugin
  gradlePluginPortal()
}
