package dev.spartan.exposed.postgis

import org.jetbrains.exposed.sql.Table

object AddressTable : Table("address") {
  val id = text("id")
  val point = point("point")
  val polygon = polygon("polygon")
  val multiPolygon = multiPolygon("multi_polygon")
  val lineString = lineString("line_string")
  val multiLineString = multiLineString("multi_line_string")
}
