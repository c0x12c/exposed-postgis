import com.c0x12c.Dependencies

dependencies {
  implementation(Dependencies.Database.postgisJdbc)
  implementation(Dependencies.Database.postgresql)

  implementation(Dependencies.Exposed.core)
  implementation(Dependencies.Exposed.dao)
  implementation(Dependencies.Exposed.jdbc)

  testImplementation(Dependencies.Database.hikari)
}
