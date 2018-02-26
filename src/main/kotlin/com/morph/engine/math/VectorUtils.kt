package com.morph.engine.math

object VectorUtils {
    fun triangleArea(v1: Vector2f, v2: Vector2f, v3: Vector2f): Float {
        val p1 = v1.sub(v3)
        val p2 = v2.sub(v3)

        return Math.abs(p1.x * p2.y - p1.y * p2.x) / 2
    }
}
