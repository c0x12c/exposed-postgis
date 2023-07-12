package dev.spartan.exposed.postgis

import org.postgis.LineString
import org.postgis.MultiLineString
import org.postgis.MultiPolygon
import org.postgis.Point
import org.postgis.Polygon

data class Address(
  val id: String,
  val point: Point,
  val polygon: Polygon,
  val multiPolygon: MultiPolygon,
  val lineString: LineString,
  val multiLineString: MultiLineString,
)

