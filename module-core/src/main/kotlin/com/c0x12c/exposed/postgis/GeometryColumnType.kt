package com.c0x12c.exposed.postgis

import kotlin.reflect.KClass
import org.jetbrains.exposed.sql.ColumnType
import org.postgis.Geometry
import org.postgis.PGgeometry
import org.postgresql.util.PGobject

/**
 * A column type for storing Postgis [Geometry] objects
 *
 * @param G The type of [Geometry] to store
 * @param clazz The class of type [G]
 */
class GeometryColumnType<G : Geometry>(
  private val clazz: KClass<G>
) : ColumnType() {

  override fun sqlType() = "GEOMETRY(${clazz.simpleName}, 4326)"

  override fun valueFromDB(value: Any): Any {
    return if (value is PGobject) {
      if (value is PGgeometry) {
        value.geometry
      } else {
        PGgeometry.geomFromString(value.value)
      }
    } else {
      value
    }
  }

  override fun notNullValueToDB(value: Any): Any {
    return if (value is Geometry) {
      PGgeometry(value)
    } else {
      value
    }
  }
}
