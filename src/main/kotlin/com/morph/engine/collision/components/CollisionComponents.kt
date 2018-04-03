package com.morph.engine.collision.components

import com.morph.engine.entities.Component
import com.morph.engine.entities.Entity
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f

data class BoundingBox2D @JvmOverloads constructor(var center: Vector2f, var halfSize: Vector2f, var isTrigger: Boolean = false) : Component() {
    val min: Vector2f
        get() = center - halfSize

    val max: Vector2f
        get() = center + halfSize

    fun intersects(other: BoundingBox2D): Boolean {
        /*
		 * ---------
		 * |       |
		 * |  b1   |            b2.minX < b1.maxX
		 * |    ---|---         b2.minY < b1.maxY
		 * |____|__|  |
		 *      |  b2 |
		 *      |_____|
		 */

        val (x, y) = this.min
        val (x1, y1) = this.max
        val (x2, y2) = other.min
        val (x3, y3) = other.max

        return (x2 < x1 && y2 < y1
                && x3 > x && y3 > y)
    }

    fun encloses(other: BoundingBox2D): Boolean {
        val (x, y) = this.min
        val (x1, y1) = this.max
        val (x2, y2) = other.min
        val (x3, y3) = other.max

        return (x < x2 && x1 > x3
                && y < y2 && y1 > y3)
    }

    operator fun contains(point: Vector2f): Boolean {
        val min = this.min
        val max = this.max

        return (min.x < point.x && point.x < max.x
                && min.y < point.y && point.y < max.y)
    }

    override fun init() {

    }

    override fun destroy() {

    }
}

data class CollisionComponent(val collidedEntity: Entity, val normal: Vector3f, val distance: Float, val time: Float) : Component() {
    var isHandled: Boolean = false
}

data class TriggerComponent(val collidedEntity: Entity, val normal: Vector3f) : Component() {
    var isHandled: Boolean = false
}
