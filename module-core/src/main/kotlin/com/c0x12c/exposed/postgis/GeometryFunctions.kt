package com.c0x12c.exposed.postgis

import org.jetbrains.exposed.sql.BooleanColumnType
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.DoubleColumnType
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.doubleLiteral
import org.jetbrains.exposed.sql.stringLiteral
import org.jetbrains.exposed.sql.stringLiteral
import org.postgis.Geometry

class IntersectFunction(e: Expression<*>, g: Geometry) :
  CustomFunction<Boolean>("st_intersects", BooleanColumnType(), e, stringLiteral(g.toString()))

/**
 * Check if the geometry intersects with the given geometry.
 *
 * @param geometry The geometry to check.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.intersect(geometry: Geometry): Op<Boolean> {
  return IntersectFunction(this, geometry).eq(true)
}

class WithinFunction(e: Expression<*>, g: Geometry) :
  CustomFunction<Boolean>("st_within", BooleanColumnType(), e, stringLiteral(g.toString()))

/**
 * Check if the geometry is within the given geometry.
 *
 * @param geometry The geometry to check.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.within(geometry: Geometry): Op<Boolean> {
  return WithinFunction(this, geometry).eq(true)
}

class OverlapFunction(e: Expression<*>, g: Geometry) :
  CustomFunction<Boolean>("st_overlaps", BooleanColumnType(), e, stringLiteral(g.toString()))

/**
 * Check if the geometry overlaps with the given geometry.
 *
 * @param geometry The geometry to check.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.overlap(geometry: Geometry): Op<Boolean> {
  return OverlapFunction(this, geometry).eq(true)
}

class ContainFunction(e: Expression<*>, g: Geometry) :
  CustomFunction<Boolean>("st_contains", BooleanColumnType(), e, stringLiteral(g.toString()))

/**
 * Check if the geometry contains the given geometry.
 *
 * @param geometry The geometry to check.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.contain(geometry: Geometry): Op<Boolean> {
  return ContainFunction(this, geometry).eq(true)
}

class DistanceFunction(e: Expression<*>, g: Geometry) :
  CustomFunction<Double>("st_distance", DoubleColumnType(), e, stringLiteral(g.toString()))

/**
 * Calculate the distance between the geometry and the given geometry.
 *
 * @param geometry The geometry to calculate the distance to.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.distance(geometry: Geometry): DistanceFunction {
  return DistanceFunction(this, geometry)
}

class WithinDistanceFunction(e: Expression<*>, g: Geometry, radius: Double) :
  CustomFunction<Boolean>("st_dwithin", BooleanColumnType(), e, stringLiteral(g.toString()), doubleLiteral(radius))

/**
 * Check if the geometry is within the given distance of the given geometry.
 *
 * @param geometry The geometry to check.
 * @param distance The distance to check.
 */
fun <G : Geometry> ExpressionWithColumnType<G>.withinDistance(geometry: Geometry, distance: Double): Op<Boolean> {
  return WithinDistanceFunction(this, geometry, distance).eq(true)
}

