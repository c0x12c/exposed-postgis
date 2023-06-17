spartan-exposed-postgis
=======
This extension builds upon Exposed SQL, enabling the utilization of PostGIS types within your database schema.

## Usage
For each geometry type, there exists an extension method on the Table object that facilitates the 
creation of a column with the corresponding `Geometry` type.

```kotlin
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
```


## Example

### PostgreSQL schema
```sql
CREATE TABLE address (
  id text PRIMARY KEY,
  point geometry(Point, 4326),
  polygon geometry(Polygon, 4326),
  multi_polygon geometry(MultiPolygon, 4326),
  line_string geometry(LineString, 4326),
  multi_line_string geometry(MultiLineString, 4326)
);
```

### Exposed Table
```kotlin
object AddressTable : Table("address") {
  val id = text("id")
  val point = point("point")
  val polygon = polygon("polygon")
  val multiPolygon = multiPolygon("multi_polygon")
  val lineString = lineString("line_string")
  val multiLineString = multiLineString("multi_line_string")
}
```

### CRUD
```kotlin
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
```



