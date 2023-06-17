package com.c0x12c.exposed.postgis

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.postgis.Geometry
import org.postgis.LineString
import org.postgis.MultiLineString
import org.postgis.MultiPolygon
import org.postgis.Point
import org.postgis.Polygon

inline fun <reified G : Geometry> Table.geometry(name: String): Column<G> {
  return registerColumn(name, GeometryColumnType(G::class))
}

/**
 * Creates a [point] column, with the specified [name], for storing Postgis [Point]
 *
 * @param name The name of the column
 */
fun Table.point(name: String): Column<Point> {
  return geometry(name)
}

/**
 * Creates a polygon column, with the specified [name], for storing Postgis [Polygon]
 *
 * @param name The name of the column
 */
fun Table.polygon(name: String): Column<Polygon> {
  return geometry(name)
}

/**
 * Creates a multi polygon column, with the specified [name], for storing Postgis [MultiPolygon]
 *
 * @param name The name of the column
 */
fun Table.multiPolygon(name: String): Column<MultiPolygon> {
  return geometry(name)
}

/**
 * Creates a line string column, with the specified [name], for storing Postgis [LineString]
 *
 * @param name The name of the column
 */
fun Table.lineString(name: String): Column<LineString> {
  return geometry(name)
}

/**
 * Creates a multi line string column, with the specified [name], for storing Postgis [MultiLineString]
 *
 * @param name The name of the column
 */
fun Table.multiLineString(name: String): Column<MultiLineString> {
  return geometry(name)
}
