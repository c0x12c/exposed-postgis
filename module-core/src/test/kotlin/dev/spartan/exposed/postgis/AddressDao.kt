package dev.spartan.exposed.postgis

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgis.Geometry

class AddressDao(
  private val database: Database
) {

  fun insert(address: Address): Address? {
    return transaction(database) {
      AddressTable
        .insert {
          it[id] = address.id
          it[point] = address.point
          it[polygon] = address.polygon
          it[multiPolygon] = address.multiPolygon
          it[lineString] = address.lineString
          it[multiLineString] = address.multiLineString
        }
        .resultedValues
        ?.firstOrNull()
        ?.asAddress()
    }
  }

  fun intersect(geometry: Geometry): Boolean {
    return transaction(database) {
      AddressTable
        .select { AddressTable.polygon.intersect(geometry) }
        .firstOrNull() != null
    }
  }

  fun overlap(geometry: Geometry): Boolean {
    return transaction(database) {
      AddressTable
        .select { AddressTable.polygon.overlap(geometry) }
        .firstOrNull() != null
    }
  }

  fun contain(geometry: Geometry): Boolean {
    return transaction(database) {
      AddressTable
        .select { AddressTable.polygon.contain(geometry) }
        .firstOrNull() != null
    }
  }

  fun byId(id: String): Address? {
    return transaction(database) {
      AddressTable
        .select {
          AddressTable.id eq id
        }
        .firstOrNull()
        ?.asAddress()
    }
  }

  private fun ResultRow.asAddress(): Address {
    return Address(
      id = this[AddressTable.id],
      point = this[AddressTable.point],
      polygon = this[AddressTable.polygon],
      multiPolygon = this[AddressTable.multiPolygon],
      lineString = this[AddressTable.lineString],
      multiLineString = this[AddressTable.multiLineString],
    )
  }
}
