package com.morph.engine.collision

import com.morph.engine.collision.components.BoundingBox2D
import com.morph.engine.entities.Entity
import com.morph.engine.math.Vector2f

data class BoundingBox2DSweep(val entity: Entity, val velocity: Vector2f, val isMoving: Boolean) {
    val boundingBox: BoundingBox2D
        get() = entity.getComponent(BoundingBox2D::class.java)!!
}
