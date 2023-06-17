package com.c0x12c.exposed.postgis

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties
import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.postgis.LineString
import org.postgis.MultiLineString
import org.postgis.MultiPolygon
import org.postgis.PGbox2d
import org.postgis.PGbox3d
import org.postgis.PGgeometry
import org.postgis.Point
import org.postgis.Polygon
import org.postgresql.PGConnection
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class PostgisExtensionTest {

  companion object {

    private val tableName = AddressTable.tableName

    @Language("PostgreSQL")
    private val SQL_CREATE = """
      CREATE TABLE IF NOT EXISTS $tableName
      (
          id                TEXT NOT NULL PRIMARY KEY,
          point             geometry(Point, 4326),
          polygon           geometry(Polygon, 4326),
          multi_polygon     geometry(MultiPolygon, 4326),
          line_string       geometry(LineString, 4326),
          multi_line_string geometry(MultiLineString, 4326)
      );
    """.trimIndent()

    @Language("PostgreSQL")
    private val SQL_DROP = """
      DROP TABLE IF EXISTS $tableName;
    """.trimIndent()

    private val database by lazy {
      val config = HikariConfig(Properties().apply {
        setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        setProperty("dataSource.user", "test")
        setProperty("dataSource.password", "test")
        setProperty("dataSource.databaseName", "test")
        setProperty("dataSource.serverName", "localhost")
        setProperty("dataSource.portNumber", "5432")
      })
      val source = HikariDataSource(config).apply {
        connection.use { c ->
          c.unwrap(PGConnection::class.java).apply {
            addDataType("geometry", PGgeometry::class.java)
            addDataType("box3d", PGbox3d::class.java)
            addDataType("box2d", PGbox2d::class.java)
          }
        }
      }
      Database.connect(datasource = source)
    }

    private val dao by lazy {
      AddressDao(database)
    }

    private val address = Address(
      id = "0",
      point = Point(1.0, 2.0).apply {
        srid = 4326
      },
      polygon = Polygon("POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))").apply {
        srid = 4326
      },
      multiPolygon = MultiPolygon("MULTIPOLYGON(((0 0, 0 1, 1 1, 1 0, 0 0)))").apply {
        srid = 4326
      },
      lineString = LineString("LINESTRING(0 0, 0 1, 1 1, 1 0, 0 0)").apply {
        srid = 4326
      },
      multiLineString = MultiLineString("MULTILINESTRING((0 0, 0 1, 1 1, 1 0, 0 0))").apply {
        srid = 4326
      }
    )
  }

  @BeforeEach
  fun beforeEach() {
    execute(SQL_CREATE)
  }

  @AfterEach
  fun afterEach() {
    execute(SQL_DROP)
  }

  @Test
  fun `insert and verify result with geometry`() {
    val result = dao.insert(address)
    expectThat(result).isEqualTo(address)
  }

  @Test
  fun `insert and query`() {
    dao.insert(address)
    val result = dao.byId("0")
    expectThat(result).isEqualTo(address)
  }

  @Test
  fun `intersect test`() {
    dao.insert(address)
    expectThat(dao.intersect(Point(0.0, 0.0).apply { srid = 4326 }))
      .isEqualTo(true)
    expectThat(dao.intersect(Point(100.0, 100.0).apply { srid = 4326 }))
      .isEqualTo(false)
  }

  @Test
  fun `overlap test`() {
    dao.insert(address)
    expectThat(dao.overlap(Point(100.0, 100.0).apply { srid = 4326 }))
      .isEqualTo(false)
  }

  @Test
  fun `contain test`() {
    dao.insert(address)
    expectThat(dao.contain(Point(0.5, 0.5).apply { srid = 4326 }))
      .isEqualTo(true)
    expectThat(dao.contain(Point(100.0, 100.0).apply { srid = 4326 }))
      .isEqualTo(false)
  }

  private fun execute(sql: String) {
    transaction(database) {
      val conn = TransactionManager.current().connection
      val statement = conn.prepareStatement(sql, false)
      statement.executeUpdate()
    }
  }
}
